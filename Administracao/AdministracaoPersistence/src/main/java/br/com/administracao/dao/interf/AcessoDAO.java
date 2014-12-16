package br.com.administracao.dao.interf;

import java.util.List;

import br.com.administracao.execao.PSTException;
import br.com.administracao.model.Acesso;
import br.com.administracao.model.Tela;
import br.com.administracao.model.Usuario;

public interface AcessoDAO {

	public void inserir(Acesso acesso) throws PSTException;

	public void editar(Acesso acesso) throws PSTException;

	public void excluir(Acesso acesso) throws PSTException;

	public List<Acesso> listar(Usuario usuario) throws PSTException;

	public List<Acesso> listar(Tela tela) throws PSTException;

}
