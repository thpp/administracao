package br.com.administracao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Modulo implements Serializable{
	
	private Long nro;
	private String nome;
	private Projeto projeto = new Projeto();
	
	public Long getNro() {
		return nro;
	}
	public void setNro(Long nro) {
		this.nro = nro;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Projeto getProjeto() {
		return projeto;
	}
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	
	

}
