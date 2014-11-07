package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
	private List<Tela> telas = new ArrayList<Tela>();
	private List<Funcoes> funcoes;

	@PostConstruct
	public void buscarLista() {
		buscarProjetos();
	}

	public void buscarPorNome() {

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

	public void limparCamposBusca() {
		textoBusca = "";
		nroProjetoBusca = 0L;
		nroModuloBusca = 0L;
		quantidadeAcoesSelecionadas = 0;
		count = 0;
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

			for (Acoes acao : acoes.getTarget()) {
				Funcoes f = new Funcoes();
				f.setAcoes(acao);
				funcoes.add(f);
			}
			System.out.println("Tamanho Lista Funções: " + funcoes.size());
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

			limparCamposBusca();

			tabView.setActiveIndex(0);
			activeTabIndex = tabView.getActiveIndex();

			// buscar()

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}

	}

	public void testeDuplaChamada() {
		System.out.println("PASSOU PELO MÉTODO");
	}

	public void prepararEditar() {

		tela = new Tela();

		// Ações
		AcoesService service = (AcoesService) WebUtil
				.getNamedObject(AcoesService.NAME);

		acoesDisponiveis = service.listar();
		acoesSelecionadas = service.listar(); // Carregar as ações da tela
												// selecionada
		List<Acoes> resultado = new ArrayList<Acoes>();

		for (Acoes acao : acoesDisponiveis) {
			if (!acoesDisponiveis.contains(acao)) {
				resultado.add(acao);
			}
		}

		count = acoesSelecionadas.size();
		quantidadeAcoesSelecionadas = count;

		acoes = new DualListModel<Acoes>(resultado, acoesSelecionadas);

		buscarProjetos();
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
			msg.setSummary(String.valueOf(event.getItems().size())
					+ " ação(ões) adicionada(s).");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else if (event.isRemove()) {

			count = count - event.getItems().size();

			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary(String.valueOf(event.getItems().size())
					+ " ação(ões) removida(s).");
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

	public List<Tela> getTelas() {
		return telas;
	}

	public void setTelas(List<Tela> telas) {
		this.telas = telas;
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

}