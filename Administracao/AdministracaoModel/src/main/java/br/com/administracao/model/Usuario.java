package br.com.administracao.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Usuario implements Serializable {
	
	private Long nro;
	private String usuario;
	private String senha;
	private String obs;
	private Date dataInclusao;
	private Date dataBaixa;
	private Boolean flgAtivo;
	private Boolean flgProfissional;
	private Boolean flgAdm;
	private Pessoa pessoa;		

	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Long getNro() {
		return nro;
	}
	public void setNro(Long nro) {
		this.nro = nro;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public Date getDataBaixa() {
		return dataBaixa;
	}
	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
	}
	public Boolean getFlgAtivo() {
		return flgAtivo;
	}
	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public Boolean getFlgProfissional() {
		return flgProfissional;
	}
	public void setFlgProfissional(Boolean flgProfissional) {
		this.flgProfissional = flgProfissional;
	}
	public Boolean getFlgAdm() {
		return flgAdm;
	}
	public void setFlgAdm(Boolean flgAdm) {
		this.flgAdm = flgAdm;
	}	
}
