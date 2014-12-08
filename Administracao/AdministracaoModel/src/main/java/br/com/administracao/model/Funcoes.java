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

	public Long getHashCode() {
		return (long) hashCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acoes == null) ? 0 : acoes.hashCode());
		result = prime * result + ((tela == null) ? 0 : tela.hashCode());
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
		Funcoes other = (Funcoes) obj;
		if (acoes == null) {
			if (other.acoes != null)
				return false;
		} else if (!acoes.equals(other.acoes))
			return false;
		if (tela == null) {
			if (other.tela != null)
				return false;
		} else if (!tela.equals(other.tela))
			return false;
		return true;
	}
}
