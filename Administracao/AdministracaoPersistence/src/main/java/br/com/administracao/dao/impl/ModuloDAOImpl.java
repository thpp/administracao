package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.ModuloDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Modulo;
import br.com.administracao.util.PSTUtil;

public class ModuloDAOImpl implements ModuloDAO {
	
	private static Logger logger = Logger.getLogger(ModuloDAOImpl.class.getName());

	@Override
	public void inserir(Modulo modulo) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO s_modulo ");
		sql.append("(NOME, PROJ_NRO ) ");
		sql.append("VALUES (UPPER(?), ?) ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, modulo.getNome());
			comando.setLong(2, modulo.getProjeto().getNro());

			comando.executeUpdate();

			logger.info("Modulo inserido com sucesso");
		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar inserir um modulo "+ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}		
	}

	@Override
	public void editar(Modulo modulo) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE s_modulo ");
		sql.append("SET nome = UPPER(?), PROJ_NRO = ?");
		sql.append("WHERE nro = ? ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, modulo.getNome());
			comando.setLong(2, modulo.getProjeto().getNro());
			comando.setLong(3, modulo.getNro());

			comando.executeUpdate();

			logger.info("Modulo editado com sucesso");
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar editar um modulo", ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
		
	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Modulo> listar(int primeiro, int tamanho) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.nro, p.nome, p.proj_nro ");
		sql.append("FROM s_modulo p ");
		//sql.append("ORDER BY p.descricao ");
		
		
		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Modulo> lista = new ArrayList<Modulo>();
		
		try {
			
		conexao = ConnectionFactory.getConnection();		
		comando = conexao.prepareStatement(sql.toString());		
		resultado = comando.executeQuery();

		while (resultado.next()) {
			Modulo modulo = new Modulo();
			modulo.setNro(resultado.getLong("nro"));
			modulo.setNome(resultado.getString("nome"));
			modulo.setProjeto(new ProjetoDAOImpl().buscar(resultado.getLong("PROJ_NRO")));

			lista.add(modulo);
		}
		
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de projetos", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
		
		return lista;
	}

	@Override
	public List<Modulo> listar(String nome) throws PSTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int contar() throws PSTException {
		// TODO Auto-generated method stub
		return 0;
	}	
}
