package br.com.administracao.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import br.com.administracao.client.ProjetoService;
import br.com.administracao.dao.impl.ProjetoDAOImpl;
import br.com.administracao.execao.PSTException;
import br.com.administracao.execao.ServiceException;
import br.com.administracao.model.Projeto;


@Stateless
public class ProjetoServiceImpl implements ProjetoService {
	private static Logger logger = Logger.getLogger(ProjetoServiceImpl.class
			.getName());

	@Override
	public void inserir(Projeto projeto) {
		try {
			new ProjetoDAOImpl().inserir(projeto);
			logger.info("Produto inserido com sucesso");
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
	public List<Projeto> listar(int primeiro, int tamanho) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editar(Projeto projeto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Long codigo) {
		// TODO Auto-generated method stub
		
	}

}
