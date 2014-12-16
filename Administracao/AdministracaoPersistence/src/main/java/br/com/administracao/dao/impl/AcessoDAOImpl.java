package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.AcessoDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Acesso;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Funcoes;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Permissoes;
import br.com.administracao.model.Pessoa;
import br.com.administracao.model.Tela;
import br.com.administracao.model.Usuario;
import br.com.administracao.util.PSTUtil;

public class AcessoDAOImpl implements AcessoDAO {

	private static Logger logger = Logger.getLogger(AcessoDAOImpl.class
			.getName());

	@Override
	public void inserir(Acesso acesso) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO S_ACESSO ");
		sql.append("(TELA_NRO, USU_NRO) ");
		sql.append("VALUES (?, ?) ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);
			comando = conexao.prepareStatement(sql.toString());
			comando.setLong(1, acesso.getTela().getNro());
			comando.setLong(2, acesso.getUsuario().getNro());

			comando.executeUpdate();

			incluirPermissoes(acesso.getListaPermissoes(), conexao);

			conexao.commit();

			logger.info("Acesso inserido com sucesso");

		} catch (SQLException ex) {

			if (ex.getMessage().contains("ORA-00001")) {
				throw new PSTException(
						"O acesso a essa tela já foi atribuído a esse usuário"
								+ ex.getMessage(), ex);
			} else {
				throw new PSTException(
						"Ocorreu um erro ao tentar inserir um acesso "
								+ ex.getMessage(), ex);
			}

		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
	}

	@Override
	public void editar(Acesso acesso) throws PSTException {

		StringBuilder sqlDeletaPermissao = new StringBuilder();
		sqlDeletaPermissao.append("DELETE FROM s_permissoes ");
		sqlDeletaPermissao.append("WHERE aces_tela_nro = ? ");
		sqlDeletaPermissao.append("AND aces_usu_nro = ? ");
		sqlDeletaPermissao.append("AND funcao_tela_nro = ? ");

		StringBuilder sqlInserePermissao = new StringBuilder();
		sqlInserePermissao.append(" INSERT INTO s_permissoes ");
		sqlInserePermissao
				.append("(aces_tela_nro, aces_usu_nro, funcao_acao_nro, funcao_tela_nro) ");
		sqlInserePermissao.append("VALUES (?, ?, ?, ?) ");

		Connection conexao = null;
		PreparedStatement comandoDeletaPermissao = null;
		PreparedStatement comandoInserePermissao = null;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);

			comandoDeletaPermissao = conexao
					.prepareStatement(sqlDeletaPermissao.toString());
			comandoDeletaPermissao.setLong(1, acesso.getTela().getNro());
			comandoDeletaPermissao.setLong(2, acesso.getUsuario().getNro());
			comandoDeletaPermissao.setLong(3, acesso.getTela().getNro());
			comandoDeletaPermissao.executeUpdate();

			comandoInserePermissao = conexao
					.prepareStatement(sqlInserePermissao.toString());

			for (Permissoes permissoes : acesso.getListaPermissoes()) {

				comandoInserePermissao.setLong(1, permissoes.getAcesso()
						.getTela().getNro());
				comandoInserePermissao.setLong(2, permissoes.getAcesso()
						.getUsuario().getNro());
				comandoInserePermissao.setLong(3, permissoes.getFuncoes()
						.getAcoes().getNro());
				comandoInserePermissao.setLong(4, permissoes.getFuncoes()
						.getTela().getNro());
				comandoInserePermissao.executeUpdate();
			}

			conexao.commit();

			logger.info("Acesso editado com sucesso");

		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException(
						"Ocorreu um erro ao tentar editar o Acesso", ex);
			} finally {
				PSTUtil.fechar(comandoDeletaPermissao);
				PSTUtil.fechar(comandoInserePermissao);
				PSTUtil.fechar(conexao);
			}
		}
	}

	@Override
	public void excluir(Acesso acesso) throws PSTException {
		StringBuilder sqlDeletaPermissao = new StringBuilder();
		sqlDeletaPermissao.append("DELETE FROM s_permissoes ");
		sqlDeletaPermissao.append("WHERE aces_tela_nro = ? ");
		sqlDeletaPermissao.append("AND aces_usu_nro = ? ");
		sqlDeletaPermissao.append("AND funcao_tela_nro = ? ");

		StringBuilder sqlDeletaAcesso = new StringBuilder();
		sqlDeletaAcesso.append("DELETE FROM s_acesso ");
		sqlDeletaAcesso.append("WHERE tela_nro = ? ");
		sqlDeletaAcesso.append("AND usu_nro = ? ");

		Connection conexao = null;
		PreparedStatement comandoDeletaAcesso = null;
		PreparedStatement comandoDeletaPermissao = null;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);

			comandoDeletaPermissao = conexao
					.prepareStatement(sqlDeletaPermissao.toString());
			comandoDeletaPermissao.setLong(1, acesso.getTela().getNro());
			comandoDeletaPermissao.setLong(2, acesso.getUsuario().getNro());
			comandoDeletaPermissao.setLong(3, acesso.getTela().getNro());
			comandoDeletaPermissao.executeUpdate();

			comandoDeletaAcesso = conexao.prepareStatement(sqlDeletaAcesso
					.toString());
			comandoDeletaAcesso.setLong(1, acesso.getTela().getNro());
			comandoDeletaAcesso.setLong(2, acesso.getUsuario().getNro());
			comandoDeletaAcesso.executeUpdate();

			conexao.commit();

			logger.info("Acesso excluído com sucesso");
		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException(
						"Ocorreu um erro ao tentar excluir o Acesso", ex);
			}
		} finally {
			PSTUtil.fechar(comandoDeletaPermissao);
			PSTUtil.fechar(comandoDeletaAcesso);
			PSTUtil.fechar(conexao);
		}

	}

	private List<Permissoes> listarPermissoes(Acesso acesso, Connection conexao)
			throws PSTException {

		StringBuilder sqlFuncao = new StringBuilder();
		sqlFuncao.append("SELECT ac.nro nroAC, ac.nome nomeAC ");
		sqlFuncao
				.append("from s_permissoes p, s_usuario u, s_telas t, s_funcoes f, s_acoes ac ");
		sqlFuncao.append("WHERE p.aces_tela_nro = t.nro ");
		sqlFuncao.append("AND p.aces_usu_nro = u.nro ");
		sqlFuncao.append("AND p.funcao_acao_nro = f.acao_nro ");
		sqlFuncao.append("AND p.funcao_tela_nro = f.tela_nro ");
		sqlFuncao.append("AND f.acao_nro = ac.nro ");
		sqlFuncao.append("AND u.nro = ? ");
		sqlFuncao.append("AND t.nro = ? ");

		ResultSet resultado = null;
		PreparedStatement comando = null;

		List<Permissoes> lista = new ArrayList<Permissoes>();

		try {

			comando = conexao.prepareStatement(sqlFuncao.toString());
			comando.setLong(1, acesso.getUsuario().getNro());
			comando.setLong(2, acesso.getTela().getNro());

			resultado = comando.executeQuery();

			Permissoes permissao = null;
			Funcoes funcao = null;
			Acoes acao = null;
			Tela tela = null;

			while (resultado.next()) {
				permissao = new Permissoes();
				funcao = new Funcoes();
				acao = new Acoes();
				tela = new Tela();

				acao.setNro(resultado.getLong("nroAC"));
				acao.setNome(resultado.getString("nomeAC"));
				funcao.setAcoes(acao);

				tela.setNro(acesso.getTela().getNro());
				tela.setNome(acesso.getTela().getNome());
				tela.setModulo(acesso.getTela().getModulo());
				tela.setListaFuncoes(acesso.getTela().getListaFuncoes());

				funcao.setTela(tela);

				permissao.setAcesso(acesso);
				permissao.setFuncoes(funcao);

				lista.add(permissao);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de funções", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
		}

		return lista;
	}

	private void incluirPermissoes(List<Permissoes> listaPermissoes,
			Connection conexao) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO S_PERMISSOES ");
		sql.append("(ACES_TELA_NRO, ACES_USU_NRO, FUNCAO_ACAO_NRO, FUNCAO_TELA_NRO) ");
		sql.append("VALUES (?, ?, ?, ?) ");

		PreparedStatement comando = null;

		try {

			comando = conexao.prepareStatement(sql.toString());

			for (Permissoes permissoes : listaPermissoes) {

				comando.setLong(1, permissoes.getAcesso().getTela().getNro());
				comando.setLong(2, permissoes.getAcesso().getUsuario().getNro());
				comando.setLong(3, permissoes.getFuncoes().getAcoes().getNro());
				comando.setLong(4, permissoes.getFuncoes().getTela().getNro());
				comando.executeUpdate();
			}

			logger.info("Permissões inseridas com sucesso");

		} catch (SQLException ex) {

			try {
				conexao.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new PSTException(
						"Ocorreu um erro no rollback de permissões "
								+ ex.getMessage(), ex);
			}

			if (ex.getMessage().contains("ORA-00001")) {
				throw new PSTException(
						"O acesso a essa tela já foi atribuído a esse usuário"
								+ ex.getMessage(), ex);
			} else {
				throw new PSTException(
						"Ocorreu um erro ao tentar inserir um acesso "
								+ ex.getMessage(), ex);
			}

		} finally {
			PSTUtil.fechar(comando);
		}
	}

	@Override
	public List<Acesso> listar(Usuario usuario) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ac.usu_nro, ac.tela_nro,  t.nro nroT, t.nome nomeT, m.nro nroM, m.nome nomeM ");
		sql.append("FROM s_acesso ac, s_usuario u, s_telas t, s_modulo m  ");
		sql.append("WHERE u.nro = ac.usu_nro  ");
		sql.append("AND ac.tela_nro = t.nro  ");
		sql.append("AND t.mod_nro = m.nro  ");
		sql.append("AND u.nro = ?  ");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Acesso> lista = new ArrayList<Acesso>();

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());

			comando.setLong(1, usuario.getNro());

			resultado = comando.executeQuery();

			Acesso acesso = null;
			Tela tela = null;
			Modulo modulo = null;

			while (resultado.next()) {

				acesso = new Acesso();
				tela = new Tela();
				modulo = new Modulo();

				tela.setNro(resultado.getLong("nroT"));
				tela.setNome(resultado.getString("nomeT"));

				modulo.setNro(resultado.getLong("nroM"));
				modulo.setNome(resultado.getString("nomeM"));

				tela.setModulo(modulo);

				TelaDAOImpl daoTela = new TelaDAOImpl();
				tela.setListaFuncoes(daoTela.listarFuncoes(tela, conexao));

				acesso.setTela(tela);
				acesso.setUsuario(usuario);
				acesso.setListaPermissoes(listarPermissoes(acesso, conexao));

				lista.add(acesso);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de acesso", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return lista;
	}

	@Override
	public List<Acesso> listar(Tela tela) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.nro nroP, p.nome nomeP ");
		sql.append("FROM s_acesso ac, s_usuario u, pessoa p, s_telas t  ");
		sql.append("WHERE u.nro = ac.usu_nro  ");
		sql.append("AND p.nro = u.pes_nro  ");
		sql.append("AND ac.tela_nro = t.nro ");
		sql.append("AND t.nro = ? ");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Acesso> lista = new ArrayList<Acesso>();

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());

			comando.setLong(1, tela.getNro());

			resultado = comando.executeQuery();

			Acesso acesso = null;
			Usuario usuario = null;
			Pessoa pessoa = null;

			while (resultado.next()) {

				acesso = new Acesso();
				usuario = new Usuario();
				pessoa = new Pessoa();

				pessoa.setNro(resultado.getLong("nroP"));
				pessoa.setNome(resultado.getString("nomeP"));

				usuario.setPessoa(pessoa);

				acesso.setUsuario(usuario);
				acesso.setTela(tela);

				lista.add(acesso);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de acesso", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return lista;
	}

	void excluirAcessoSemPermissao(Connection conexao) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM s_acesso ac WHERE concat(ac.tela_nro, ac.usu_nro) IN ");
		sql.append("(SELECT concat(acd.tela_nro, acd.usu_nro) FROM (SELECT * FROM s_acesso ac ");
		sql.append("LEFT JOIN s_permissoes p ON (ac.tela_nro = p.funcao_tela_nro) ");
		sql.append("WHERE p.funcao_tela_nro IS NULL) acd) ");

		PreparedStatement comando = null;

		try {

			comando = conexao.prepareStatement(sql.toString());
			comando.executeUpdate();

			logger.info("Acessos sem permissões excluídos com sucesso");

		} catch (SQLException ex) {

			try {
				conexao.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new PSTException(
						"Ocorreu um erro no rollback de exclusão de acessos sem permissões "
								+ ex.getMessage(), ex);
			}

		} finally {
			PSTUtil.fechar(comando);
		}
	}
}
