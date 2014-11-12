package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Usuario;


public interface UsuarioDAO {
	
	public void inserir(Usuario usuario) throws PSTException;

	public void editar(Usuario usuario) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Usuario> listar(int primeiro, int tamanho, String nome) throws PSTException;	

	public int contar() throws PSTException;

}
