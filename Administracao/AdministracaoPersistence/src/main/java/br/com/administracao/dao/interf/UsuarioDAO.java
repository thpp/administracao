package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Usuario;


public interface UsuarioDAO {
	
	//retorna null se pessoa foi incluida no banco, caso contrario retorna o usuario cadastraso 
	public Usuario inserir(Usuario usuario) throws PSTException;

	public void editar(Usuario usuario) throws PSTException;

	public void excluir(Long codigo) throws PSTException;
	
	public List<Usuario> listar(int primeiro, int tamanho, String nome, String flgAtivo) throws PSTException;
	
	public List<Usuario> listar(String cpf) throws PSTException;

	public int contar() throws PSTException;

}
