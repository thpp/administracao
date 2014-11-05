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

import br.com.administracao.client.ModuloService;
import br.com.administracao.client.ProjetoService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;
import br.com.administracao.util.WebUtil;

@ManagedBean(name = "MBTela")
@ViewScoped
public class TelaBean implements Serializable {

	private static final long serialVersionUID = -2062657756767836389L;

	private DualListModel<Acoes> acoes;
	private TabView tabView;
	private Integer activeTabIndex;

	private String textoBusca = "";
	private Long nroProjetoBusca = 0L;
	private Long nroModuloBusca = 0L;

	private Tela tela = new Tela();
	private List<Tela> telas = new ArrayList<Tela>();
	private List<Projeto> listaProjetos = new ArrayList<Projeto>();
	private List<Modulo> listaModulos = new ArrayList<Modulo>();

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
	}

	public void prepararNovo() {
		// Ações
		List<Acoes> themesSource = new ArrayList<Acoes>(); // Vai receber a
															// listagem de Ações
		List<Acoes> themesTarget = new ArrayList<Acoes>();

		acoes = new DualListModel<Acoes>(themesSource, themesTarget);

		buscarProjetos();
	}

	public void novo() {
		tabView.setActiveIndex(1);
		activeTabIndex = tabView.getActiveIndex();

		prepararNovo();
		limparCamposBusca();
	}

	public void onTabChange(TabChangeEvent event) {

		activeTabIndex = ((TabView) event.getComponent()).getActiveIndex();

		if (activeTabIndex == 1) {
			prepararNovo();
		}

		limparCamposBusca();
	}

	public void onTransfer(TransferEvent event) {
		StringBuilder builder = new StringBuilder();
		for (Object item : event.getItems()) {
			builder.append(((Acoes) item).getNome()).append("<br />");
		}

		FacesMessage msg = new FacesMessage();
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		msg.setSummary("Items Transferred");
		msg.setDetail(builder.toString());

		FacesContext.getCurrentInstance().addMessage(null, msg);
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

}