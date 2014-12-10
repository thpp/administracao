package br.com.administracao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
	private List<Acesso> listaAcesso;

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

	public List<Acesso> getListaAcesso() {
		return listaAcesso;
	}

	public void setListaAcesso(List<Acesso> listaAcesso) {
		this.listaAcesso = listaAcesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataBaixa == null) ? 0 : dataBaixa.hashCode());
		result = prime * result
				+ ((dataInclusao == null) ? 0 : dataInclusao.hashCode());
		result = prime * result + ((flgAdm == null) ? 0 : flgAdm.hashCode());
		result = prime * result
				+ ((flgAtivo == null) ? 0 : flgAtivo.hashCode());
		result = prime * result
				+ ((flgProfissional == null) ? 0 : flgProfissional.hashCode());
		result = prime * result
				+ ((listaAcesso == null) ? 0 : listaAcesso.hashCode());
		result = prime * result + ((nro == null) ? 0 : nro.hashCode());
		result = prime * result + ((obs == null) ? 0 : obs.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
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
		Usuario other = (Usuario) obj;
		if (dataBaixa == null) {
			if (other.dataBaixa != null)
				return false;
		} else if (!dataBaixa.equals(other.dataBaixa))
			return false;
		if (dataInclusao == null) {
			if (other.dataInclusao != null)
				return false;
		} else if (!dataInclusao.equals(other.dataInclusao))
			return false;
		if (flgAdm == null) {
			if (other.flgAdm != null)
				return false;
		} else if (!flgAdm.equals(other.flgAdm))
			return false;
		if (flgAtivo == null) {
			if (other.flgAtivo != null)
				return false;
		} else if (!flgAtivo.equals(other.flgAtivo))
			return false;
		if (flgProfissional == null) {
			if (other.flgProfissional != null)
				return false;
		} else if (!flgProfissional.equals(other.flgProfissional))
			return false;
		if (listaAcesso == null) {
			if (other.listaAcesso != null)
				return false;
		} else if (!listaAcesso.equals(other.listaAcesso))
			return false;
		if (nro == null) {
			if (other.nro != null)
				return false;
		} else if (!nro.equals(other.nro))
			return false;
		if (obs == null) {
			if (other.obs != null)
				return false;
		} else if (!obs.equals(other.obs))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

}
