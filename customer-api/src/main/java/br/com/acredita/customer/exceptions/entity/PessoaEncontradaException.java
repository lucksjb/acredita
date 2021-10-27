package br.com.acredita.customer.exceptions.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.acredita.customer.exceptions.EntidadeNaoEncontradaException;


public class PessoaEncontradaException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public PessoaEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public PessoaEncontradaException(String cpf, LocalDate dtNasc) {
		this( String.format("Não existe uma pessoa com o cpf %s  e data de nascimento %s ", cpf, dtNasc.format(formatter)));
	}
	
}