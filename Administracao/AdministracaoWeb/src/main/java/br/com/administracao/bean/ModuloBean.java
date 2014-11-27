package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import br.com.administracao.client.ModuloService;
import br.com.administracao.client.ProjetoService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.util.WebUtil;

/**
 * @author Leticia Alves
 */
@ManagedBean(name = "MBModulo")
@ViewScoped
public class ModuloBean implements Serializable {

	private static final long serialVersionUID = -1750627356416442575L;

	private Modulo modulo;
	private Integer caracteresMinimos = 3;
	private Long nroProjetoBusca = 0L;
	private String textoBusca = "";
	private List<Projeto> listaProjetos = new ArrayList<Projeto>();
	private List<Projeto> listaProjetosBusca = new ArrayList<Projeto>();
	private List<Modulo> listaModulos = new ArrayList<Modulo>();

	@PostConstruct
	public void buscarLista() {
		try {
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			listaModulos = service.listar(0, 0);

			ProjetoService serviceProjeto = (ProjetoService) WebUtil
					.getNamedObject(ProjetoService.NAME);
			listaProjetosBusca = serviceProjeto.listar(0, 0);

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void limparCamposBusca() {
		textoBusca = "";
		nroProjetoBusca = 0L;
		buscarLista();
	}

	public void pesquisar() {
		if (!"".equals(textoBusca) && nroProjetoBusca != 0) {
			// Pesquisar pelo texto digitado e número do projeto
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			listaModulos = service.listar(textoBusca, nroProjetoBusca);

		} else if (!"".equals(textoBusca)) {
			// Pesquisar pelo texto
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			listaModulos = service.listar(textoBusca, null);

		} else if (nroProjetoBusca != 0) {
			// Pesquisar pelo número do projeto
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			listaModulos = service.listar(null, nroProjetoBusca);

		} else {
			// Pesquisar todos os módulos
			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			listaModulos = service.listar(0, 0);
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

			if (modulo.getNome().length() > caracteresMinimos) {
				if (modulo.getNro() == null) {
					ModuloService service = (ModuloService) WebUtil
							.getNamedObject(ModuloService.NAME);
					service.inserir(modulo);
					WebUtil.adicionarMensagemSucesso("Módulo salvo com sucesso");

					// Fecha o diálogo
					org.primefaces.context.RequestContext.getCurrentInstance()
							.execute("PF('dlgNovo').hide();");

				} else {
					ModuloService service = (ModuloService) WebUtil
							.getNamedObject(ModuloService.NAME);
					service.editar(modulo);
					WebUtil.adicionarMensagemSucesso("Módulo editado com sucesso");

					// Fecha o diálogo
					org.primefaces.context.RequestContext.getCurrentInstance()
							.execute("PF('dlgEdicao').hide();");

				}

				// Atualiza a mensagem de sucesso
				RequestContext.getCurrentInstance().update("form1:msgs");
			} else {
				WebUtil.adicionarMensagemAviso("Módulo deve ter o nome maior que 3 caracteres");
				RequestContext.getCurrentInstance().update("msgValorInvalido");
			}

			buscarLista();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void excluir() {
		try {

			ModuloService service = (ModuloService) WebUtil
					.getNamedObject(ModuloService.NAME);
			service.excluir(modulo.getNro());
			WebUtil.adicionarMensagemSucesso("Módulo excluído com sucesso");

			buscarLista();

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Integer getCaracteresMinimos() {
		return caracteresMinimos;
	}

	public void setCaracteresMinimos(Integer caracteresMinimos) {
		this.caracteresMinimos = caracteresMinimos;
	}

	public Long getNroProjetoBusca() {
		return nroProjetoBusca;
	}

	public void setNroProjetoBusca(Long nroProjetoBusca) {
		this.nroProjetoBusca = nroProjetoBusca;
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

	public List<Projeto> getListaProjetosBusca() {
		return listaProjetosBusca;
	}

	public void setListaProjetosBusca(List<Projeto> listaProjetosBusca) {
		this.listaProjetosBusca = listaProjetosBusca;
	}

	public List<Modulo> getListaModulos() {
		return listaModulos;
	}

	public void setListaModulos(List<Modulo> listaModulos) {
		this.listaModulos = listaModulos;
	}

}
