package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Projeto;


public interface ProjetoDAO {
	
	public void inserir(Projeto projeto) throws PSTException;

	public void editar(Projeto projeto) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Projeto> listar(int primeiro, int tamanho) throws PSTException;
	
	public List<Projeto> listar(String nome) throws PSTException;

	public int contar() throws PSTException;
	

}
