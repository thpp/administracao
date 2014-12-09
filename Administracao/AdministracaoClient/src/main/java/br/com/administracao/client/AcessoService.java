package br.com.administracao.client;

import javax.ejb.Remote;

import br.com.administracao.model.Acesso;

@Remote
public interface AcessoService {
	public static final String NAME = "global/AdministracaoService/AcessoServiceImpl!br.com.administracao.client.AcessoService";
	
	public void inserir(Acesso acesso);

}
