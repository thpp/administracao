package br.com.administracao.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Tela implements Serializable {
	
	private Long nro;
	private String nome;
	private Modulo modulo = new Modulo();
	private List<Funcoes> listaFuncoes;
	
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
	public Modulo getModulo() {
		return modulo;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public List<Funcoes> getListaFuncoes() {
		return listaFuncoes;
	}
	public void setListaFuncoes(List<Funcoes> listaFuncoes) {
		this.listaFuncoes = listaFuncoes;
	}
	
	

}
