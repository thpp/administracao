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
	
	private static Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());
	
	@Override
	public void inserir(Usuario usuario) {
		try {			
			new UsuarioDAOImpl().inserir(usuario);
			logger.info("Usuario inserido com sucesso");
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
	public List<Usuario> listar(int primeiro, int tamanho, String nome) {
		List<Usuario> lista = new ArrayList<Usuario>();
		
		try {
			lista = new UsuarioDAOImpl().listar(primeiro, tamanho, nome);
			logger.info("Lista de usuarios obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
				
		return lista;
	}

	@Override
	public void editar(Usuario usuario) {
		try {			
			new UsuarioDAOImpl().editar(usuario);
			logger.info("Usuario editado com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}		
	}

	@Override
	public void excluir(Long codigo) {
		try {			
			new UsuarioDAOImpl().excluir(codigo);
			logger.info("Usuario excluido com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
	}

}
