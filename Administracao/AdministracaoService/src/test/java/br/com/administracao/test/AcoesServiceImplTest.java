package br.com.administracao.test;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import br.com.administracao.client.AcoesService;
import br.com.administracao.model.Acoes;

public class AcoesServiceImplTest {
	private final static EJBContainer ejbContainer = EJBContainer.createEJBContainer();
	
	@Test
	public void listar() throws NamingException, ServiceException{
		List<Acoes> lista = new ArrayList<Acoes>();
		
		AcoesService service = (AcoesService) ejbContainer.getContext().lookup("java:global/AdministracaoService/AcoesServiceImpl");
		lista = service.listar();
		
		for (Acoes acoes : lista) {
			System.out.println("Ação: "+acoes.getNome());
		}
	}	
}
