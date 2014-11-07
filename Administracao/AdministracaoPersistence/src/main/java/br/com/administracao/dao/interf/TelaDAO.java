package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;

public interface TelaDAO {
	
	public void inserir(Tela tela) throws PSTException;

	public void editar(Tela tela) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Tela> listar(int primeiro, int tamanho) throws PSTException;
	
	public List<Tela> listar(Projeto projeto, Modulo modulo, String nome) throws PSTException;

	public int contar() throws PSTException;

}
