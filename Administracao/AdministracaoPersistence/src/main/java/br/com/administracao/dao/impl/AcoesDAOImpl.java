package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.AcoesDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Projeto;
import br.com.administracao.util.PSTUtil;

public class AcoesDAOImpl implements AcoesDAO{
	
	private static Logger logger = Logger.getLogger(AcoesDAOImpl.class.getName());

	@Override
	public List<Acoes> listar() throws PSTException {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.nro, a.nome ");
		sql.append("FROM s_acoes a ");
		//sql.append("ORDER BY p.descricao ");
		
		
		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Acoes> lista = new ArrayList<Acoes>();
		
		try {
		conexao = ConnectionFactory.getConnection();
		
		comando = conexao.prepareStatement(sql.toString());
		
		resultado = comando.executeQuery();

		while (resultado.next()) {
			Acoes acoes = new Acoes();
			acoes.setNro(resultado.getLong("nro"));
			acoes.setNome(resultado.getString("nome"));

			lista.add(acoes);
		}
		
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de Ações", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
		
		return lista;
	}


}
