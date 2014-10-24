package br.com.administracao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.administracao.client.ProjetoService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Projeto;
import br.com.administracao.util.WebUtil;

@ManagedBean(name = "MBProjeto")
@ViewScoped
public class ProjetoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Projeto projeto = new Projeto();
	private List<Projeto> listaProjetos = new ArrayList<Projeto>();
	private Integer caracteresMinimos = 3;
	
	public ProjetoBean(){
		buscaLista();
	}

	public void buscaLista() {
		try {
			ProjetoService service = (ProjetoService) WebUtil.getNamedObject(ProjetoService.NAME);
			listaProjetos = service.listar(0, 0);
		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void salvar() {
		try {

			if (projeto.getNome().length() > caracteresMinimos) {
				//projeto.setNome(projeto.getNome().toUpperCase());
				if (projeto.getNro() == null) {
					ProjetoService service = (ProjetoService) WebUtil.getNamedObject(ProjetoService.NAME);
					service.inserir(projeto);
					WebUtil.adicionarMensagemSucesso("Produto salvo com sucesso");
				} else {
					ProjetoService service = (ProjetoService) WebUtil.getNamedObject(ProjetoService.NAME);
					service.editar(projeto);
					WebUtil.adicionarMensagemSucesso("Produto salvo com sucesso");
				}
				buscaLista();
				novo();
			} else {
				WebUtil.adicionarMensagemAviso("Projeto deve ter o nome maior que 3 caracteres");
			}

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void novo() {
		projeto = new Projeto();
	}

	public void excluir() {
		try {
			if (projeto.getNro() != null) {
				ProjetoService service = (ProjetoService) WebUtil.getNamedObject(ProjetoService.NAME);
				service.excluir(projeto.getNro());
				WebUtil.adicionarMensagemSucesso("Projeto excluido com sucesso");
			} else {
				WebUtil.adicionarMensagemAviso("Selecione um projeto antes de excluir");
			}
			buscaLista();
			novo();
		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public void buscarPorNome() {
		try {

			if (!projeto.getNome().equals("")) {				
				ProjetoService service = (ProjetoService) WebUtil.getNamedObject(ProjetoService.NAME);
				listaProjetos = service.listar(projeto.getNome());
				System.out.println("Tamanho lista retorno: "+listaProjetos.size());
				for (Projeto projeto : listaProjetos) {
					System.out.println("Progeto: "+projeto.getNome());
				}				
			} else {
				buscaLista();							
			}

		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {		
		this.projeto = projeto;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}
}
