package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Usuario;


public interface UsuarioDAO {
	
	//retorna true quando a pessoa foi incluida na tabela pessoa
	public Boolean inserir(Usuario usuario) throws PSTException;

	public void editar(Usuario usuario) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Usuario> listar(int primeiro, int tamanho, String nome, String flgAtivo) throws PSTException;	

	public int contar() throws PSTException;

}
