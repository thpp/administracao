package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Modulo;


public interface ModuloDAO {
	
	public void inserir(Modulo modulo) throws PSTException;

	public void editar(Modulo modulo) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Modulo> listar(int primeiro, int tamanho) throws PSTException;
	
	public List<Modulo> listar(String nome, Long nroProjeto) throws PSTException;

	public int contar() throws PSTException;
	

}
