package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.administracao.client.UsuarioService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Usuario;
import br.com.administracao.util.CpfValidator;
import br.com.administracao.util.WebUtil;

@ManagedBean(name = "MBUsuario")
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -1750627356416442575L;

	private Usuario usuario;
	private List<Usuario> listaUsuarios = new ArrayList<Usuario>();
	private Integer caracteresMinimos = 3;
	private String textoBusca = "";
	private String cpf;

	@PostConstruct
	public void buscarLista() {
		try {
			UsuarioService service = (UsuarioService) WebUtil
					.getNamedObject(UsuarioService.NAME);
			listaUsuarios = service.listar(0, 0, null);

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void limparCamposBusca() {
		textoBusca = "";
		buscarLista();
	}

	public void pesquisar() {
		if (!"".equals(textoBusca)) {
			// Pesquisar pelo texto
			UsuarioService service = (UsuarioService) WebUtil
					.getNamedObject(UsuarioService.NAME);
			listaUsuarios = service.listar(0, 0, textoBusca);

		} else {
			// Pesquisar todos os usuários
			UsuarioService service = (UsuarioService) WebUtil
					.getNamedObject(UsuarioService.NAME);
			listaUsuarios = service.listar(0, 0, null);
		}
	}

	public void novo() {
		usuario = new Usuario();
	}

	public void validarSalvar() {
		cpf = cpf.replaceAll("\\/", "").replaceAll("\\.", "")
				.replaceAll("-", "").trim();

		if (usuario.getNome().length() > caracteresMinimos
				&& CpfValidator.validaCPF(this.cpf)) {
			salvar();
		} else {
			if (!(usuario.getNome().length() > caracteresMinimos)) {
				WebUtil.adicionarMensagemAviso("Usuário deve ter o nome maior que 3 caracteres");
			} else if (!CpfValidator.validaCPF(this.cpf)) {
				WebUtil.adicionarMensagemAviso("CPF inválido.");
			}
		}
	}

	public void salvar() {
		try {

			if (usuario.getNro() == null) {
				usuario.setCpf(cpf);
				usuario.setDataInclusao(new java.util.Date());
				UsuarioService service = (UsuarioService) WebUtil
						.getNamedObject(UsuarioService.NAME);
				service.inserir(usuario);
				WebUtil.adicionarMensagemSucesso("Usuário salvo com sucesso");

			} else {
				UsuarioService service = (UsuarioService) WebUtil
						.getNamedObject(UsuarioService.NAME);
				service.editar(usuario);
				WebUtil.adicionarMensagemSucesso("Usuário editado com sucesso");
			}
			org.primefaces.context.RequestContext.getCurrentInstance().execute(
					"PF('dlgNovo').hide();");

			buscarLista();
			cpf = "";

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void excluir() {
		try {

			UsuarioService service = (UsuarioService) WebUtil
					.getNamedObject(UsuarioService.NAME);
			service.excluir(usuario.getNro());
			WebUtil.adicionarMensagemSucesso("Usuário excluído com sucesso");

			buscarLista();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Integer getCaracteresMinimos() {
		return caracteresMinimos;
	}

	public void setCaracteresMinimos(Integer caracteresMinimos) {
		this.caracteresMinimos = caracteresMinimos;
	}

	public String getTextoBusca() {
		return textoBusca;
	}

	public void setTextoBusca(String textoBusca) {
		this.textoBusca = textoBusca;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
