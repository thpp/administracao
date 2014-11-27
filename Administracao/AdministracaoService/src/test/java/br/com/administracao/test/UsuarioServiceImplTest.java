package br.com.administracao.test;

import java.util.List;

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
	@Ignore
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
		Boolean retorno = service.inserir(usuario);
		
		if(retorno){
			System.out.println("Não!!!....estava na tabela pessoa");
		}else{
			System.out.println("Já estava na tabela pessoa");
		}
	}
	
	@Test
	@Ignore
	public void listar() throws NamingException, ServiceException {		

		UsuarioService service = (UsuarioService) ejbContainer.getContext().lookup("java:global/AdministracaoService/UsuarioServiceImpl");
		
		List<Usuario> lista = service.listar(0, 0, null, "S");
		
		for (Usuario usuario : lista) {
			System.out.println("Nome: "+usuario.getPessoa().getNome());
			System.out.println("Usuario: "+usuario.getUsuario());
			System.out.println("Senha: "+usuario.getSenha());
		}		
	}	
	
	@Test
	@Ignore
	public void excluir() throws NamingException, ServiceException {		

		UsuarioService service = (UsuarioService) ejbContainer.getContext().lookup("java:global/AdministracaoService/UsuarioServiceImpl");
		service.excluir(1L);		
	}
	
	@Test
	public void editar() throws NamingException, ServiceException {

		Usuario usuario = new Usuario();
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNro(148337L);
		pessoa.setNome("Thiago Henrique Pereira Paiva");
		pessoa.setFlgPessoa("M");
		pessoa.setCpf("36995369807");
		
		usuario.setNro(3L);
		usuario.setUsuario("vaiiiEditarrr");
		usuario.setSenha("123mudar");
		usuario.setFlgAtivo(Boolean.TRUE);
		usuario.setFlgAdm(Boolean.TRUE);
		usuario.setFlgProfissional(Boolean.TRUE);
		usuario.setObs("Teste.....");
		usuario.setDataBaixa(null);
		
		usuario.setPessoa(pessoa);

		UsuarioService service = (UsuarioService) ejbContainer.getContext().lookup("java:global/AdministracaoService/UsuarioServiceImpl");
		service.editar(usuario);		
		
	}
}
