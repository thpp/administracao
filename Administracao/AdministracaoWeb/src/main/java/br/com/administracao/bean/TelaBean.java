package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import br.com.administracao.model.Acoes;

@ManagedBean(name = "MBTela")
@ViewScoped
public class TelaBean implements Serializable {

	private static final long serialVersionUID = -2062657756767836389L;

	private DualListModel<Acoes> acoes;

	@PostConstruct
	public void init() {

		// Ações
		List<Acoes> themesSource = new ArrayList<Acoes>(); // Vai receber a
															// listagem de Ações
		List<Acoes> themesTarget = new ArrayList<Acoes>();

		acoes = new DualListModel<Acoes>(themesSource, themesTarget);

	}

	public DualListModel<Acoes> getAcoes() {
		return acoes;
	}

	public void setAcoes(DualListModel<Acoes> acoes) {
		this.acoes = acoes;
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