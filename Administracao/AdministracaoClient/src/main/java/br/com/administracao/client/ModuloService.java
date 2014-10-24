package br.com.administracao.client;

import java.util.List;

import javax.ejb.Remote;
import br.com.administracao.model.Modulo;

@Remote
public interface ModuloService {		
	public static final String NAME = "global/AdministracaoService/ModuloServiceImpl!br.com.administracao.client.ModuloService";

	public void inserir(Modulo modulo);

	public Integer contar();

	public List<Modulo> listar(int primeiro, int tamanho);
	
	public List<Modulo> listar(String nome);

	public void editar(Modulo modulo);

	public void excluir(Long codigo);
	
}
