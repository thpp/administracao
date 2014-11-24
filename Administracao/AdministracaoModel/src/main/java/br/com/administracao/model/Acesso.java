package br.com.administracao.model;

public class Acesso {
	
	private Usuario usuario;
	private Tela tela;
		
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

}
