package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.ProjetoDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Projeto;
import br.com.administracao.util.PSTUtil;


public class ProjetoDAOImpl implements ProjetoDAO {
	
	private static Logger logger = Logger.getLogger(ProjetoDAOImpl.class
			.getName());

	@Override
	public void inserir(Projeto projeto) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO s_projeto ");
		sql.append("(nome) ");
		sql.append("VALUES (?) ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, projeto.getNome());

			comando.executeUpdate();

			logger.info("Produto inserido com sucesso");
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar inserir um produto "+ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
		
	}

	@Override
	public void editar(Projeto projeto) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Projeto> listar(int primeiro, int tamanho) throws PSTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int contar() throws PSTException {
		// TODO Auto-generated method stub
		return 0;
	}


}
