package br.com.administracao.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
	
	private static Logger logger = Logger.getLogger(ProjetoDAOImpl.class.getName());

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
			
			System.out.println("Ultimo id inserido: "+ultimoId);
			
			comando = conexao.prepareStatement(sqlFuncao.toString());
			
			for (Funcoes funcoes : tela.getListaFuncoes()) {
				comando.setLong(1, funcoes.getAcoes().getNro());
				comando.setLong(2, ultimoId);
				
				comando.executeUpdate();				
			}
			
			conexao.commit();
			
			logger.info("Tela inserido com sucesso");
		} catch (SQLException ex) {
			try {
				conexao.rollback();
			} catch (SQLException e) {
				throw new PSTException("Ocorreu um erro ao tentar dar rollback em tela "+ex.getCause(), ex);
			}
			throw new PSTException("Ocorreu um erro ao tentar inserir um tela "+ex.getCause(), ex);
		} finally {
			PSTUtil.fechar(call);
			PSTUtil.fechar(conexao);
			PSTUtil.fechar(comando);
		}		
	}

	@Override
	public void editar(Tela tela) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Long codigo) throws PSTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tela> listar(int primeiro, int tamanho) throws PSTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tela> listar(String nome) throws PSTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int contar() throws PSTException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public static void main(String[] args) {
		
		TelaDAOImpl dao = new TelaDAOImpl();
		Tela t = new Tela();
		Modulo m = new Modulo();
		Projeto p = new Projeto();
		
		Acoes ac1 = new Acoes();		
		ac1.setNro(1L);
		
		Acoes ac2 = new Acoes();		
		ac2.setNro(2L);
		
		Funcoes f1 = new Funcoes();
		Funcoes f2 = new Funcoes();
		
		f1.setAcoes(ac1);
		f2.setAcoes(ac2);
		
		List<Funcoes> lista = new  ArrayList<Funcoes>();
		
		lista.add(f1);
		lista.add(f2);
		
		p.setNro(15L);
		m.setNro(9L);
		m.setProjeto(p);
		
		
		t.setNome("Teste ID");
		t.setModulo(m);
		t.setListaFuncoes(lista);
		
		try {
			dao.inserir(t);
			System.out.println("Salvo!!");
		} catch (PSTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRO: "+e.getMessage());
		}
	}

}
