package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.UsuarioDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
import br.com.administracao.model.Projeto;
import br.com.administracao.model.Usuario;
import br.com.administracao.util.PSTUtil;

public class UsuarioDAOImpl implements UsuarioDAO {
	
	private static Logger logger = Logger.getLogger(UsuarioDAOImpl.class.getName());

	@Override
	public void inserir(Usuario usuario) throws PSTException {
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO S_USUARIO ");
		sql.append("(NOME_COMPLETO, CPF, CARGO, USUARIO, SENHA, DATA_BAIXA, FLG_ATIVO, DATA_INC) ");			
		sql.append("VALUES (UPPER(?), ?, ?, ?, ?, ?, ?, TRUNC(SYSDATE)) ");
		
		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, usuario.getNome());
			comando.setString(2, usuario.getCpf());
			comando.setString(3, usuario.getCargo());
			comando.setString(4, usuario.getUsuario());
			comando.setString(5, usuario.getSenha());
			comando.setDate(6, null);
			comando.setString(7, "S");
			
			comando.executeUpdate();

			logger.info("Usuario inserido com sucesso");
		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar inserir um usuario "+ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}		
	}

	@Override
	public void editar(Usuario usuario) throws PSTException {		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE S_USUARIO ");
		sql.append("SET NOME_COMPLETO = UPPER(?), CPF = ?, CARGO = ?, USUARIO = ?, SENHA = ?, DATA_BAIXA = ?, FLG_ATIVO = ?");
		sql.append("WHERE NRO = ? ");
		
		Connection conexao = null;
		PreparedStatement comando = null;

		try {
			conexao = ConnectionFactory.getConnection();
			comando = conexao.prepareStatement(sql.toString());
			comando.setString(1, usuario.getNome());
			comando.setString(2, usuario.getCpf());
			comando.setString(3, usuario.getCargo());
			comando.setString(4, usuario.getUsuario());
			comando.setString(5, usuario.getSenha());
			comando.setDate(6, usuario.getDataBaixa() == null ? null : new java.sql.Date(((java.util.Date) usuario.getDataBaixa()).getTime()));
			comando.setString(7, usuario.getFlgAtivo() ? "S" : "N");
			
			comando.setLong(8, usuario.getNro());
			
			comando.executeUpdate();

			logger.info("Usuario editado com sucesso");
		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar editado um usuario "+ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(comando);
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

			logger.info("Usuario excluido com sucesso");
		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar excluir um usuario", ex);
		} finally {
			PSTUtil.fechar(comando);
			PSTUtil.fechar(conexao);
		}
		
	}

	@Override
	public List<Usuario> listar(int primeiro, int tamanho, String nome)throws PSTException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NRO, NOME_COMPLETO, CPF, CARGO, USUARIO, SENHA, DATA_BAIXA, FLG_ATIVO, DATA_INC ");
		sql.append("FROM S_USUARIO ");
		
		if(nome != null){
			sql.append("WHERE NOME_COMPLETO LIKE UPPER(?)");
		};
		
		Connection conexao = null;
		PreparedStatement comando = null;
		ResultSet resultado = null;
		List<Usuario> lista = new ArrayList<Usuario>();
		
		try {
			conexao = ConnectionFactory.getConnection();		
			comando = conexao.prepareStatement(sql.toString());
		
			if(nome != null){
				comando.setString(1, "%"+nome+"%");
			}		
			
			resultado = comando.executeQuery();
	
			while (resultado.next()) {
				Usuario usuario = new Usuario();
				usuario.setNro(resultado.getLong("NRO"));
				usuario.setNome(resultado.getString("NOME_COMPLETO"));
				usuario.setCpf(resultado.getString("CPF"));
				usuario.setCargo(resultado.getString("CARGO"));
				usuario.setUsuario(resultado.getString("USUARIO"));
				usuario.setSenha(resultado.getString("SENHA"));
				usuario.setFlgAtivo(resultado.getString("FLG_ATIVO") == "S" ? Boolean.TRUE : Boolean.FALSE);
				usuario.setDataBaixa(resultado.getDate("DATA_BAIXA"));
				usuario.setDataInclusao(resultado.getDate("DATA_INC"));
	
				lista.add(usuario);
			}
		
		} catch (SQLException ex) {
			throw new PSTException("Ocorreu um erro ao tentar obter a listagem de usuarios", ex);
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
		
		try {
			UsuarioDAOImpl dao = new UsuarioDAOImpl();
			
			//Usuario usuario = new Usuario();
			
//			//usuario.setNro(1L);
//			usuario.setNome("Christian das menininhas");
//			usuario.setCpf("24242424242424");
//			usuario.setCargo("fungir ser homem");
//			usuario.setUsuario("formen");
//			usuario.setSenha("123mudar");
//			usuario.setFlgAtivo(Boolean.TRUE);
//			
//			usuario.setDataBaixa(new java.util.Date());
			
			List<Usuario> lista = dao.listar(0, 0, null);
			
			for (Usuario usuario : lista) {
				System.out.println("Nome: "+usuario.getNome());
				System.out.println("Data: "+usuario.getDataBaixa());
			}
			
			
		} catch (PSTException e) {
			System.out.println("ERRO: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
}
