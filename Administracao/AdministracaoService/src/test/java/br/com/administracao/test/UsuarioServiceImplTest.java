package br.com.administracao.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import br.com.administracao.client.UsuarioService;
import br.com.administracao.model.Pessoa;
import br.com.administracao.model.Usuario;



public class UsuarioServiceImplTest {
	
	private final static EJBContainer ejbContainer = EJBContainer.createEJBContainer();
	
	@Test
	public void inserir() throws NamingException, ServiceException {

		Usuario usuario = new Usuario();
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome("Thiago Henrique");
		pessoa.setFlgPessoa("M");
		pessoa.setCpf("36995369807");
		
		usuario.setUsuario("formen");
		usuario.setSenha("123mudar");
		usuario.setFlgAtivo(Boolean.TRUE);
		usuario.setFlgAdm(Boolean.TRUE);
		usuario.setFlgProfissional(Boolean.TRUE);
		usuario.setObs("Teste.....");
		usuario.setPessoa(pessoa);

		UsuarioService service = (UsuarioService) ejbContainer.getContext().lookup("java:global/AdministracaoService/UsuarioServiceImpl");
		service.inserir(usuario);
	}
	
	
	
}
