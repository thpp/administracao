package br.com.administracao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import br.com.administracao.client.UsuarioService;
import br.com.administracao.dao.impl.UsuarioDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Usuario;

@Stateless
public class UsuarioServiceImpl implements UsuarioService {

	private static Logger logger = Logger.getLogger(UsuarioServiceImpl.class
			.getName());

	// retorna true quando a pessoa foi incluída na tabela pessoa
	@Override
	public Usuario inserir(Usuario usuario) {
		Usuario usuarioRetorno = null;
		try {
			usuarioRetorno = new UsuarioDAOImpl().inserir(usuario);
			logger.info("Usuário inserido com sucesso");
			return usuarioRetorno;
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public Integer contar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Usuario> listar(int primeiro, int tamanho, String nome,
			String flgAtivo) {
		List<Usuario> lista = new ArrayList<Usuario>();

		try {
			lista = new UsuarioDAOImpl().listar(primeiro, tamanho, nome,
					flgAtivo);
			logger.info("Lista de usuários obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

	@Override
	public List<Usuario> listar(String cpf) {
		List<Usuario> lista = new ArrayList<Usuario>();

		try {
			lista = new UsuarioDAOImpl().listar(cpf);
			logger.info("Lista de usuários obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

	@Override
	public void editar(Usuario usuario) {
		try {
			new UsuarioDAOImpl().editar(usuario);
			logger.info("Usuário editado com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public void excluir(Long codigo) {
		try {
			new UsuarioDAOImpl().excluir(codigo);
			logger.info("Usuário excluído com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
	}

}
