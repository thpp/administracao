package br.com.administracao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import br.com.administracao.client.TelaService;
import br.com.administracao.dao.impl.ProjetoDAOImpl;
import br.com.administracao.dao.impl.TelaDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;

@Stateless
public class TelaServiceImpl implements TelaService {
	
	private static Logger logger = Logger.getLogger(TelaServiceImpl.class.getName());
	
	@Override
	public void inserir(Tela tela) {
		try {			
			new TelaDAOImpl().inserir(tela);
			logger.info("Tela inserido com sucesso");
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
	public List<Tela> listar(int primeiro, int tamanho) {
		
		List<Tela> lista = new ArrayList<Tela>();
		try {
			lista = new TelaDAOImpl().listar(primeiro, tamanho);
			logger.info("Lista de telas obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
		
		return lista;
	}

	@Override
	public List<Tela> listar(Projeto projeto, Modulo modulo, String nome) {
		List<Tela> lista = new ArrayList<Tela>();
		try {
			lista = new TelaDAOImpl().listar(projeto, modulo, nome);
			logger.info("Lista de telas obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
		
		return lista;
	}

	@Override
	public void editar(Tela tela) {
		try {			
			new TelaDAOImpl().editar(tela);
			logger.info("Tela editado com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public void excluir(Long codigo) {
		try {			
			new TelaDAOImpl().excluir(codigo);
			logger.info("Tela excluido com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
	}

}
