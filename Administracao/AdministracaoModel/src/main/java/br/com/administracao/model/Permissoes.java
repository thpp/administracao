package br.com.administracao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Permissoes implements Serializable {
	
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
