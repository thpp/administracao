package br.com.administracao.client;

import java.util.List;

import javax.ejb.Remote;

import br.com.administracao.model.Acoes;

@Remote
public interface AcoesService {
	public static final String NAME = "global/AdministracaoService/AcoesServiceImpl!br.com.administracao.client.AcoesService";
	
	public List<Acoes> listar();
	
	
}
