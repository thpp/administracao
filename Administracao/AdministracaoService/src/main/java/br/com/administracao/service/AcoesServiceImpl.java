package br.com.administracao.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import br.com.administracao.client.AcoesService;
import br.com.administracao.dao.impl.AcoesDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Acoes;

@Stateless
public class AcoesServiceImpl implements AcoesService {

	private static Logger logger = Logger.getLogger(AcoesServiceImpl.class
			.getName());

	@Override
	public List<Acoes> listar() {

		List<Acoes> lista = null;

		try {
			lista = new AcoesDAOImpl().listar();
			logger.info("Lista de Ações obtida com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

}
