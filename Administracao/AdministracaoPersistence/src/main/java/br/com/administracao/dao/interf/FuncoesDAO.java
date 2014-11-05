package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Funcoes;

public interface FuncoesDAO {
	
	public void inserir(Funcoes funcoes) throws PSTException;

	public void editar(Funcoes funcoes) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Funcoes> listar() throws PSTException;

}
