package br.com.administracao.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.TelaDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Acoes;
import br.com.administracao.model.Funcoes;
import br.com.administracao.model.Modulo;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Tela;
import br.com.administracao.util.PSTUtil;

public class TelaDAOImpl implements TelaDAO {

	private static Logger logger = Logger.getLogger(ProjetoDAOImpl.class
			.getName());

	@Override
	public void inserir(Tela tela) throws PSTException {
		StringBuilder sqlTela = new StringBuilder();
		sqlTela.append("BEGIN INSERT INTO S_TELAS ");
		sqlTela.append("(NOME, MOD_NRO, DATA_INC) ");
		sqlTela.append("VALUES (UPPER(?), ?, TRUNC(SYSDATE)) RETURNING NRO into ?; END; ");

		StringBuilder sqlFuncao = new StringBuilder();
		sqlFuncao.append("INSERT INTO S_FUNCOES ");
		sqlFuncao.append("(ACAO_NRO, TELA_NRO) ");
		sqlFuncao.append("VALUES (?, ?)");

		Connection conexao = null;
		CallableStatement call = null;
		PreparedStatement comando = null;

		Integer ultimoId;

		try {
			conexao = ConnectionFactory.getConnection();

			conexao.setAutoCommit(false);

			call = conexao.prepareCall(sqlTela.toString());

			call.setString(1, tela.getNome());
			call.setLong(2, tela.getModulo().getNro());

			call.registerOutParameter(3, java.sql.Types.INTEGER);

			call.executeUpdate();

			ultimoId = call.getInt(3);

			comando = conexao.prepareStatement(sqlFuncao.toString());

			for (Funcoes funcoes : tela.getListaFuncoes()) {
				comando.setLong(1, funcoes.getAcoes().getNro());
				comando.setLong(2, ultimoId);

				comando.executeUpdate();
			}

			conexao.commit();

			logger.info("Tela inserida com sucesso");
		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException(
						"Ocorreu um erro ao tentar dar rollback em tela "
								+ ex.getCause(), ex);
			}
			throw new PSTException("Ocorreu um erro ao tentar inserir a tela "
					+ ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(call);
			PSTUtil.fechar(conexao);
			PSTUtil.fechar(comando);
		}
	}

	@Override
	public void editar(Tela tela) throws PSTException {

		StringBuilder sqlAtualizaTela = new StringBuilder();
		sqlAtualizaTela.append("UPDATE s_telas ");
		sqlAtualizaTela.append("SET nome = UPPER(?), MOD_NRO = ? ");
		sqlAtualizaTela.append("WHERE nro = ? ");

		StringBuilder sqlDeletaFuncao = new StringBuilder();
		sqlDeletaFuncao.append("DELETE FROM S_FUNCOES ");
		sqlDeletaFuncao.append("WHERE ACAO_NRO = ? ");
		sqlDeletaFuncao.append("AND TELA_NRO = ? ");

		StringBuilder sqlInsereFuncao = new StringBuilder();
		sqlInsereFuncao.append("INSERT INTO S_FUNCOES ");
		sqlInsereFuncao.append("(ACAO_NRO, TELA_NRO) ");
		sqlInsereFuncao.append("VALUES (?, ?)");

		StringBuilder sqlDeletaPermissao = new StringBuilder();
		sqlDeletaPermissao.append("DELETE FROM s_permissoes ");
		sqlDeletaPermissao.append("WHERE aces_tela_nro = ? ");
		sqlDeletaPermissao.append("AND funcao_acao_nro = ? ");
		sqlDeletaPermissao.append("AND funcao_tela_nro = ? ");

		Connection conexao = null;
		PreparedStatement comandoAtualizaTela = null;
		PreparedStatement comandoDeletaFuncao = null;
		PreparedStatement comandoInsereFuncao = null;
		PreparedStatement comandoDeletaPermissao = null;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);

			List<Funcoes> funcoesBean = tela.getListaFuncoes();
			List<Funcoes> funcoesBanco = listarFuncoes(tela, conexao);

			List<Acoes> acoesBean = new ArrayList<Acoes>();
			for (Funcoes funcao : funcoesBean) {
				acoesBean.add(funcao.getAcoes());
			}

			List<Acoes> acoesBanco = new ArrayList<Acoes>();
			for (Funcoes funcao : funcoesBanco) {
				acoesBanco.add(funcao.getAcoes());
			}

			List<Funcoes> listaInserir = new ArrayList<Funcoes>();
			listaInserir.addAll(funcoesBean);

			List<Funcoes> listaExcluir = new ArrayList<Funcoes>();
			listaExcluir.addAll(funcoesBanco);

			// Tira da lista de inserção os que já constam no banco
			for (Funcoes funcao : funcoesBean) {
				for (Acoes acao : acoesBanco) {
					if (funcao.getAcoes().getNro().equals(acao.getNro())) {
						listaInserir.remove(funcao);
					}
				}
			}

			// Tira da lista de exclusão os que constam no bean
			for (Funcoes funcao : funcoesBanco) {
				for (Acoes acao : acoesBean) {
					if (funcao.getAcoes().getNro().equals(acao.getNro())) {
						listaExcluir.remove(funcao);
					}
				}
			}

			comandoAtualizaTela = conexao.prepareStatement(sqlAtualizaTela
					.toString());
			comandoAtualizaTela.setString(1, tela.getNome());
			comandoAtualizaTela.setLong(2, tela.getModulo().getNro());
			comandoAtualizaTela.setLong(3, tela.getNro());
			comandoAtualizaTela.executeUpdate();

			comandoDeletaFuncao = conexao.prepareStatement(sqlDeletaFuncao
					.toString());
			comandoDeletaPermissao = conexao
					.prepareStatement(sqlDeletaPermissao.toString());

			for (Funcoes funcoes : listaExcluir) {
				comandoDeletaPermissao.setLong(1, tela.getNro());
				comandoDeletaPermissao.setLong(2, funcoes.getAcoes().getNro());
				comandoDeletaPermissao.setLong(3, tela.getNro());
				comandoDeletaPermissao.executeUpdate();

				comandoDeletaFuncao.setLong(1, funcoes.getAcoes().getNro());
				comandoDeletaFuncao.setLong(2, tela.getNro());
				comandoDeletaFuncao.executeUpdate();
			}

			comandoInsereFuncao = conexao.prepareStatement(sqlInsereFuncao
					.toString());

			for (Funcoes funcoes : listaInserir) {
				comandoInsereFuncao.setLong(1, funcoes.getAcoes().getNro());
				comandoInsereFuncao.setLong(2, tela.getNro());
				comandoInsereFuncao.executeUpdate();
			}

			// Se excluiu alguma função, executa a rotina que deleta acessos sem
			// permissão
			if (listaExcluir.size() > 0) {
				excluirAcessoSemPermissao(conexao);
			}

			conexao.commit();

			logger.info("Tela editada com sucesso");
		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException(
						"Ocorreu um erro ao tentar editar a tela", ex);
			}
		} finally {
			PSTUtil.fechar(comandoAtualizaTela);
			PSTUtil.fechar(comandoDeletaPermissao);
			PSTUtil.fechar(comandoDeletaFuncao);
			PSTUtil.fechar(comandoInsereFuncao);
			PSTUtil.fechar(conexao);
		}

	}

	@Override
	public void excluir(Long codigo) throws PSTException {

		StringBuilder sqlDeletaPermissao = new StringBuilder();
		sqlDeletaPermissao.append("DELETE FROM s_permissoes ");
		sqlDeletaPermissao.append("WHERE aces_tela_nro = ? ");
		sqlDeletaPermissao.append("AND funcao_tela_nro = ? ");

		StringBuilder sqlDeletaAcesso = new StringBuilder();
		sqlDeletaAcesso.append("DELETE FROM s_acesso ");
		sqlDeletaAcesso.append("WHERE tela_nro = ? ");

		StringBuilder sqlDeletaFuncao = new StringBuilder();
		sqlDeletaFuncao.append("DELETE FROM s_funcoes ");
		sqlDeletaFuncao.append("WHERE tela_nro = ? ");

		StringBuilder sqlDeletaTela = new StringBuilder();
		sqlDeletaTela.append("DELETE FROM s_telas ");
		sqlDeletaTela.append("WHERE nro = ? ");

		Connection conexao = null;
		PreparedStatement comandoDeletaPermissao = null;
		PreparedStatement comandoDeletaAcesso = null;
		PreparedStatement comandoDeletaTela = null;
		PreparedStatement comandoDeletaFuncao = null;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);

			comandoDeletaPermissao = conexao
					.prepareStatement(sqlDeletaPermissao.toString());
			comandoDeletaPermissao.setLong(1, codigo);
			comandoDeletaPermissao.setLong(2, codigo);
			comandoDeletaPermissao.executeUpdate();

			comandoDeletaAcesso = conexao.prepareStatement(sqlDeletaAcesso
					.toString());
			comandoDeletaAcesso.setLong(1, codigo);
			comandoDeletaAcesso.executeUpdate();

			comandoDeletaFuncao = conexao.prepareStatement(sqlDeletaFuncao
					.toString());
			comandoDeletaFuncao.setLong(1, codigo);
			comandoDeletaFuncao.executeUpdate();

			comandoDeletaTela = conexao.prepareStatement(sqlDeletaTela
					.toString());
			comandoDeletaTela.setLong(1, codigo);
			comandoDeletaTela.executeUpdate();

			conexao.commit();

			logger.info("Tela excluída com sucesso");
		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException(
						"Ocorreu um erro ao tentar excluir a tela", ex);
			}
		} finally {
			PSTUtil.fechar(comandoDeletaPermissao);
			PSTUtil.fechar(comandoDeletaAcesso);
			PSTUtil.fechar(comandoDeletaFuncao);
			PSTUtil.fechar(comandoDeletaTela);
			PSTUtil.fechar(conexao);
		}

	}

	private void excluirAcessoSemPermissao(Connection conexao)
			throws PSTException {

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

	@Override
	public List<Tela> listar(int primeiro, int tamanho) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.nro nroT, t.nome nomeT, t.mod_nro nroM, m.nome nomeM, m.proj_nro nroP, p.NOME nomeP ");
		sql.append("FROM s_telas t, s_modulo m, s_projeto p  ");
		sql.append("where t.mod_nro = m.nro and m.PROJ_NRO = p.NRO ");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Tela> lista = new ArrayList<Tela>();

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());

			resultado = comando.executeQuery();

			Tela tela = null;
			Modulo modelo = null;
			Projeto projeto = null;

			while (resultado.next()) {

				tela = new Tela();
				modelo = new Modulo();
				projeto = new Projeto();

				tela.setNro(resultado.getLong("nroT"));
				tela.setNome(resultado.getString("nomeT"));

				projeto.setNro(resultado.getLong("nroP"));
				projeto.setNome(resultado.getString("nomeP"));

				modelo.setNro(resultado.getLong("nroM"));
				modelo.setNome(resultado.getString("nomeM"));
				modelo.setProjeto(projeto);

				tela.setModulo(modelo);
				tela.setListaFuncoes(listarFuncoes(tela, conexao));

				lista.add(tela);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de telas", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return lista;
	}

	private List<Funcoes> listarFuncoes(Tela tela, Connection conexao)
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

	@Override
	public List<Tela> listar(Projeto projetoBusca, Modulo modulo, String nome)
			throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.nro nroT, t.nome nomeT, t.mod_nro nroM, m.nome nomeM, m.proj_nro nroP, p.NOME nomeP ");
		sql.append("FROM s_telas t, s_modulo m, s_projeto p  ");
		sql.append("where t.mod_nro = m.nro and m.PROJ_NRO = p.NRO ");

		if (projetoBusca != null) {
			sql.append("and p.NRO = ? ");
		}

		if (modulo != null) {
			sql.append("and m.NRO = ? ");
		}

		if (nome != null) {
			sql.append("and t.nome like UPPER(?) ");
		}

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Tela> lista = new ArrayList<Tela>();

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());

			if (projetoBusca != null && modulo == null) {

				comando.setLong(1, projetoBusca.getNro());

				if (nome != null) {
					comando.setString(2, "%" + nome + "%");
				}

			} else if (projetoBusca != null && modulo != null) {
				comando.setLong(1, projetoBusca.getNro());
				comando.setLong(2, modulo.getNro());

				if (nome != null) {
					comando.setString(3, "%" + nome + "%");
				}
			}

			if (projetoBusca == null && modulo == null && nome != null) {
				comando.setString(1, "%" + nome + "%");
			}

			resultado = comando.executeQuery();

			Tela tela = null;
			Modulo modelo = null;
			Projeto projeto = null;

			while (resultado.next()) {

				tela = new Tela();
				modelo = new Modulo();
				projeto = new Projeto();

				tela.setNro(resultado.getLong("nroT"));
				tela.setNome(resultado.getString("nomeT"));

				projeto.setNro(resultado.getLong("nroP"));
				projeto.setNome(resultado.getString("nomeP"));

				modelo.setNro(resultado.getLong("nroM"));
				modelo.setNome(resultado.getString("nomeM"));
				modelo.setProjeto(projeto);

				tela.setModulo(modelo);
				tela.setListaFuncoes(listarFuncoes(tela, conexao));

				lista.add(tela);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de telas", ex);
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

	public static void main(String[] args) {

		TelaDAOImpl dao = new TelaDAOImpl();

		try {

			Modulo modulo = new Modulo();
			modulo.setNro(8L);

			Tela tela = new Tela();
			tela.setNro(5L);
			tela.setNome("TESTE LETICIA 67898");
			tela.setModulo(modulo);

			Acoes a1 = new Acoes();
			a1.setNro(1L);
			a1.setNome("INSERIR");

			Acoes a2 = new Acoes();
			a2.setNro(4L);
			a2.setNome("BUSCAR");

			Acoes a3 = new Acoes();
			a3.setNro(5L);
			a3.setNome("RELATÓRIO");

			Funcoes f1 = new Funcoes();
			f1.setAcoes(a1);

			Funcoes f2 = new Funcoes();
			f2.setAcoes(a2);

			Funcoes f3 = new Funcoes();
			f3.setAcoes(a3);

			List<Funcoes> funcoesBean = new ArrayList<Funcoes>();
			funcoesBean.add(f1);
			funcoesBean.add(f2);
			funcoesBean.add(f3);

			tela.setListaFuncoes(funcoesBean);
			dao.editar(tela);

		} catch (PSTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRO: " + e.getMessage());
		}
	}

}
