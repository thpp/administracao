package br.com.administracao.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Acesso implements Serializable {

	private Usuario usuario;
	private Tela tela;
	private List<Permissoes> listaPermissoes;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tela getTela() {
		return tela;
	}

	public void setTela(Tela tela) {
		this.tela = tela;
	}

	public List<Permissoes> getListaPermissoes() {
		return listaPermissoes;
	}

	public void setListaPermissoes(List<Permissoes> listaPermissoes) {
		this.listaPermissoes = listaPermissoes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((listaPermissoes == null) ? 0 : listaPermissoes.hashCode());
		result = prime * result + ((tela == null) ? 0 : tela.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		Acesso other = (Acesso) obj;
		if (listaPermissoes == null) {
			if (other.listaPermissoes != null)
				return false;
		} else if (!listaPermissoes.equals(other.listaPermissoes))
			return false;
		if (tela == null) {
			if (other.tela != null)
				return false;
		} else if (!tela.equals(other.tela))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

}
