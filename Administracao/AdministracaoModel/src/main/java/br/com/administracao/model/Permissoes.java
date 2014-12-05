package br.com.administracao.model;

public class Permissoes {
	
	private Acesso acesso;
	private Funcoes funcoes;
	
	public Acesso getAcesso() {
		return acesso;
	}
	public void setAcesso(Acesso acesso) {
		this.acesso = acesso;
	}
	public Funcoes getFuncoes() {
		return funcoes;
	}
	public void setFuncoes(Funcoes funcoes) {
		this.funcoes = funcoes;
	}	
}
