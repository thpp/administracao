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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result
				+ ((flgPessoa == null) ? 0 : flgPessoa.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((nro == null) ? 0 : nro.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (flgPessoa == null) {
			if (other.flgPessoa != null)
				return false;
		} else if (!flgPessoa.equals(other.flgPessoa))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (nro == null) {
			if (other.nro != null)
				return false;
		} else if (!nro.equals(other.nro))
			return false;
		return true;
	}

}
