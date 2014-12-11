package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import br.com.administracao.client.AcoesService;
import br.com.administracao.client.ModuloService;
import br.com.administracao.client.ProjetoService;
import br.com.administracao.client.TelaService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Funcoes;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;
import br.com.administracao.util.WebUtil;

/**
 * @author Leticia Alves
 */
@ManagedBean(name = "MBTela")
@ViewScoped
public class TelaBean implements Serializable {

	private static final long serialVersionUID = -2062657756767836389L;

	/* TABVIEW */
	private transient TabView tabView; // Não precisa ser serializada
	private Integer activeTabIndex;

	/* PICKLIST */
	private DualListModel<Acoes> acoes;
	List<Acoes> acoesDisponiveis = new ArrayList<Acoes>();
	List<Acoes> acoesSelecionadas = new ArrayList<Acoes>();
	private Integer quantidadeAcoesSelecionadas;
	int count = 0;

	/* CAMPOS DE BUSCA */
	private String textoBusca = "";
	private Long nroProjetoBusca = 0L;
	private Long nroModuloBusca = 0L;
	private List<Projeto> listaProjetos = new ArrayList<Projeto>();
	private List<Modulo> listaModulos = new ArrayList<Modulo>();

	/* DOMAIN */
	private Tela tela = new Tela();
	private Integer caracteresMinimos = 3;
	private List<Tela> listaTelas = new ArrayList<Tela>();
	private List<Funcoes> funcoes;

	/* RENDERED */
	private boolean existeAcoes = false;

	@PostConstruct
	public void buscarLista() {
		buscarProjetos();

		TelaService service = (TelaService) WebUtil
				.getNamedObject(TelaService.NAME);
		listaTelas = service.listar(0, 0);
	}

	public void pesquisar() {

		if (!"".equals(textoBusca) && nroProjetoBusca != 0
				&& nroModuloBusca != 0) {
			textoBusca = textoBusca.toUpperCase();

			Projeto projeto = new Projeto();
			Modulo modulo = new Modulo();
			projeto.setNro(nroProjetoBusca);
			modulo.setNro(nroModuloBusca);

			// Pesquisar pelo texto digitado, número do projeto e número do
			// módulo
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, modulo, textoBusca);

		} else if (!"".equals(textoBusca) && nroProjetoBusca != 0) {
			textoBusca = textoBusca.toUpperCase();

			Projeto projeto = new Projeto();
			projeto.setNro(nroProjetoBusca);

			// Pesquisar pelo texto digitado e número do projeto
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, null, textoBusca);

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

		} else if (!"".equals(textoBusca)) {
			textoBusca = textoBusca.toUpperCase();
			// Pesquisar pelo texto
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(null, null, textoBusca);

		} else if (nroProjetoBusca != 0) {

			Projeto projeto = new Projeto();
			projeto.setNro(nroProjetoBusca);

			// Pesquisar pelo número do projeto
			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			listaTelas = service.listar(projeto, null, null);

		} else {

			// Pesquisar todas as telas
			buscarLista();
		}

	}

	public void buscarProjetos() {
		try {
			ProjetoService service = (ProjetoService) WebUtil
					.getNamedObject(ProjetoService.NAME);
			listaProjetos = service.listar(0, 0);
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

	public void buscarFuncoes() {
		funcoes = tela.getListaFuncoes();
	}

	public void limparCamposBusca() {
		textoBusca = "";
		nroModuloBusca = 0L;
		nroProjetoBusca = 0L;
		quantidadeAcoesSelecionadas = 0;
		count = 0;
		setExisteAcoes(false);
		buscarLista();
	}

	public void prepararNovo() {

		tela = new Tela();
		funcoes = new ArrayList<Funcoes>();

		// Ações
		AcoesService service = (AcoesService) WebUtil
				.getNamedObject(AcoesService.NAME);

		acoesDisponiveis = service.listar();
		acoesSelecionadas = new ArrayList<Acoes>();

		acoes = new DualListModel<Acoes>(acoesDisponiveis, acoesSelecionadas);

		buscarProjetos();

		if (acoesDisponiveis.size() > 0)
			setExisteAcoes(true);
	}

	public void novo() {
		tabView.setActiveIndex(1);
		activeTabIndex = tabView.getActiveIndex();

		limparCamposBusca();

		prepararNovo();

	}

	public void validarSalvar() {

		if (tela.getNome().length() > caracteresMinimos && nroProjetoBusca > 0
				&& tela.getModulo().getNro() > 0
				&& acoes.getTarget().size() > 0) {

			salvar();

		} else {
			if (!(tela.getNome().length() > caracteresMinimos)) {
				WebUtil.adicionarMensagemAviso("Tela deve ter o nome maior que 3 caracteres");
			} else if (!(nroProjetoBusca > 0)) {
				WebUtil.adicionarMensagemAviso("Tela deve ter um módulo correspondente. Comece escolhendo o projeto.");
			} else if (!(tela.getModulo().getNro() > 0)) {
				WebUtil.adicionarMensagemAviso("Tela deve ter um módulo correspondente");
			} else if (!(acoes.getTarget().size() > 0)) {
				WebUtil.adicionarMensagemAviso("Tela deve ter pelo menos uma função");
			}

		}
	}

	public void salvar() {
		try {

			funcoes = new ArrayList<Funcoes>();/* Importante na edição */

			for (Acoes acao : acoes.getTarget()) {
				Funcoes f = new Funcoes();
				f.setAcoes(acao);
				funcoes.add(f);
			}
			tela.setListaFuncoes(funcoes);

			if (tela.getNro() == null) {
				TelaService service = (TelaService) WebUtil
						.getNamedObject(TelaService.NAME);
				service.inserir(tela);
				WebUtil.adicionarMensagemSucesso("Tela salva com sucesso");
			} else {
				TelaService service = (TelaService) WebUtil
						.getNamedObject(TelaService.NAME);
				service.editar(tela);
				WebUtil.adicionarMensagemSucesso("Tela editada com sucesso");
			}

			tabView.setActiveIndex(0);
			activeTabIndex = tabView.getActiveIndex();

			limparCamposBusca();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}

	}

	public void prepararEditar(ActionEvent event) {

		tela = (Tela) event.getComponent().getAttributes()
				.get("telaSelecionada");

		tabView.setActiveIndex(1);
		activeTabIndex = tabView.getActiveIndex();

		buscarProjetos();

		nroProjetoBusca = tela.getModulo().getProjeto().getNro();
		buscarModulos();

		// Ações
		AcoesService service = (AcoesService) WebUtil
				.getNamedObject(AcoesService.NAME);

		List<Acoes> todasAcoes = service.listar();

		if (todasAcoes.size() > 0)
			setExisteAcoes(true);

		List<Acoes> acoesTela = new ArrayList<Acoes>();
		acoesDisponiveis = service.listar();
		buscarFuncoes();

		for (Funcoes funcao : funcoes) {
			acoesTela.add(funcao.getAcoes());
		}

		for (Acoes acao : todasAcoes) {

			for (Acoes acaoTela : acoesTela) {
				if (acaoTela.equals(acao)) {
					acoesDisponiveis.remove(acaoTela);
				}
			}
		}

		count = acoesTela.size();
		quantidadeAcoesSelecionadas = count;

		acoes = new DualListModel<Acoes>(acoesDisponiveis, acoesTela);

	}

	public void cancelarCadastro() {
		tabView.setActiveIndex(0);
		activeTabIndex = tabView.getActiveIndex();

		limparCamposBusca();
	}

	public void excluir() {
		try {

			TelaService service = (TelaService) WebUtil
					.getNamedObject(TelaService.NAME);
			service.excluir(tela.getNro());
			WebUtil.adicionarMensagemSucesso("Tela excluída com sucesso");

			buscarLista();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void onTabChange(TabChangeEvent event) {

		activeTabIndex = ((TabView) event.getComponent()).getActiveIndex();

		limparCamposBusca();

		if (activeTabIndex == 1) {
			prepararNovo();
		}

	}

	public void onTransfer(TransferEvent event) {
		StringBuilder builder = new StringBuilder();
		for (Object item : event.getItems()) {
			builder.append(((Acoes) item).getNome()).append("<br />");
		}

		if (event.isAdd()) {

			count += event.getItems().size();

			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			int quantidadeItens = event.getItems().size();
			msg.setSummary(String.valueOf(quantidadeItens)
					+ ((quantidadeItens == 1) ? " ação adicionada."
							: " ações adicionadas."));
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else if (event.isRemove()) {

			count = count - event.getItems().size();

			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			int quantidadeItens = event.getItems().size();
			msg.setSummary(String.valueOf(quantidadeItens)
					+ ((quantidadeItens == 1) ? " ação removida."
							: " ações removidas."));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		quantidadeAcoesSelecionadas = count;
	}

	public DualListModel<Acoes> getAcoes() {
		return acoes;
	}

	public void setAcoes(DualListModel<Acoes> acoes) {
		this.acoes = acoes;
	}

	public TabView getTabView() {
		return tabView;
	}

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}

	public Integer getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(Integer activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public String getTextoBusca() {
		return textoBusca;
	}

	public void setTextoBusca(String textoBusca) {
		this.textoBusca = textoBusca;
	}

	public Long getNroProjetoBusca() {
		return nroProjetoBusca;
	}

	public void setNroProjetoBusca(Long nroProjetoBusca) {
		this.nroProjetoBusca = nroProjetoBusca;
	}

	public Tela getTela() {
		return tela;
	}

	public void setTela(Tela tela) {
		this.tela = tela;
	}

	public List<Tela> getListaTelas() {
		return listaTelas;
	}

	public void setListaTelas(List<Tela> listaTelas) {
		this.listaTelas = listaTelas;
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

	public Long getNroModuloBusca() {
		return nroModuloBusca;
	}

	public void setNroModuloBusca(Long nroModuloBusca) {
		this.nroModuloBusca = nroModuloBusca;
	}

	public List<Acoes> getAcoesDisponiveis() {
		return acoesDisponiveis;
	}

	public void setAcoesDisponiveis(List<Acoes> acoesDisponiveis) {
		this.acoesDisponiveis = acoesDisponiveis;
	}

	public List<Acoes> getAcoesSelecionadas() {
		return acoesSelecionadas;
	}

	public void setAcoesSelecionadas(List<Acoes> acoesSelecionadas) {
		this.acoesSelecionadas = acoesSelecionadas;
	}

	public Integer getquantidadeAcoesSelecionadas() {
		return quantidadeAcoesSelecionadas;
	}

	public void setquantidadeAcoesSelecionadas(
			Integer quantidadeAcoesSelecionadas) {
		this.quantidadeAcoesSelecionadas = quantidadeAcoesSelecionadas;
	}

	public List<Funcoes> getFuncoes() {
		return funcoes;
	}

	public void setFuncoes(List<Funcoes> funcoes) {
		this.funcoes = funcoes;
	}

	public boolean isExisteAcoes() {
		return existeAcoes;
	}

	public void setExisteAcoes(boolean existeAcoes) {
		this.existeAcoes = existeAcoes;
	}

}