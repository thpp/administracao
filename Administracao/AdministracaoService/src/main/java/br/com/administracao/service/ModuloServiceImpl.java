package br.com.administracao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import br.com.administracao.client.ModuloService;
import br.com.administracao.dao.impl.ModuloDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Modulo;

@Stateless
public class ModuloServiceImpl implements ModuloService {
	
	private static Logger logger = Logger.getLogger(ModuloServiceImpl.class.getName());

	@Override
	public void inserir(Modulo modulo) {
		try {			
			new ModuloDAOImpl().inserir(modulo);
			logger.info("Modulo inserido com sucesso");
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
	public List<Modulo> listar(int primeiro, int tamanho) {
		
		List<Modulo> lista = new ArrayList<Modulo>();
		try {
			lista = new ModuloDAOImpl().listar(primeiro, tamanho);
			logger.info("Lista de modulos obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
		return lista;
	}

	@Override
	public List<Modulo> listar(String nome, Long nroProjeto) {
		
		List<Modulo> lista = new ArrayList<Modulo>();
		try {
			lista = new ModuloDAOImpl().listar(nome, nroProjeto);
			logger.info("Lista de modulos obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
		return lista;
	}

	@Override
	public void editar(Modulo modulo) {
		try {			
			new ModuloDAOImpl().editar(modulo);
			logger.info("Modulo editado com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}	
		
	}

	@Override
	public void excluir(Long codigo) {
		try {
			new ModuloDAOImpl().excluir(codigo);
			logger.info("Modulo excluido com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
		
	}

}
