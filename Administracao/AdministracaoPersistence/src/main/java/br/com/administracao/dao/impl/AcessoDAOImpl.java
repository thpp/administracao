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

			incluiPermissoes(acesso.getListaPermissoes(), conexao);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		// TODO Auto-generated method stub

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
				tela.setListaFuncoes(listaFuncoes(tela, conexao));

				acesso.setTela(tela);
				acesso.setUsuario(usuario);
				acesso.setListaPermissoes(listaPermissoes(acesso, conexao));

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

	public List<Permissoes> listaPermissoes(Acesso acesso, Connection conexao)
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

	public List<Funcoes> listaFuncoes(Tela tela, Connection conexao)
			throws PSTException {

		StringBuilder sqlFuncao = new StringBuilder();
		sqlFuncao
				.append("select f.ACAO_NRO acaoF, f.TELA_NRO telaF, ac.NRO nroAC, ac.NOME nomeAC  ");
		sqlFuncao.append("from s_funcoes f, s_acoes ac ");
		sqlFuncao.append("where ac.NRO = f.ACAO_NRO and f.TELA_NRO = ? ");

		ResultSet resultado = null;
		PreparedStatement comando = null;

		List<Funcoes> lista = new ArrayList<Funcoes>();

		try {

			comando = conexao.prepareStatement(sqlFuncao.toString());
			comando.setLong(1, tela.getNro());

			resultado = comando.executeQuery();

			Funcoes funcao = null;
			Acoes acao = null;

			while (resultado.next()) {
				funcao = new Funcoes();
				acao = new Acoes();

				acao.setNro(resultado.getLong("nroAC"));
				acao.setNome(resultado.getString("nomeAC"));

				funcao.setTela(tela);
				funcao.setAcoes(acao);

				lista.add(funcao);
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

	private void incluiPermissoes(List<Permissoes> listaPermissoes,
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

	public static void main(String[] args) {

		// AcessoDAOImpl dao = new AcessoDAOImpl();
		//
		// Usuario usuario = new Usuario();
		// Tela tela = new Tela();
		// Acesso acesso = new Acesso();
		// List<Permissoes> listaPermissoes = new ArrayList<Permissoes>();
		//
		// Funcoes f1 = new Funcoes();
		// Funcoes f2 = new Funcoes();
		// Funcoes f3 = new Funcoes();
		//
		// Acoes ac1 = new Acoes();
		// Acoes ac2 = new Acoes();
		// Acoes ac3 = new Acoes();
		//
		// Permissoes p1 = new Permissoes();
		// Permissoes p2 = new Permissoes();
		// Permissoes p3 = new Permissoes();
		//
		// ac1.setNro(1L);
		// ac2.setNro(2L);
		// ac3.setNro(3L);
		//
		// usuario.setNro(1L);
		// tela.setNro(3L);
		// acesso.setTela(tela);
		// acesso.setUsuario(usuario);
		//
		// f1.setTela(tela);
		// f2.setTela(tela);
		// f3.setTela(tela);
		//
		// f1.setAcoes(ac1);
		// f2.setAcoes(ac2);
		// f3.setAcoes(ac3);
		//
		// p1.setAcesso(acesso);
		// p1.setFuncoes(f1);
		//
		// p2.setAcesso(acesso);
		// p2.setFuncoes(f2);
		//
		// p3.setAcesso(acesso);
		// p3.setFuncoes(f3);
		//
		// listaPermissoes.add(p1);
		// listaPermissoes.add(p2);
		// listaPermissoes.add(p3);
		//
		// acesso.setListaPermicoes(listaPermissoes);
		//
		// try {
		// dao.inserir(acesso);
		// System.out.println("Sucesso!...");
		// } catch (PSTException e) {
		// System.out.println("ERRO.... " + e.getMessage());
		// e.printStackTrace();
		// }

		AcessoDAOImpl dao = new AcessoDAOImpl();
		UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();

		try {

			List<Usuario> listausuario = daoUsuario.listar("40998069876");
			Usuario usuario = listausuario.get(0);

			System.out.println("Nome: " + usuario.getPessoa().getNome());

			List<Acesso> lista = dao.listar(usuario);

			for (Acesso acesso : lista) {
				System.out.println("Nome da Tela: "
						+ acesso.getTela().getNome());
				System.out.println("Nome do Módulo: "
						+ acesso.getTela().getModulo().getNome());

				List<Permissoes> listaPermissoes = acesso.getListaPermissoes();

				System.out.println("Permissões dessa tela: ");

				for (Permissoes permissoes : listaPermissoes) {
					System.out.println("Nro da Ação: "
							+ permissoes.getFuncoes().getAcoes().getNro());
					System.out.println("Nome da Ação: "
							+ permissoes.getFuncoes().getAcoes().getNome());
				}
				System.out.println("=====================================");
			}

		} catch (PSTException e) {
			System.out.println("ERRO.... " + e.getMessage());
			e.printStackTrace();
		}

	}
}
