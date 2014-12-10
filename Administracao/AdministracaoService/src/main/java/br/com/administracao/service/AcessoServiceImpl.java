package br.com.administracao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import br.com.administracao.client.AcessoService;
import br.com.administracao.dao.impl.AcessoDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Acesso;
import br.com.administracao.model.Usuario;

@Stateless
public class AcessoServiceImpl implements AcessoService {

	private static Logger logger = Logger.getLogger(AcessoServiceImpl.class
			.getName());

	@Override
	public void inserir(Acesso acesso) {
		try {
			new AcessoDAOImpl().inserir(acesso);
			logger.info("Acesso inserido com sucesso");
		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}

	}

	@Override
	public List<Acesso> listar(Usuario usuario) {

		List<Acesso> lista = new ArrayList<Acesso>();
		try {
			lista = new AcessoDAOImpl().listar(usuario);
			logger.info("Lista de acessos obtida com sucesso");

		} catch (PSTException ex) {
			throw new ServiceException(ex);
		}
		return lista;
	}

}
