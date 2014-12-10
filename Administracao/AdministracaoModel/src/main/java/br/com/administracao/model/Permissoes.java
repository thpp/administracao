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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acesso == null) ? 0 : acesso.hashCode());
		result = prime * result + ((funcoes == null) ? 0 : funcoes.hashCode());
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
		Permissoes other = (Permissoes) obj;
		if (acesso == null) {
			if (other.acesso != null)
				return false;
		} else if (!acesso.equals(other.acesso))
			return false;
		if (funcoes == null) {
			if (other.funcoes != null)
				return false;
		} else if (!funcoes.equals(other.funcoes))
			return false;
		return true;
	}

}
