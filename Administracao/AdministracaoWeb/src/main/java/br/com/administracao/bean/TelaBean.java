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

import br.com.administracao.model.Acoes;
import br.com.administracao.util.WebUtil;

@ManagedBean(name = "MBTela")
@ViewScoped
public class TelaBean implements Serializable {

	private static final long serialVersionUID = -2062657756767836389L;

	private DualListModel<Acoes> acoes;

	private String escolhaBusca = "tela";
	private String textoBusca = "";

	private TabView tabView;
	private Integer activeTabIndex;

	@PostConstruct
	public void buscarLista() {

	}

	public void buscarPorNome() {
		if ("tela".equals(escolhaBusca)) {
			System.out.println("Busca Por Tela");
			textoBusca = "";
		} else if ("modulo".equals(escolhaBusca)) {
			System.out.println("Busca Por Módulo");
			textoBusca = "";
		} else if ("projeto".equals(escolhaBusca)) {
			System.out.println("Busca Por Projeto");
			textoBusca = "";
		} else {
			WebUtil.adicionarMensagemAviso("Selecione um filtro para busca!");
		}
	}

	public void novo() {
		tabView.setActiveIndex(1);
		activeTabIndex = tabView.getActiveIndex();
		// Ações
		List<Acoes> themesSource = new ArrayList<Acoes>(); // Vai receber a
															// listagem de Ações
		List<Acoes> themesTarget = new ArrayList<Acoes>();

		acoes = new DualListModel<Acoes>(themesSource, themesTarget);
	}

	public void onTabChange(TabChangeEvent event) {
		System.out.println(((TabView) event.getComponent()).getActiveIndex());
		activeTabIndex = ((TabView) event.getComponent()).getActiveIndex();
		if (activeTabIndex == 1) {
			// Ações
			List<Acoes> themesSource = new ArrayList<Acoes>(); // Vai receber a
																// listagem de
																// Ações
			List<Acoes> themesTarget = new ArrayList<Acoes>();

			acoes = new DualListModel<Acoes>(themesSource, themesTarget);
		}
	}

	public TabView getTabView() {
		return tabView;
	}

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}

	public DualListModel<Acoes> getAcoes() {
		return acoes;
	}

	public void setAcoes(DualListModel<Acoes> acoes) {
		this.acoes = acoes;
	}

	public Integer getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(Integer activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public String getEscolhaBusca() {
		return escolhaBusca;
	}

	public void setEscolhaBusca(String escolhaBusca) {
		this.escolhaBusca = escolhaBusca;
	}

	public String getTextoBusca() {
		return textoBusca;
	}

	public void setTextoBusca(String textoBusca) {
		this.textoBusca = textoBusca;
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
}