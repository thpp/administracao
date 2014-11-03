package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Acoes;

public interface AcoesDAO {	
	
	public List<Acoes> listar() throws PSTException;

}
