package br.com.administracao.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import br.com.administracao.client.ModuloService;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;


public class ModuloServiceImplTest {
	private final static EJBContainer ejbContainer = EJBContainer.createEJBContainer();
	
	
	@Test
	@Ignore
	public void inserir() throws NamingException, ServiceException{
		Projeto p = new Projeto();
		Modulo m = new Modulo();
		p.setNro(9L);
		m.setNome("Teste");
		m.setProjeto(p);
		ModuloService service = (ModuloService) ejbContainer.getContext().lookup("java:global/AdministracaoService/ModuloServiceImpl");
		service.inserir(m);
	}
	
	@Test
	@Ignore
	public void editar() throws NamingException, ServiceException{
		Projeto p = new Projeto();
		Modulo m = new Modulo();
		p.setNro(9L);
		m.setNro(4L);
		m.setNome("Teste editar");
		m.setProjeto(p);
		ModuloService service = (ModuloService) ejbContainer.getContext().lookup("java:global/AdministracaoService/ModuloServiceImpl");
		service.editar(m);
	}
	
}
