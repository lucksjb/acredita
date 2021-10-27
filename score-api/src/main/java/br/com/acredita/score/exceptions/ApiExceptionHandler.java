// UTILIZAR A RFC 7807 - que padroniza as mensagens HTTP
// https://www.rfc-editor.org/info/rfc7807
package br.com.acredita.score.exceptions;

import java.beans.Introspector;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {

		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, HttpStatus status,
			WebRequest request, BindingResult bindingResult) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

		List<Problem.Campos> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {
			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

			String name = objectError.getObjectName();

			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}

			return Problem.Campos.builder().name(name).userMessage(message).build();
		}).collect(Collectors.toList());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).campos(problemObjects)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler({ ValidacaoException.class })
	public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {
		return handleValidationInternal(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request, ex.getBindingResult());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

		log.error(ex.getMessage(), ex);

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' não existe. " + "Corrija ou remova essa propriedade e tente novamente.", path);

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = Problem.builder().timestamp(OffsetDateTime.now()).title(status.getReasonPhrase())
					.status(status.value()).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		} else if (body instanceof String) {
			body = Problem.builder().timestamp(OffsetDateTime.now()).title((String) body).status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {

		return Problem.builder().timestamp(OffsetDateTime.now()).status(status.value()).type(problemType.getUri())
				.title(problemType.getTitle()).detail(detail);
	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

		List<Problem.Campos> problemObjects = new ArrayList<>();

		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String objectName = Introspector.decapitalize(violation.getRootBean().getClass().getSimpleName());
			String field = String.valueOf(violation.getPropertyPath());

			String campo = messageSource.getMessage(objectName + "." + field, null, null,
					LocaleContextHolder.getLocale());

			String message = messageSource.getMessage(violation.getMessage(),
					new Object[] { campo, violation.getPropertyPath().toString(), violation.getInvalidValue() },
					violation.getMessage(), LocaleContextHolder.getLocale());

			problemObjects.add(
					Problem.Campos.builder().name(violation.getPropertyPath().toString()).userMessage(message).build());
		}

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

		Problem problem = createProblemBuilder(status, problemType, detail).campos(problemObjects).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

	}

	@ExceptionHandler({ SecurityException.class })
	public ResponseEntity<Object> securityException(SecurityException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		ProblemType problemType = ProblemType.ACESSO_NEGADO;
		String detail = "Acesso negado, você não tem permissão para acessar esse recurso ";

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.OPERACAO_NAO_PERMITIDA;
		String detail = "Operação não permitida ";
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = "Recurso não encontrado ";
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

	}

	@ExceptionHandler({ RedisCommandTimeoutException.class, RedisConnectionException.class,
			RedisConnectionFailureException.class })
	public ResponseEntity<Object> handleRedisCommandTimeoutException(RuntimeException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = "Redis server is down ";
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler({ WebClientRequestException.class })
	public ResponseEntity<Object> handleWebClientRequestException(WebClientRequestException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		
		String detail = String.format("Cannot connect '%s'  - API is down  ", ex.getUri().toString()) ;
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

}
