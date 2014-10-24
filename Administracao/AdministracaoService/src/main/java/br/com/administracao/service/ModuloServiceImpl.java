package br.com.administracao.service;

import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.client.ModuloService;
import br.com.administracao.dao.impl.ModuloDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Modulo;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Modulo> listar(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editar(Modulo modulo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Long codigo) {
		// TODO Auto-generated method stub
		
	}

}
