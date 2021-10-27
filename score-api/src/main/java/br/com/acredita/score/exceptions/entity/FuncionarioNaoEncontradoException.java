package br.com.acredita.score.exceptions.entity;

import br.com.acredita.score.exceptions.EntidadeNaoEncontradaException;


public class FuncionarioNaoEncontradoException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public FuncionarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public FuncionarioNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de funcionario com código %d", id));
	}
	
}