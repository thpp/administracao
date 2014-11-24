package br.com.administracao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Pessoa implements Serializable {
	
	private Long nro;
	private String nome;
	private String flgPessoa;
	private String cpf;
	
	public Pessoa() {	
	}
	
	public Pessoa(Long nro, String nome, String flgPessoa, String cpf) {
		super();
		this.nro = nro;
		this.nome = nome;
		this.flgPessoa = flgPessoa;
		this.cpf = cpf;
	}
	
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
	public String getFlgPessoa() {
		return flgPessoa;
	}
	public void setFlgPessoa(String flgPessoa) {
		this.flgPessoa = flgPessoa;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	
	

}
