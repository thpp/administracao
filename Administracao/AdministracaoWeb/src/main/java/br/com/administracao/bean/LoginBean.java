package br.com.administracao.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.administracao.util.WebUtil;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String login;
	private String senha;
	protected HttpSession session;
		
	public String logar(){
		
		System.out.println(getLogin());
		System.out.println(getSenha());
		
		if(getLogin().equals("micromap") && getSenha().equals("Oramap82")){
			
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        session = request.getSession();
			
	        session.setAttribute("isLogado", "true");
			
			return "/pages/protected/index.xhtml?faces-redirect=true";
		}else{
			WebUtil.adicionarMensagemErro("Usuario e senha incorretos");
			return null;
		}
		
	}
	
	  public String logout() {

	        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        HttpSession session = request.getSession();
	        session.removeAttribute("isLogado");
	        
	        return "/pages/public/login.xhtml?faces-redirect=true";
	    }
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
