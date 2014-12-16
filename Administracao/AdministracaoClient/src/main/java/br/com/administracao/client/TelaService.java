package br.com.administracao.client;

import java.util.List;

import javax.ejb.Remote;

import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;

@Remote
public interface TelaService {
	public static final String NAME = "global/AdministracaoService/TelaServiceImpl!br.com.administracao.client.TelaService";

	public void inserir(Tela tela);

	public Integer contar();

	public List<Tela> listar(int primeiro, int tamanho);

	public List<Tela> listar(Projeto projeto, Modulo modulo, String nome);

	public void editar(Tela tela);

	public void excluir(Long codigo);

}
