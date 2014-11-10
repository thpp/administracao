package br.com.administracao.test;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import br.com.administracao.client.TelaService;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Funcoes;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;

public class TelaServiceImplTest {
	private final static EJBContainer ejbContainer = EJBContainer
			.createEJBContainer();

	@Test
	@Ignore
	public void inserir() throws NamingException, ServiceException {

		Tela t = new Tela();
		Modulo m = new Modulo();
		Projeto p = new Projeto();

		Acoes ac1 = new Acoes();
		ac1.setNro(1L);

		Acoes ac2 = new Acoes();
		ac2.setNro(2L);

		Funcoes f1 = new Funcoes();
		Funcoes f2 = new Funcoes();

		f1.setAcoes(ac1);
		f2.setAcoes(ac2);

		List<Funcoes> lista = new ArrayList<Funcoes>();

		lista.add(f1);
		lista.add(f2);

		p.setNro(15L);
		m.setNro(9L);
		m.setProjeto(p);

		t.setNome("Teste ID");
		t.setModulo(m);
		t.setListaFuncoes(lista);

		TelaService service = (TelaService) ejbContainer.getContext().lookup("java:global/AdministracaoService/TelaServiceImpl");
		service.inserir(t);
	}
	
	@Test
	public void editar() throws NamingException, ServiceException {

		Tela t = new Tela();
		Modulo m = new Modulo();
		//Projeto p = new Projeto();

		Acoes ac1 = new Acoes();
		ac1.setNro(3L);

		Acoes ac2 = new Acoes();
		ac2.setNro(4L);

		Funcoes f1 = new Funcoes();
		Funcoes f2 = new Funcoes();
				
		f1.setAcoes(ac1);
		f2.setAcoes(ac2);

		List<Funcoes> lista = new ArrayList<Funcoes>();

		lista.add(f1);
		lista.add(f2);

		//p.setNro(15L);
		m.setNro(10L);
		//m.setProjeto(p);
		
		t.setNro(10L);
		t.setNome("Alteração!!..");
		t.setModulo(m);
		t.setListaFuncoes(lista);

		TelaService service = (TelaService) ejbContainer.getContext().lookup("java:global/AdministracaoService/TelaServiceImpl");
		service.editar(t);

	}
	

	@Test
	@Ignore
	public void lista() throws NamingException, ServiceException {

		TelaService service = (TelaService) ejbContainer.getContext().lookup("java:global/AdministracaoService/TelaServiceImpl");
		
		Projeto projeto = new Projeto();
		Modulo modulo = new Modulo();
		
		projeto.setNro(11L);
		modulo.setNro(8L);
		
		
		List<Tela> lista = service.listar(projeto, modulo, null);			
		
		for (Tela tela : lista) {
			System.out.println("Tela nro: " + tela.getNro());
			System.out.println("Tela nome: " + tela.getNome());
			System.out.println("Modelo: " + tela.getModulo().getNome());
			System.out.println("Projeto: "+ tela.getModulo().getProjeto().getNome());
			System.out.println("Funções:");
			
			for(Funcoes f : tela.getListaFuncoes()){
				System.out.println(f.getAcoes().getNome());
			}
			
			System.out.println(".........................");
			System.out.println("");
			
		}
	}
}
