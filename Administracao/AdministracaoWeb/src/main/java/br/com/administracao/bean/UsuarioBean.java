package br.com.administracao.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import br.com.administracao.client.UsuarioService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Pessoa;
import br.com.administracao.model.Usuario;
import br.com.administracao.util.CpfValidator;
import br.com.administracao.util.WebUtil;

/**
 * @author Leticia Alves
 */
@ManagedBean(name = "MBUsuario")
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -1750627356416442575L;

	private Usuario usuario;
	private Pessoa pessoa;
	private List<Usuario> listaUsuarios = new ArrayList<Usuario>();
	private Integer caracteresMinimos = 3;
	private String textoBusca = "";
	private String cpf;

	@PostConstruct
	public void buscarLista() {
		try {
			UsuarioService service = (UsuarioService) WebUtil
					.getNamedObject(UsuarioService.NAME);
			listaUsuarios = service.listar(0, 0, null, "S");

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
			// listaUsuarios = service.listar(0, 0, textoBusca);

		} else {
			// Pesquisar todos os usuários
			UsuarioService service = (UsuarioService) WebUtil
					.getNamedObject(UsuarioService.NAME);
			// listaUsuarios = service.listar(0, 0, null);
		}
	}

	public void novo() {
		cpf = "";
		usuario = new Usuario();
		pessoa = new Pessoa();
		usuario.setPessoa(pessoa);
		// Senha padrão
		usuario.setSenha("123456");
	}

	public void editar() {
		// cpf = usuario.getCpf();
		salvar();
	}

	public void salvar() {
		try {
			cpf = cpf.replaceAll("\\/", "").replaceAll("\\.", "")
					.replaceAll("-", "").trim();

			/* Validação */
			if (usuario.getPessoa().getNome().length() > caracteresMinimos
					&& CpfValidator.validaCPF(this.cpf)) {
				if (usuario.getNro() == null) {
					Boolean pessoaGravada = null;

					usuario.getPessoa().setCpf(cpf);
					usuario.setDataInclusao(new java.util.Date());
					UsuarioService service = (UsuarioService) WebUtil
							.getNamedObject(UsuarioService.NAME);

					pessoaGravada = service.inserir(usuario);
					WebUtil.adicionarMensagemSucesso("Usuário salvo com sucesso");

					// Fecha o diálogo
					org.primefaces.context.RequestContext.getCurrentInstance()
							.execute("PF('dlgNovo').hide();");

					if (true) {
						// A pessoa já existia na tabela
						RequestContext.getCurrentInstance().update(
								"formInfo:pnlInfo");
						org.primefaces.context.RequestContext
								.getCurrentInstance().execute(
										"PF('dlgInfoEdicaoPessoa').show();");

					}

				} else {
					// editar
				}
				// Atualiza a mensagem de sucesso
				RequestContext.getCurrentInstance().update("form1:msgs");
				cpf = "";
			} else {
				if (!(usuario.getPessoa().getNome().length() > caracteresMinimos)) {
					WebUtil.adicionarMensagemAviso("Usuário deve ter o nome maior que 3 caracteres");
					RequestContext.getCurrentInstance().update(
							"msgValorInvalido");
				} else if (!CpfValidator.validaCPF(this.cpf)) {
					/* Exibe p:growl de cpf inválido */
					WebUtil.adicionarMensagemAviso("CPF inválido.");
					RequestContext.getCurrentInstance().update(
							"msgValorInvalido");
				}
			}

			buscarLista();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro("Service Exception");
			RequestContext.getCurrentInstance().update("msgValorInvalido");
		} catch (Exception ex) {
			String mensagem = ex.getMessage();
			String[] mensagemSeparada = mensagem.split(":");
			System.out.println("Tamanho mensagem: " + mensagemSeparada.length);
			WebUtil.adicionarMensagemErro(mensagemSeparada[3] + " : "
					+ mensagemSeparada[4]);
			for (int i = 0; i < mensagemSeparada.length; i++)
				System.out.println(mensagemSeparada[i]);
			RequestContext.getCurrentInstance().update("msgValorInvalido");
		}
		// try {
		// cpf = cpf.replaceAll("\\/", "").replaceAll("\\.", "")
		// .replaceAll("-", "").trim();
		//
		// /* Validação */
		// if (usuario.getNome().length() > caracteresMinimos
		// && CpfValidator.validaCPF(this.cpf)) {
		//
		// if (usuario.getNro() == null) {
		//
		// usuario.setCpf(cpf);
		// usuario.setDataInclusao(new java.util.Date());
		//
		// UsuarioService service = (UsuarioService) WebUtil
		// .getNamedObject(UsuarioService.NAME);
		// service.inserir(usuario);
		// WebUtil.adicionarMensagemSucesso("Usuário salvo com sucesso");
		//
		// // Fecha o diálogo
		// org.primefaces.context.RequestContext.getCurrentInstance()
		// .execute("PF('dlgNovo').hide();");
		//
		// } else {
		// UsuarioService service = (UsuarioService) WebUtil
		// .getNamedObject(UsuarioService.NAME);
		// service.editar(usuario);
		// WebUtil.adicionarMensagemSucesso("Usuário editado com sucesso");
		//
		// // Fecha o diálogo
		// org.primefaces.context.RequestContext.getCurrentInstance()
		// .execute("PF('dlgEdicao').hide();");
		//
		// }
		//
		// // Atualiza a mensagem de sucesso
		// RequestContext.getCurrentInstance().update("form1:msgs");
		//
		// } else {
		// if (!(usuario.getNome().length() > caracteresMinimos)) {
		// WebUtil.adicionarMensagemAviso("Usuário deve ter o nome maior que 3 caracteres");
		// RequestContext.getCurrentInstance().update(
		// "msgValorInvalido");
		// } else if (!CpfValidator.validaCPF(this.cpf)) {
		// /* Exibe p:growl de cpf inválido */
		// WebUtil.adicionarMensagemAviso("CPF inválido.");
		// RequestContext.getCurrentInstance().update(
		// "msgValorInvalido");
		// }
		//
		// }
		//
		// buscarLista();
		// cpf = "";
		//
		// } catch (ServiceException ex) {
		// WebUtil.adicionarMensagemErro(ex.getMessage());
		// }
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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
