package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import br.com.administracao.dao.interf.UsuarioDAO;
import br.com.administracao.execao.PSTException;
import br.com.administracao.factory.ConnectionFactory;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Usuario> listar(int primeiro, int tamanho, String nome)
			throws PSTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int contar() throws PSTException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args) {			
		
		try {
			UsuarioDAOImpl dao = new UsuarioDAOImpl();
			
			Usuario usuario = new Usuario();
			
			usuario.setNro(1L);
			usuario.setNome("Thiago");
			usuario.setCpf("36995369807");
			usuario.setCargo("Programador");
			usuario.setUsuario("thiagohpp");
			usuario.setSenha("123mudar");
			usuario.setFlgAtivo(Boolean.TRUE);
			
			//usuario.setDataBaixa(new java.util.Date());
			
			dao.editar(usuario);
		} catch (PSTException e) {
			System.out.println("ERRO: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
}
