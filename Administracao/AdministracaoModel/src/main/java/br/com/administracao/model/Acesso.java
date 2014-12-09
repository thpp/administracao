package br.com.administracao.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Acesso implements Serializable {
	
	private Usuario usuario;
	private Tela tela;
	private List<Permissoes> listaPermicoes;
		
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
	public List<Permissoes> getListaPermicoes() {
		return listaPermicoes;
	}
	public void setListaPermicoes(List<Permissoes> listaPermicoes) {
		this.listaPermicoes = listaPermicoes;
	}	

}
