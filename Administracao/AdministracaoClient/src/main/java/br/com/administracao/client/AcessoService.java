package br.com.administracao.client;

import java.util.List;

import javax.ejb.Remote;

import br.com.administracao.model.Acesso;
import br.com.administracao.model.Tela;
import br.com.administracao.model.Usuario;

@Remote
public interface AcessoService {
	public static final String NAME = "global/AdministracaoService/AcessoServiceImpl!br.com.administracao.client.AcessoService";

	public void inserir(Acesso acesso);

	public List<Acesso> listar(Usuario usuario);

	public List<Acesso> listar(Tela tela);

	public void editar(Acesso acesso);

	public void excluir(Acesso acesso);
}
