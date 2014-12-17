package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		sql.append("VALUES (UPPER(?)) ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, projeto.getNome());

			comando.executeUpdate();

			logger.info("Projeto inserido com sucesso");
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar inserir o projeto "
							+ ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

	}

	@Override
	public void editar(Projeto projeto) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE s_projeto ");
		sql.append("SET nome = UPPER(?)");
		sql.append("WHERE nro = ? ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, projeto.getNome());
			comando.setLong(2, projeto.getNro());

			comando.executeUpdate();

			logger.info("Projeto editado com sucesso");
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar editar o projeto", ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM s_projeto ");
		sql.append("WHERE nro = ? ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setLong(1, codigo);

			comando.executeUpdate();

			logger.info("Projeto excluído com sucesso");
		} catch (SQLException ex) {

			if (ex.getMessage().contains("ORA-02292")) {
				throw new PSTException(
						"Este projeto possui registros filhos e não pode ser excluído");
			} else {
				throw new PSTException(
						"Ocorreu um erro ao tentar excluir o projeto", ex);
			}

		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

	}

	@Override
	public List<Projeto> listar(int primeiro, int tamanho) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.nro, p.nome ");
		sql.append("FROM s_projeto p ");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Projeto> lista = new ArrayList<Projeto>();

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());

			resultado = comando.executeQuery();

			while (resultado.next()) {
				Projeto produto = new Projeto();
				produto.setNro(resultado.getLong("nro"));
				produto.setNome(resultado.getString("nome"));

				lista.add(produto);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de projetos",
					ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return lista;
	}

	@Override
	public List<Projeto> listar(String nome) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.nro, p.nome ");
		sql.append("FROM s_projeto p ");
		sql.append("WHERE p.nome LIKE UPPER(?)");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Projeto> lista = new ArrayList<Projeto>();

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());

			comando.setString(1, "%" + nome + "%");

			resultado = comando.executeQuery();

			while (resultado.next()) {
				Projeto produto = new Projeto();
				produto.setNro(resultado.getLong("nro"));
				produto.setNome(resultado.getString("nome"));

				lista.add(produto);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de projetos",
					ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return lista;
	}

	@Override
	public int contar() throws PSTException {
		// TODO Auto-generated method stub
		return 0;
	}

}
