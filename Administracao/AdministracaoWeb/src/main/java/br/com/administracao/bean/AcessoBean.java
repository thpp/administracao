package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import br.com.administracao.client.AcessoService;
import br.com.administracao.client.ModuloService;
import br.com.administracao.client.ProjetoService;
import br.com.administracao.client.TelaService;
import br.com.administracao.client.UsuarioService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Acesso;
import br.com.administracao.model.Funcoes;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Permissoes;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;
import br.com.administracao.model.Usuario;
import br.com.administracao.util.CpfValidator;
import br.com.administracao.util.WebUtil;

/**
 * 
 * @author Leticia Alves
 *
 */
@ManagedBean(name = "MBAcesso")
@ViewScoped
public class AcessoBean implements Serializable {

	private static final long serialVersionUID = 8843326651714475272L;

	/* PICKLIST */
	private DualListModel<Funcoes> funcoes;
	List<Funcoes> funcoesDisponiveis = new ArrayList<Funcoes>();
	List<Funcoes> funcoesSelecionadas = new ArrayList<Funcoes>();
	private Integer quantidadeFuncoesSelecionadas;
	int count = 0;

	/* CAMPOS DE BUSCA */
	private String textoBuscaUser = "";
	private String textoBuscaTela = "";
	private Long nroProjetoBusca = 0L;
	private Long nroModuloBusca = 0L;
	private List<Projeto> listaProjetos = new ArrayList<Projeto>();
	private List<Modulo> listaModulos = new ArrayList<Modulo>();
	private List<Tela> listaTelas = new ArrayList<Tela>();
	private List<Usuario> listaUsuarios = new ArrayList<Usuario>();

	/* EXIBIÇÃO */
	private String labelUsuarioSelecionado;

	/* DOMAIN */
	private Usuario usuario;
	private Tela tela;
	private Acesso acesso = new Acesso();
	private Acesso acessoSelecionado = null;
	private List<Acesso> listaAcessos = new ArrayList<Acesso>();
	private List<Permissoes> permissoes = new ArrayList<Permissoes>();

	/* RENDERED */
	private boolean existeFuncoes = false;

	public AcessoBean() {
		setLabelUsuarioSelecionado("Nenhum usuário selecionado");
		usuario = null;
	}

	public void limparCamposBuscaUser() {
		setTextoBuscaUser("");
		setLabelUsuarioSelecionado("Nenhum usuário selecionado");
		usuario = null;
	}

	public void buscarUsuario() {
		usuario = null;

		if (!"".equals(textoBuscaUser)) {
			String cpf = textoBuscaUser.replaceAll("\\/", "")
					.replaceAll("\\.", "").replaceAll("-", "").trim();

			if (CpfValidator.validaCPF(cpf)) {

				UsuarioService service = (UsuarioService) WebUtil
						.getNamedObject(UsuarioService.NAME);
				listaUsuarios = service.listar(cpf);

				if (listaUsuarios.size() == 1) {
					usuario = listaUsuarios.get(0);
					acesso.setUsuario(usuario);
					setLabelUsuarioSelecionado(usuario.getPessoa().getNome()
							+ " - Usuário: " + usuario.getUsuario());

					buscarAcessos();

				} else if (listaUsuarios.size() == 0) {
					setLabelUsuarioSelecionado("Não existe usuário com o cpf informado");
				} else {
					// Se por algum motivo houver o mesmo cpf para duas pessoas
					setLabelUsuarioSelecionado("Erro ao buscar cpf");
				}
			} else {
				setLabelUsuarioSelecionado("Cpf inválido");
			}

		} else {
			setLabelUsuarioSelecionado("Preencha o campo cpf");
		}

	}

	public void buscarAcessos() {

		AcessoService service = (AcessoService) WebUtil
				.getNamedObject(AcessoService.NAME);
		listaAcessos = service.listar(usuario);

		funcoes = new DualListModel<>(funcoesDisponiveis, funcoesSelecionadas);

	}

	public void prepararEditar(ActionEvent event) {
		acessoSelecionado = (Acesso) event.getComponent().getAttributes()
				.get("acessoSelecionado");

		// Todas as funções da tela
		List<Funcoes> disponiveis = new ArrayList<Funcoes>();
		disponiveis.addAll(acessoSelecionado.getTela().getListaFuncoes());

		// Todas as funções que o usuário acessa
		List<Funcoes> funcoesUsuario = new ArrayList<Funcoes>();
		for (Permissoes permissao : acessoSelecionado.getListaPermissoes()) {
			funcoesUsuario.add(permissao.getFuncoes());
		}

		// Funções disponíveis
		for (Funcoes funcaoTela : acessoSelecionado.getTela().getListaFuncoes()) {
			for (Funcoes funcaoUsuario : funcoesUsuario) {
				if (funcaoTela.equals(funcaoUsuario)) {
					disponiveis.remove(funcaoTela);
				}
			}
		}

		count = funcoesUsuario.size();
		quantidadeFuncoesSelecionadas = count;

		// Carrego o PickList
		funcoes = new DualListModel<>(disponiveis, funcoesUsuario);

		// Renderiza o PickList
		if (acessoSelecionado.getTela().getListaFuncoes().size() > 0)
			setExisteFuncoes(true);
	}

	public void pesquisarTelas() {

		if (!"".equals(textoBuscaTela) && nroProjetoBusca != 0
				&& nroModuloBusca != 0) {

			Projeto projeto = new Projeto();
			Modulo modulo = new Modulo();
			projeto.setNro(nroProjetoBusca);
			modulo.setNro(nroModuloBusca);

			// Pesquisar pelo texto digitado, número do projeto e número do
			// módulo
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, modulo, textoBuscaTela);

		} else if (!"".equals(textoBuscaTela) && nroProjetoBusca != 0) {

			Projeto projeto = new Projeto();
			projeto.setNro(nroProjetoBusca);

			// Pesquisar pelo texto digitado e número do projeto
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, null, textoBuscaTela);

		} else if (nroProjetoBusca != 0 && nroModuloBusca != 0) {

			Projeto projeto = new Projeto();
			Modulo modulo = new Modulo();
			projeto.setNro(nroProjetoBusca);
			modulo.setNro(nroModuloBusca);

			// Pesquisar pelo número do projeto e número do
			// módulo
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, modulo, null);

		} else if (!"".equals(textoBuscaTela)) {
			// Pesquisar pelo texto
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(null, null, textoBuscaTela);

		} else if (nroProjetoBusca != 0) {

			Projeto projeto = new Projeto();
			projeto.setNro(nroProjetoBusca);

			// Pesquisar pelo número do projeto
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, null, null);

		} else {

			// Pesquisar todas as telas
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(0, 0);
		}

	}

	public void prepararAdicionaTela() {
		tela = new Tela();

		funcoes = new DualListModel<Funcoes>(funcoesDisponiveis,
				funcoesSelecionadas);

		setExisteFuncoes(false);
	}

	public void buscarFuncoes(ActionEvent event) {
		quantidadeFuncoesSelecionadas = 0;
		count = 0;

		tela = (Tela) event.getComponent().getAttributes()
				.get("telaSelecionada");

		acesso.setTela(tela);

		funcoesDisponiveis = tela.getListaFuncoes();

		if (funcoesDisponiveis.size() > 0)
			setExisteFuncoes(true);

		funcoes = new DualListModel<Funcoes>(funcoesDisponiveis,
				funcoesSelecionadas);
	}

	public void prepararBuscaTela() {
		try {
			/* Popula combo de projetos */
			ProjetoService serviceProjeto = (ProjetoService) WebUtil
					.getNamedObject(ProjetoService.NAME);
			listaProjetos = serviceProjeto.listar(0, 0);

			/* Mostra todas as telas */
			TelaService serviceTela = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = serviceTela.listar(0, 0);

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void buscarModulos() {
		try {
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			listaModulos = service.listar(null, nroProjetoBusca);

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void limparCamposBuscaTela() {
		textoBuscaTela = "";
		nroModuloBusca = 0L;
		nroProjetoBusca = 0L;
		setExisteFuncoes(false);
		pesquisarTelas();
	}

	public void salvar() {

		if (funcoes.getTarget().size() > 0) {

			try {
				permissoes = new ArrayList<Permissoes>();

				for (Funcoes funcao : funcoes.getTarget()) {
					Permissoes p = new Permissoes();
					p.setFuncoes(funcao);
					p.setAcesso(acesso);
					permissoes.add(p);
				}

				acesso.setListaPermissoes(permissoes);

				AcessoService service = (AcessoService) WebUtil
						.getNamedObject(AcessoService.NAME);

				service.inserir(acesso);
				WebUtil.adicionarMensagemSucesso("Acesso salvo com sucesso");

				// Fecha o diálogo
				org.primefaces.context.RequestContext.getCurrentInstance()
						.execute("PF('dlgAddTela').hide();");

			} catch (Exception ex) {
				String mensagem = ex.getMessage();
				String[] mensagemSeparada = mensagem.split(":");
				System.out.println("Tamanho mensagem: "
						+ mensagemSeparada.length);
				int tamanhoMensagem = mensagemSeparada.length;

				WebUtil.adicionarMensagemErro(mensagemSeparada[tamanhoMensagem - 2]
						+ " : " + mensagemSeparada[tamanhoMensagem - 1]);

				RequestContext.getCurrentInstance().update("msgValorInvalido");

			} finally {
				buscarAcessos();
			}

		} else {
			WebUtil.adicionarMensagemAviso("Selecione pelo menos uma permissão");
		}

	}

	public void editar() {

		if (funcoes.getTarget().size() > 0) {

			try {
				permissoes = new ArrayList<Permissoes>();

				for (Funcoes funcao : funcoes.getTarget()) {
					Permissoes p = new Permissoes();
					p.setFuncoes(funcao);
					p.setAcesso(acessoSelecionado);
					permissoes.add(p);
				}

				acessoSelecionado.setListaPermissoes(permissoes);

				AcessoService service = (AcessoService) WebUtil
						.getNamedObject(AcessoService.NAME);

				service.editar(acessoSelecionado);
				WebUtil.adicionarMensagemSucesso("Acesso editado com sucesso");

				// Fecha o diálogo
				org.primefaces.context.RequestContext.getCurrentInstance()
						.execute("PF('dlgEdicao').hide();");

				acessoSelecionado = null;

			} catch (Exception ex) {
				String mensagem = ex.getMessage();
				String[] mensagemSeparada = mensagem.split(":");
				System.out.println("Tamanho mensagem: "
						+ mensagemSeparada.length);
				int tamanhoMensagem = mensagemSeparada.length;

				WebUtil.adicionarMensagemErro(mensagemSeparada[tamanhoMensagem - 2]
						+ " : " + mensagemSeparada[tamanhoMensagem - 1]);

				RequestContext.getCurrentInstance().update("msgValorInvalido");

			} finally {
				buscarAcessos();
			}

		} else {
			WebUtil.adicionarMensagemAviso("Selecione pelo menos uma permissão");
		}
	}

	public void excluir() {
		try {
			AcessoService service = (AcessoService) WebUtil
					.getNamedObject(AcessoService.NAME);
			service.excluir(acessoSelecionado);
			
			WebUtil.adicionarMensagemSucesso("Acesso excluído com sucesso");

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		} finally {
			buscarAcessos();
		}
	}

	/* PickList */
	public void onTransfer(TransferEvent event) {
		StringBuilder builder = new StringBuilder();
		for (Object item : event.getItems()) {
			builder.append(((Funcoes) item).getAcoes().getNome()).append(
					"<br />");
		}

		if (event.isAdd()) {

			count += event.getItems().size();

			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			int quantidadeItens = event.getItems().size();
			msg.setSummary(String.valueOf(quantidadeItens)
					+ ((quantidadeItens == 1) ? " permissão adicionada."
							: " permissões adicionadas."));
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else if (event.isRemove()) {

			count -= event.getItems().size();

			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			int quantidadeItens = event.getItems().size();
			msg.setSummary(String.valueOf(quantidadeItens)
					+ ((quantidadeItens == 1) ? " permissão removida."
							: " permissões removidas."));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		quantidadeFuncoesSelecionadas = count;
	}

	public DualListModel<Funcoes> getFuncoes() {
		return funcoes;
	}

	public void setFuncoes(DualListModel<Funcoes> funcoes) {
		this.funcoes = funcoes;
	}

	public Integer getQuantidadeFuncoesSelecionadas() {
		return quantidadeFuncoesSelecionadas;
	}

	public void setQuantidadeFuncoesSelecionadas(
			Integer quantidadeFuncoesSelecionadas) {
		this.quantidadeFuncoesSelecionadas = quantidadeFuncoesSelecionadas;
	}

	public String getTextoBuscaUser() {
		return textoBuscaUser;
	}

	public void setTextoBuscaUser(String textoBuscaUser) {
		this.textoBuscaUser = textoBuscaUser;
	}

	public String getTextoBuscaTela() {
		return textoBuscaTela;
	}

	public void setTextoBuscaTela(String textoBuscaTela) {
		this.textoBuscaTela = textoBuscaTela;
	}

	public Long getNroProjetoBusca() {
		return nroProjetoBusca;
	}

	public void setNroProjetoBusca(Long nroProjetoBusca) {
		this.nroProjetoBusca = nroProjetoBusca;
	}

	public Long getNroModuloBusca() {
		return nroModuloBusca;
	}

	public void setNroModuloBusca(Long nroModuloBusca) {
		this.nroModuloBusca = nroModuloBusca;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public List<Modulo> getListaModulos() {
		return listaModulos;
	}

	public void setListaModulos(List<Modulo> listaModulos) {
		this.listaModulos = listaModulos;
	}

	public List<Tela> getListaTelas() {
		return listaTelas;
	}

	public void setListaTelas(List<Tela> listaTelas) {
		this.listaTelas = listaTelas;
	}

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

	public Acesso getAcesso() {
		return acesso;
	}

	public void setAcesso(Acesso acesso) {
		this.acesso = acesso;
	}

	public List<Permissoes> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissoes> permissoes) {
		this.permissoes = permissoes;
	}

	public boolean isExisteFuncoes() {
		return existeFuncoes;
	}

	public void setExisteFuncoes(boolean existeFuncoes) {
		this.existeFuncoes = existeFuncoes;
	}

	public String getLabelUsuarioSelecionado() {
		return labelUsuarioSelecionado;
	}

	public void setLabelUsuarioSelecionado(String labelUsuarioSelecionado) {
		this.labelUsuarioSelecionado = labelUsuarioSelecionado;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Acesso> getListaAcessos() {
		return listaAcessos;
	}

	public void setListaAcessos(List<Acesso> listaAcessos) {
		this.listaAcessos = listaAcessos;
	}

	public Acesso getAcessoSelecionado() {
		return acessoSelecionado;
	}

	public void setAcessoSelecionado(Acesso acessoSelecionado) {
		this.acessoSelecionado = acessoSelecionado;
	}
}
