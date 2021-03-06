package br.com.administracao.client;

import java.util.List;

import javax.ejb.Remote;

import br.com.administracao.model.Usuario;

@Remote
public interface UsuarioService {
	public static final String NAME = "global/AdministracaoService/UsuarioServiceImpl!br.com.administracao.client.UsuarioService";

	// retorna null se a pessoa foi incluída no banco, caso contrário retorna o
	// usuário cadastrado
	public Usuario inserir(Usuario usuario);

	public Integer contar();

	public List<Usuario> listar(int primeiro, int tamanho, String nome,
			String flgAtivo);

	public void editar(Usuario usuario);

	public void excluir(Long codigo);

	public List<Usuario> listar(String cpf);

}
