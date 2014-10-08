package br.com.administracao.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.administracao.client.ProjetoService;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Projeto;
import br.com.administracao.util.WebUtil;


@ManagedBean
@ViewScoped
public class ProjetoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Projeto projeto = new Projeto();
	
		
	public Projeto getProjeto() {
		return projeto;
	}


	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}


	public void salvar() {
		try {
			ProjetoService service = (ProjetoService) WebUtil.getNamedObject(ProjetoService.NAME);
			service.inserir(projeto);			
			WebUtil.adicionarMensagemSucesso("Produto salvo com sucesso");
		} catch (ServiceException ex) {
			WebUtil.adicionarMensagemErro(ex.getMessage());
		}
	}
	
	

}
