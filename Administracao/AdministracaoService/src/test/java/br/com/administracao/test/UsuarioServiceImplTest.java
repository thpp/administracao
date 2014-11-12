package br.com.administracao.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import br.com.administracao.client.UsuarioService;
import br.com.administracao.model.Usuario;



public class UsuarioServiceImplTest {
	
	private final static EJBContainer ejbContainer = EJBContainer.createEJBContainer();
	
	@Test
	@Ignore
	public void inserir() throws NamingException, ServiceException {

		Usuario usuario = new Usuario();
		
		usuario.setNome("Thiago Henrique");
		usuario.setCpf("36995369807");
		usuario.setCargo("Programador");
		usuario.setUsuario("thiagohpp");
		usuario.setSenha("123mudar");

		UsuarioService service = (UsuarioService) ejbContainer.getContext().lookup("java:global/AdministracaoService/UsuarioServiceImpl");
		service.inserir(usuario);
	}
	
	
	
}
