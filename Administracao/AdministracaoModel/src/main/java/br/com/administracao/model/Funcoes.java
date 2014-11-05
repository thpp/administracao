package br.com.administracao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Funcoes implements Serializable {
	
	private Tela tela;
	private Acoes acoes;
	
	
	public Tela getTela() {
		return tela;
	}
	public void setTela(Tela tela) {
		this.tela = tela;
	}
	public Acoes getAcoes() {
		return acoes;
	}
	public void setAcoes(Acoes acoes) {
		this.acoes = acoes;
	}	

}
