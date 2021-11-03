package br.com.acredita.applicationlog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acredita.applicationlog.DTOout.LogDTOout;
import br.com.acredita.applicationlog.config.RestControllerPath;
import br.com.acredita.applicationlog.filters.LogFilter;
import br.com.acredita.applicationlog.models.Log;
import br.com.acredita.applicationlog.services.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(path = RestControllerPath.Log_PATH)
@Api(tags = "Log", description = "Logs")
public class LogController implements ILogController {

    @Autowired
    private LogService logService;

    @GetMapping("/")
	@ApiOperation(value = "Obtem Logs paginados por filter")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Página que você deseja recuperar (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Número de registros por página."),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort formato: propriedade,asc|desc. "
					+ "Padrão do sort é ascendente. " + "sort com multiplos campos são permitidos") })
    @Override
    public Page<LogDTOout> findByFilter(Pageable pageable, LogFilter filter) {
        return logService.findByFilter(pageable, filter);
    }

    @GetMapping("/{id}")
	@ApiOperation(value = "Obtem um Log através do identificador")
    @Override
    public ResponseEntity<Log> findById(@ApiParam(value = "identificador do Log requerido", required = true) @PathVariable Long id) {
		Optional<Log> busca = logService.findById(id);
		return busca.isPresent() ? ResponseEntity.ok(busca.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping("/to-data-table")
	@ApiOperation(value = "Obtem Logs para combobox ")
    @Override
    public List<LogDTOout> findToCombobox() {
        return logService.findToCombobox();
    }

    @GetMapping(path ="/to-report", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Lista todos os logs (para relatórios) ")
    @Override
    public List<Log> findToReport(LogFilter filter) {
        return logService.findToReport(filter);
    }
    
}
