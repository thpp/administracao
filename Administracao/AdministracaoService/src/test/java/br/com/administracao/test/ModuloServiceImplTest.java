package br.com.administracao.test;

import java.util.ArrayList;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

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
	
	@Test
	@Ignore
	public void lista1() throws NamingException, ServiceException{
		
		java.util.List<Modulo> lista = new ArrayList<Modulo>();
		ModuloService service = (ModuloService) ejbContainer.getContext().lookup("java:global/AdministracaoService/ModuloServiceImpl");
		lista = service.listar(0, 0);
		
		for (Modulo modulo : lista) {
			System.out.println("Modulo Nome: "+modulo.getNome());
			System.out.println("Projeto Nome: "+modulo.getProjeto().getNome());
		}
	}
	
	@Test
	public void lista2() throws NamingException, ServiceException{
		
		java.util.List<Modulo> lista = new ArrayList<Modulo>();
		ModuloService service = (ModuloService) ejbContainer.getContext().lookup("java:global/AdministracaoService/ModuloServiceImpl");
		lista = service.listar("usu", 9L);
		
		for (Modulo modulo : lista) {
			System.out.println("Modulo Nome: "+modulo.getNome());
			System.out.println("Projeto Nome: "+modulo.getProjeto().getNome());
		}
	}
	
	@Test
	@Ignore
	public void excluir() throws NamingException, ServiceException{
		
		Modulo m = new Modulo();
		m.setNro(9L);
		ModuloService service = (ModuloService) ejbContainer.getContext().lookup("java:global/AdministracaoService/ModuloServiceImpl");
		service.excluir(m.getNro());
			
	}
	
}
