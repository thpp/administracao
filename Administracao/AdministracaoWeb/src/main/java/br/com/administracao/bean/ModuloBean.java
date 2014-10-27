package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.administracao.client.ModuloService;
import br.com.administracao.client.ProjetoService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.util.WebUtil;

@ManagedBean(name = "MBModulo")
@ViewScoped
public class ModuloBean implements Serializable {

	private static final long serialVersionUID = -1750627356416442575L;

	private Modulo modulo;
	private List<Modulo> modulos = new ArrayList<Modulo>();
	private Integer caracteresMinimos = 3;
	private String escolhaBusca;
	private String textoBusca = "";
	private List<Projeto> listaProjetos = new ArrayList<Projeto>();

	@PostConstruct
	public void buscar() {

	}

	public void buscarPorNome() {
		if ("modulo".equals(escolhaBusca)) {
			textoBusca = "";
			System.out.println("Busca Por Módulo");
		} else if ("projeto".equals(escolhaBusca)) {
			textoBusca = "";
			System.out.println("Busca Por Projeto");
		} else {
			WebUtil.adicionarMensagemAviso("Selecione um filtro para busca!");
		}
	}

	public void novo() {
		modulo = new Modulo();
		buscarProjetos();
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

	public void salvar() {
		try {
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			service.inserir(modulo);
			WebUtil.adicionarMensagemSucesso("Módulo salvo com sucesso");

			buscar();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void editar() {

	}

	public void excluir() {

	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public Integer getCaracteresMinimos() {
		return caracteresMinimos;
	}

	public void setCaracteresMinimos(Integer caracteresMinimos) {
		this.caracteresMinimos = caracteresMinimos;
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

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

}
