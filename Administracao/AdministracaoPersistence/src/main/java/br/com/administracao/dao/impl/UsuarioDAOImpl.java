package br.com.administracao.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.UsuarioDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Pessoa;
import br.com.administracao.model.Usuario;
import br.com.administracao.util.Helper;
import br.com.administracao.util.PSTUtil;

public class UsuarioDAOImpl implements UsuarioDAO {

	private static Logger logger = Logger.getLogger(UsuarioDAOImpl.class
			.getName());

	// retorna null se pessoa foi incluída no banco, caso contrário retorna o
	// usuário cadastrado
	@Override
	public Usuario inserir(Usuario usuario) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append("BEGIN INSERT INTO S_USUARIO ");
		sql.append("(USUARIO, SENHA, OBS, FLG_ATIVO, FLG_PROFISSIONAL, FLG_ADM, DATA_BAIXA, PES_NRO) ");
		sql.append("VALUES (UPPER(?), ?, ?, ?, ?, ?, ?, ?) RETURNING NRO into ?; END; ");

		Connection conexao = null;
		CallableStatement comando = null;
		Pessoa pessoa = null;
		Boolean retorno = null;

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareCall(sql.toString());
			comando.setString(1, usuario.getUsuario());
			comando.setString(2, usuario.getSenha());
			comando.setString(3, usuario.getObs());
			comando.setString(4, "S"); // flgAtivo
			comando.setString(5, usuario.getFlgProfissional() ? "S" : "N");

			if (usuario.getFlgAdm()) {
				Usuario adm = buscaADM();
				if (adm == null) {
					comando.setString(6, "S");
				} else {
					throw new PSTException(
							"O sistema já possui um administrador, contate: "
									+ adm.getPessoa().getNome());
				}
			} else {
				comando.setString(6, "N");
			}

			comando.setDate(7, null);

			// verifica se o cpf já está cadastrado na tabela pessoa
			pessoa = buscaPessoa(usuario.getPessoa().getCpf());

			if (pessoa == null) {
				Integer nroPessoa = inserirPessoa(usuario.getPessoa());
				comando.setLong(8, nroPessoa);
				retorno = Boolean.FALSE;
			} else {
				comando.setLong(8, pessoa.getNro());
				retorno = Boolean.TRUE;
			}

			comando.registerOutParameter(9, java.sql.Types.INTEGER);

			comando.executeUpdate();

			Long idUsuario = comando.getLong(9);

			logger.info("Usuário inserido com sucesso");

			if (retorno) {
				usuario.setNro(idUsuario);
				usuario.setPessoa(pessoa);
				return usuario;
			} else {
				return null;
			}

		} catch (SQLException ex) {

			if (ex.getMessage().contains("ORA-00001")) {
				throw new PSTException(
						"Este usuário já existe ou este CPF já está sendo usado por outro usuário"
								+ ex.getMessage(), ex);
			} else {
				throw new PSTException(
						"Ocorreu um erro ao tentar inserir um usuário "
								+ ex.getMessage(), ex);
			}

		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
	}

	@Override
	public void editar(Usuario usuario) throws PSTException {

		StringBuilder sqlUpdatePessoa = new StringBuilder();
		sqlUpdatePessoa.append("UPDATE PESSOA ");
		sqlUpdatePessoa
				.append("SET NOME = UPPER(?), CNPJ_CPF = ?, FLG_PESSOA = UPPER(?)");
		sqlUpdatePessoa.append("WHERE NRO = ? ");

		StringBuilder sqlUpdateUsuario = new StringBuilder();
		sqlUpdateUsuario.append("UPDATE S_USUARIO ");
		sqlUpdateUsuario
				.append("SET USUARIO = UPPER(?), SENHA = ?, OBS = ?, FLG_ADM = ?, FLG_ATIVO = ?, DATA_BAIXA = ?");
		sqlUpdateUsuario.append("WHERE NRO = ? ");

		Connection conexao = null;
		PreparedStatement comandoUpdatePessoa = null;
		PreparedStatement comandoUpdateUsuario = null;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);

			comandoUpdatePessoa = conexao.prepareStatement(sqlUpdatePessoa
					.toString());
			comandoUpdatePessoa.setString(1, usuario.getPessoa().getNome());
			comandoUpdatePessoa.setString(2,
					new Helper().aplicarMascara(usuario.getPessoa().getCpf()));
			comandoUpdatePessoa
					.setString(3, usuario.getPessoa().getFlgPessoa());
			comandoUpdatePessoa.setLong(4, usuario.getPessoa().getNro());

			comandoUpdatePessoa.executeUpdate();

			comandoUpdateUsuario = conexao.prepareStatement(sqlUpdateUsuario
					.toString());
			comandoUpdateUsuario.setString(1, usuario.getUsuario());
			comandoUpdateUsuario.setString(2, usuario.getSenha());
			comandoUpdateUsuario.setString(3, usuario.getObs());

			if (usuario.getFlgAdm()) {
				Usuario adm = buscaADM();
				if (adm == null) {
					comandoUpdateUsuario.setString(4, "S");
				} else {
					if (adm.getNro().equals(usuario.getNro())) {
						comandoUpdateUsuario.setString(4,
								usuario.getFlgAdm() ? "S" : "N");
					} else {
						throw new PSTException(
								"O sistema já possui um administrador, contate: "
										+ adm.getPessoa().getNome());
					}
				}
			} else {
				comandoUpdateUsuario.setString(4, "N");
			}

			comandoUpdateUsuario
					.setString(5, usuario.getFlgAtivo() ? "S" : "N");
			if (usuario.getDataBaixa() != null) {
				comandoUpdateUsuario.setDate(6, new java.sql.Date(
						((java.util.Date) usuario.getDataBaixa()).getTime()));
			} else {
				comandoUpdateUsuario.setDate(6, null);
			}

			comandoUpdateUsuario.setLong(7, usuario.getNro());

			comandoUpdateUsuario.executeUpdate();

			conexao.commit();

			logger.info("Usuário editado com sucesso");
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar editar um usuário "
							+ ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comandoUpdatePessoa);
			PSTUtil.fechar(comandoUpdateUsuario);
			PSTUtil.fechar(conexao);
		}

	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM S_USUARIO ");
		sql.append("WHERE NRO = ? ");

		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();

			comando = conexao.prepareStatement(sql.toString());
			comando.setLong(1, codigo);

			comando.executeUpdate();

			logger.info("Usuário excluído com sucesso");
		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar excluir um usuário", ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

	}

	@Override
	public List<Usuario> listar(int primeiro, int tamanho, String nome,
			String flgAtivo) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT U.NRO nroUsuario, U.USUARIO usuario, U.SENHA senha, U.OBS obs, U.PES_NRO pesNro, U.FLG_PROFISSIONAL flgProfissional, U.FLG_ADM flgadm, U.FLG_ATIVO flgAtivo, P.NOME nomePessoa, P.FLG_PESSOA flgPessoa, P.CNPJ_CPF cnpjCpf, U.DATA_INC dataInc, U.DATA_BAIXA dataBaixa ");
		sql.append("FROM S_USUARIO U, PESSOA P ");
		sql.append("WHERE U.FLG_ATIVO = UPPER(?) ");
		sql.append("AND U.PES_NRO = P.NRO ");

		if (nome != null) {
			sql.append("AND P.NOME LIKE UPPER(?) ");
		}
		;

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Usuario> lista = new ArrayList<Usuario>();

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, flgAtivo);
			if (nome != null) {
				comando.setString(2, "%" + nome + "%");
			}

			resultado = comando.executeQuery();

			while (resultado.next()) {
				Usuario usuario = new Usuario();
				usuario = new Usuario();
				usuario.setNro(resultado.getLong("nroUsuario"));
				usuario.setUsuario(resultado.getString("usuario"));
				usuario.setSenha(resultado.getString("senha"));
				usuario.setObs(resultado.getString("obs"));
				usuario.setFlgProfissional(resultado.getString(
						"flgProfissional").equals("S") ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setFlgAdm(resultado.getString("flgadm").equals("S") ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setFlgAtivo(resultado.getString("flgAtivo").equals("S") ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setPessoa(new Pessoa(resultado.getLong("pesNro"),
						resultado.getString("nomePessoa"), resultado
								.getString("flgPessoa"), resultado
								.getString("cnpjCpf")));
				usuario.setDataInclusao(resultado.getDate("dataInc"));
				usuario.setDataBaixa(resultado.getDate("dataBaixa"));

				lista.add(usuario);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de usuários",
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

	@Override
	public List<Usuario> listar(String cpf) throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT U.NRO nroUsuario, U.USUARIO usuario, U.SENHA senha, U.OBS obs, U.PES_NRO pesNro, U.FLG_PROFISSIONAL flgProfissional, U.FLG_ADM flgadm, U.FLG_ATIVO flgAtivo, P.NOME nomePessoa, P.FLG_PESSOA flgPessoa, P.CNPJ_CPF cnpjCpf, U.DATA_INC dataInc, U.DATA_BAIXA dataBaixa ");
		sql.append("FROM S_USUARIO U, PESSOA P ");
		sql.append("WHERE U.FLG_ATIVO = 'S' ");
		sql.append("AND U.PES_NRO = P.NRO ");
		sql.append("AND P.CNPJ_CPF = ? ");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Usuario> lista = new ArrayList<Usuario>();

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, new Helper().aplicarMascara(cpf));
			resultado = comando.executeQuery();

			while (resultado.next()) {
				Usuario usuario = new Usuario();
				usuario = new Usuario();
				usuario.setNro(resultado.getLong("nroUsuario"));
				usuario.setUsuario(resultado.getString("usuario"));
				usuario.setSenha(resultado.getString("senha"));
				usuario.setObs(resultado.getString("obs"));
				usuario.setFlgProfissional(resultado.getString(
						"flgProfissional").equals("S") ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setFlgAdm(resultado.getString("flgadm").equals("S") ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setFlgAtivo(resultado.getString("flgAtivo").equals("S") ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setPessoa(new Pessoa(resultado.getLong("pesNro"),
						resultado.getString("nomePessoa"), resultado
								.getString("flgPessoa"), resultado
								.getString("cnpjCpf")));
				usuario.setDataInclusao(resultado.getDate("dataInc"));
				usuario.setDataBaixa(resultado.getDate("dataBaixa"));

				lista.add(usuario);
			}

		} catch (SQLException ex) {
			throw new PSTException(
					"Ocorreu um erro ao tentar obter a listagem de usuários",
					ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return lista;
	}

	public Usuario buscaADM() throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT U.NRO nroUsuario, U.USUARIO usuario, U.SENHA senha, U.OBS obs, U.PES_NRO pesNro, U.FLG_PROFISSIONAL flgProfissional, P.NOME nomePessoa, P.FLG_PESSOA flgPessoa, P.CNPJ_CPF cnpjCpf ");
		sql.append("FROM S_USUARIO U, PESSOA P ");
		sql.append("WHERE U.FLG_ADM = 'S' ");
		sql.append("AND U.FLG_ATIVO = 'S' ");
		sql.append("AND U.PES_NRO = P.NRO ");

		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		Usuario usuario = null;

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());
			resultado = comando.executeQuery();

			while (resultado.next()) {
				usuario = new Usuario();
				usuario.setNro(resultado.getLong("nroUsuario"));
				usuario.setUsuario(resultado.getString("usuario"));
				usuario.setSenha(resultado.getString("senha"));
				usuario.setObs(resultado.getString("obs"));
				usuario.setFlgProfissional(resultado
						.getString("flgProfissional") == "S" ? Boolean.TRUE
						: Boolean.FALSE);
				usuario.setFlgAdm(Boolean.TRUE);
				usuario.setFlgAtivo(Boolean.TRUE);
				usuario.setPessoa(new Pessoa(resultado.getLong("pesNro"),
						resultado.getString("nomePessoa"), resultado
								.getString("flgPessoa"), resultado
								.getString("cnpjCpf")));
			}

		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar buscar o ADM", ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return usuario;
	}

	private Integer inserirPessoa(Pessoa pessoa) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append("BEGIN INSERT INTO PESSOA ");
		sql.append("(NRO, NOME, CNPJ_CPF, FLG_PESSOA) ");
		sql.append("VALUES ((select F_NRO_PESSOA from DUAL), UPPER(?), ?, UPPER(?)) RETURNING NRO into ?; END; ");

		Connection conexao = null;
		CallableStatement call = null;

		Integer ultimoId;

		try {
			conexao = ConnectionFactory.getConnection();
			conexao.setAutoCommit(false);

			call = conexao.prepareCall(sql.toString());

			call.setString(1, pessoa.getNome());
			call.setString(2, new Helper().aplicarMascara(pessoa.getCpf()));
			call.setString(3, pessoa.getFlgPessoa());

			call.registerOutParameter(4, java.sql.Types.INTEGER);

			call.executeUpdate();

			ultimoId = call.getInt(4);

			conexao.commit();

			logger.info("Pessoa inserida com sucesso");
		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException(
						"Ocorreu um erro ao tentar dar rollback em pessoa "
								+ ex.getCause(), ex);
			}
			throw new PSTException(
					"Ocorreu um erro ao tentar inserir uma pessoa "
							+ ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(call);
			PSTUtil.fechar(conexao);
		}

		return ultimoId;

	}

	public Pessoa buscaPessoa(String cpf) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NRO, NOME, FLG_PESSOA, CNPJ_CPF ");
		sql.append("FROM PESSOA ");
		sql.append("WHERE CNPJ_CPF = ? ");

		Pessoa pessoa = null;
		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());

			comando.setString(1, new Helper().aplicarMascara(cpf));

			resultado = comando.executeQuery();

			while (resultado.next()) {
				pessoa = new Pessoa();
				pessoa.setNro(resultado.getLong("NRO"));
				pessoa.setNome(resultado.getString("NOME"));
				pessoa.setCpf(resultado.getString("CNPJ_CPF"));
				pessoa.setFlgPessoa(resultado.getString("FLG_PESSOA"));

			}

		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar buscar a pessoa",
					ex);
		} finally {
			PSTUtil.fechar(resultado);
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}

		return pessoa;

	}

	public static void main(String[] args) {

		try {
			UsuarioDAOImpl dao = new UsuarioDAOImpl();

			Usuario usuario = new Usuario();
			Pessoa pessoa = new Pessoa();

			pessoa.setNome("Thiago Henrique");
			pessoa.setFlgPessoa("M");
			pessoa.setCpf("369.953.698-07");

			usuario.setUsuario("formen");
			usuario.setSenha("123mudar");
			usuario.setFlgAtivo(Boolean.TRUE);
			usuario.setFlgAdm(Boolean.FALSE);
			usuario.setFlgProfissional(Boolean.TRUE);
			usuario.setObs("Teste.....");
			usuario.setPessoa(pessoa);

			dao.inserir(usuario);
			//
			// System.out.println("Sucesso!!....");

			// Pessoa pessoa1 = dao.buscaPessoa("36995369807");
			// usuario.setNro(1L);
			// usuario.setDataBaixa(new java.util.Date());

			// List<Usuario> lista = dao.listar(0, 0, null, "S");
			//
			// for (Usuario usuario : lista) {
			// System.out.println("Nome: "+usuario.getPessoa().getNome());
			// System.out.println("Usuario: "+usuario.getUsuario());
			// System.out.println("Senha: "+usuario.getSenha());
			// }

		} catch (PSTException e) {
			System.out.println("ERRO: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
