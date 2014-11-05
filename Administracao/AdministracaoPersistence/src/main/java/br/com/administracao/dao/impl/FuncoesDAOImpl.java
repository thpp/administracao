package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.FuncoesDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Funcoes;
import br.com.administracao.util.PSTUtil;

public class FuncoesDAOImpl implements FuncoesDAO {
	
	private static Logger logger = Logger.getLogger(FuncoesDAOImpl.class.getName());

	@Override
	public void inserir(Funcoes funcoes) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO S_FUNCOES ");
		sql.append("(ACAO_NRO, TELA_NRO) ");
		sql.append("VALUES (?, ?) ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setLong(1, funcoes.getAcoes().getNro());
			comando.setLong(2, funcoes.getTela().getNro());

			comando.executeUpdate();

			logger.info("Funcao inserido com sucesso");
		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar inserir uma funcao "+ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
		
	}

	@Override
	public void editar(Funcoes funcoes) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Funcoes> listar() throws PSTException {
		// TODO Auto-generated method stub
		return null;
	}

}
