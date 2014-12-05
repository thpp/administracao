package br.com.administracao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
			
			incluiPermicoes(acesso.getListaPermicoes(), conexao);
			
			conexao.commit();
			
			logger.info("Acesso inserido com sucesso");

		} catch (SQLException ex) {

			if (ex.getMessage().contains("ORA-00001")) {
				throw new PSTException("O acesso a essa tela já foi atribuído a esse usuário"+ ex.getMessage(), ex);
			} else {
				throw new PSTException("Ocorreu um erro ao tentar inserir um acesso "+ ex.getMessage(), ex);
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
		// TODO Auto-generated method stub
		return null;
	}

	private void incluiPermicoes(List<Permissoes> listaPermicoes, Connection conexao) throws PSTException {

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO S_PERMISSOES ");
		sql.append("(ACES_TELA_NRO, ACES_USU_NRO, FUNCAO_ACAO_NRO, FUNCAO_TELA_NRO) ");
		sql.append("VALUES (?, ?, ?, ?) ");

		PreparedStatement comando = null;		

			try {
				
				comando = conexao.prepareStatement(sql.toString());
				
				for (Permissoes permissoes : listaPermicoes) {
				
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
					throw new PSTException("Ocorreu um erro no rollback de permissões "+ ex.getMessage(), ex);
				}

				if (ex.getMessage().contains("ORA-00001")) {
					throw new PSTException("O acesso a essa tela já foi atribuído a esse usuário"+ ex.getMessage(), ex);
				} else {
					throw new PSTException("Ocorreu um erro ao tentar inserir um acesso "+ ex.getMessage(), ex);
				}

			} finally {
				PSTUtil.fechar(comando);
			}		
	}

	public static void main(String[] args) {

		AcessoDAOImpl dao = new AcessoDAOImpl();

		Usuario usuario = new Usuario();
		Tela tela = new Tela();
		Acesso acesso = new Acesso();
		List<Permissoes> listaPermissoes = new ArrayList<Permissoes>();
		
		Funcoes f1 = new Funcoes();
		Funcoes f2 = new Funcoes();
		Funcoes f3 = new Funcoes();
		
		Acoes ac1 = new Acoes();
		Acoes ac2 = new Acoes();
		Acoes ac3 = new Acoes();
		
		Permissoes p1 = new Permissoes();
		Permissoes p2 = new Permissoes();
		Permissoes p3 = new Permissoes();
		
		ac1.setNro(1L);
		ac2.setNro(2L);
		ac3.setNro(3L);
		
		usuario.setNro(1L);
		tela.setNro(3L);
		acesso.setTela(tela);
		acesso.setUsuario(usuario);
		
		f1.setTela(tela);
		f2.setTela(tela);
		f3.setTela(tela);
		
		f1.setAcoes(ac1);
		f2.setAcoes(ac2);
		f3.setAcoes(ac3);
		
		p1.setAcesso(acesso);
		p1.setFuncoes(f1);
		
		p2.setAcesso(acesso);
		p2.setFuncoes(f2);
		
		p3.setAcesso(acesso);
		p3.setFuncoes(f3);							
		
		listaPermissoes.add(p1);
		listaPermissoes.add(p2);
		listaPermissoes.add(p3);
		
		acesso.setListaPermicoes(listaPermissoes);

		try {
			dao.inserir(acesso);
			System.out.println("Sucesso!...");
		} catch (PSTException e) {
			System.out.println("ERRO.... " + e.getMessage());
			e.printStackTrace();
		}

	}

}
