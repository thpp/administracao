package br.com.administracao.test;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import br.com.administracao.client.AcessoService;
import br.com.administracao.dao.impl.AcessoDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Acesso;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Funcoes;
import br.com.administracao.model.Permissoes;
import br.com.administracao.model.Tela;
import br.com.administracao.model.Usuario;

public class AcessoServiceImplTest {

	private final static EJBContainer ejbContainer = EJBContainer
			.createEJBContainer();

	@Test
	@Ignore
	public void inserir() throws NamingException, ServiceException {

		Usuario usuario = new Usuario();
		Tela tela = new Tela();
		Acesso acesso = new Acesso();
		List<Permissoes> listaPermissoes = new ArrayList<Permissoes>();

		Funcoes f1 = new Funcoes();
		Funcoes f2 = new Funcoes();
		Funcoes f3 = new Funcoes();

		Acoes ac1 = new Acoes();
		Acoes ac2 = new Acoes();
		Acoes ac3 = new Acoes();

		Permissoes p1 = new Permissoes();
		Permissoes p2 = new Permissoes();
		Permissoes p3 = new Permissoes();

		ac1.setNro(1L);
		ac2.setNro(2L);
		ac3.setNro(3L);

		usuario.setNro(1L);
		tela.setNro(3L);
		acesso.setTela(tela);
		acesso.setUsuario(usuario);

		f1.setTela(tela);
		f2.setTela(tela);
		f3.setTela(tela);

		f1.setAcoes(ac1);
		f2.setAcoes(ac2);
		f3.setAcoes(ac3);

		p1.setAcesso(acesso);
		p1.setFuncoes(f1);

		p2.setAcesso(acesso);
		p2.setFuncoes(f2);

		p3.setAcesso(acesso);
		p3.setFuncoes(f3);

		listaPermissoes.add(p1);
		listaPermissoes.add(p2);
		listaPermissoes.add(p3);

		acesso.setListaPermissoes(listaPermissoes);

		AcessoService service = (AcessoService) ejbContainer.getContext()
				.lookup("java:global/AdministracaoService/AcessoServiceImpl");
		service.inserir(acesso);

	}

	@Test
	@Ignore
	public void listar() throws NamingException, ServiceException {
		AcessoService service = (AcessoService) ejbContainer.getContext()
				.lookup("java:global/AdministracaoService/AcessoServiceImpl");

		Usuario usuario = new Usuario();
		usuario.setNro(2L);

		List<Acesso> lista = service.listar(usuario);

		for (Acesso acesso : lista) {
			System.out.println("Nome da Tela: " + acesso.getTela().getNome());
			System.out.println("Nome do Módulo: "
					+ acesso.getTela().getModulo().getNome());

			List<Permissoes> listaPermissoes = acesso.getListaPermissoes();

			System.out.println("Permissões dessa tela: ");

			for (Permissoes permissoes : listaPermissoes) {
				System.out.println("Nro da Ação: "
						+ permissoes.getFuncoes().getAcoes().getNro());
				System.out.println("Nome da Ação: "
						+ permissoes.getFuncoes().getAcoes().getNome());
			}
			System.out.println("=====================================");
		}
	}

	@Test
	@Ignore
	public void excluir() throws NamingException, ServiceException {
		try {
			AcessoDAOImpl dao = new AcessoDAOImpl();
			Tela tela = new Tela();
			Usuario usuario = new Usuario();

			tela.setNro(3L);
			usuario.setNro(2L);

			Acesso acesso = new Acesso();

			acesso.setTela(tela);
			acesso.setUsuario(usuario);
			dao.excluir(acesso);

			System.out.println("Excluído com sucesso");
		} catch (PSTException e) {
			System.out.println("ERRO.... " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	//@Ignore
	public void editar() throws NamingException, ServiceException {
		try {
			AcessoDAOImpl dao = new AcessoDAOImpl();
			Tela tela = new Tela();
			Usuario usuario = new Usuario();
			Acesso acesso = new Acesso();
			List<Permissoes> listaPermissoes = new ArrayList<Permissoes>();

			tela.setNro(3L);
			usuario.setNro(2L);

			Funcoes f1 = new Funcoes();
			Funcoes f2 = new Funcoes();
			Funcoes f3 = new Funcoes();

			Acoes ac1 = new Acoes();
			Acoes ac2 = new Acoes();
			Acoes ac3 = new Acoes();

			Permissoes p1 = new Permissoes();
			Permissoes p2 = new Permissoes();
			Permissoes p3 = new Permissoes();

			ac1.setNro(1L);
			ac2.setNro(2L);
			ac3.setNro(3L);

			f1.setTela(tela);
			f2.setTela(tela);
			f3.setTela(tela);

			f1.setAcoes(ac1);
			f2.setAcoes(ac2);
			f3.setAcoes(ac3);

			p1.setAcesso(acesso);
			p1.setFuncoes(f1);

			p2.setAcesso(acesso);
			p2.setFuncoes(f2);

			p3.setAcesso(acesso);
			p3.setFuncoes(f3);

			listaPermissoes.add(p1);
			listaPermissoes.add(p2);
			listaPermissoes.add(p3);

			acesso.setListaPermissoes(listaPermissoes);

			acesso.setTela(tela);
			acesso.setUsuario(usuario);
			dao.editar(acesso);

			System.out.println("Editado com sucesso");
		} catch (PSTException e) {
			System.out.println("ERRO.... " + e.getMessage());
			e.printStackTrace();
		}
	}
}
