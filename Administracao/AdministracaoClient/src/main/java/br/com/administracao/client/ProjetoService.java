package br.com.administracao.client;

import java.util.List;

import javax.ejb.Remote;

import br.com.administracao.model.Projeto;

@Remote
public interface ProjetoService {
	public static final String NAME = "global/AdministracaoService/ProjetoServiceImpl!br.com.administracao.client.ProjetoService";

	public void inserir(Projeto projeto);

	public Integer contar();

	public List<Projeto> listar(int primeiro, int tamanho);
	
	public List<Projeto> listar(String nome);

	public void editar(Projeto projeto);

	public void excluir(Long codigo);

}
