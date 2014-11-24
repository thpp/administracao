package br.com.administracao.util;

import java.util.Properties;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class WebUtil {
	private static Logger logger = Logger.getLogger(WebUtil.class
			.getName());
	
	public static void adicionarMensagemErro(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
	}
	
	public static void adicionarMensagemSucesso(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
	}
	
	public static void adicionarMensagemAviso(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, null));
	}
	
	//metodo responsavel por encontrar o servidor onde a regra de negocio esta disponivel em serviços	
	public static Object getNamedObject(String name){
		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", "org.apache.openejb.client.RemoteInitialContextFactory");
		properties.put("java.naming.provider.url", "http://localhost:8088/tomee/ejb");
		//properties.put("java.naming.provider.url", "http://131.0.86.10:8084/tomee/ejb");
		
		Object namedObject = null;
		try {
			InitialContext context = new InitialContext(properties);
			namedObject = context.lookup(name);
			logger.info("Conectado com sucesso ao servidor de aplicação");
		} catch (NamingException ex) {
			logger.warning("Ocorreu um erro ao tentar conectar com o servidor de aplicação");
		}
		return namedObject;
	}
}