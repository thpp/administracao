package br.com.administracao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Projeto implements Serializable {
		
	private Long nro;
	private String nome;
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
			
}
