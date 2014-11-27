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
	
	//retorna true quando a pessoa foi incluida na tabela pessoa
	@Override
	public Boolean inserir(Usuario usuario) {
		Boolean pessoaJaGravada = null;
		try {			
			 pessoaJaGravada = new UsuarioDAOImpl().inserir(usuario);
			 logger.info("Usuario inserido com sucesso");
			return pessoaJaGravada;
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
	public List<Usuario> listar(int primeiro, int tamanho, String nome, String flgAtivo) {
		List<Usuario> lista = new ArrayList<Usuario>();
		
		try {
			lista = new UsuarioDAOImpl().listar(primeiro, tamanho, nome, flgAtivo);
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
