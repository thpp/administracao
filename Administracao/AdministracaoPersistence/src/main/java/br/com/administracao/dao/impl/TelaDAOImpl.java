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
				tela.setListaFuncoes(listaFuncoes(tela, conexao));
				
				lista.add(tela);
				}
			
			} catch (SQLException ex) {
				throw new PSTException("Ocorreu um erro ao tentar obter a listagem de tela", ex);
			} finally {
				PSTUtil.fechar(resultado);
				PSTUtil.fechar(comando);
				PSTUtil.fechar(conexao);
			}
		
		return lista;
	}
	
	public List<Funcoes> listaFuncoes(Tela tela, Connection conexao) throws PSTException{
				
		StringBuilder sqlFuncao = new StringBuilder();
		sqlFuncao.append("select f.ACAO_NRO acaoF, f.TELA_NRO telaF, ac.NRO nroAC, ac.NOME nomeAC  ");
		sqlFuncao.append("from s_funcoes f, s_acoes ac ");
		sqlFuncao.append("where ac.NRO = f.ACAO_NRO and f.TELA_NRO = ? ");
		
		ResultSet resultado = null;
		PreparedStatement comando = null;
		
		List<Funcoes> lista = new ArrayList<Funcoes>();
		
		try{
			
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
	public List<Tela> listar(Projeto projetoBusca, Modulo modulo, String nome) throws PSTException {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.nro nroT, t.nome nomeT, t.mod_nro nroM, m.nome nomeM, m.proj_nro nroP, p.NOME nomeP ");
		sql.append("FROM s_telas t, s_modulo m, s_projeto p  ");
		sql.append("where t.mod_nro = m.nro and m.PROJ_NRO = p.NRO ");
		
		if(projetoBusca != null){
			sql.append("and p.NRO = ? ");
		}
		
		if(modulo != null){
			sql.append("and m.NRO = ? ");
		}
		
		if(nome != null){
			sql.append("and t.nome like ? ");
		}
		
		Connection conexao = null;
		PreparedStatement comando = null;		
		ResultSet resultado = null;	
		List<Tela> lista = new ArrayList<Tela>();
		
		try {
			conexao = ConnectionFactory.getConnection();			
			comando = conexao.prepareStatement(sql.toString());			
			
			if(projetoBusca != null && modulo == null){
				comando.setLong(1, projetoBusca.getNro());
			}else if(projetoBusca != null && modulo != null){
				comando.setLong(1, projetoBusca.getNro());
				comando.setLong(2, modulo.getNro());
			}
			
			if(projetoBusca != null && modulo == null){
				
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
				tela.setListaFuncoes(listaFuncoes(tela, conexao));
				
				lista.add(tela);
				}
			
			} catch (SQLException ex) {
				throw new PSTException("Ocorreu um erro ao tentar obter a listagem de tela", ex);
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
			List<Tela> lista = dao.listar(0, 0);			
			for (Tela tela : lista) {
				System.out.println("Tela nro: "+tela.getNro());
				System.out.println("Tela nome: "+tela.getNome());
				System.out.println("Modelo: "+tela.getModulo().getNome());
				System.out.println("Projeto: "+tela.getModulo().getProjeto().getNome());
				System.out.println("Funções");
				
				for(Funcoes f : tela.getListaFuncoes()){
					System.out.println(f.getAcoes().getNome());
				}
				
				System.out.println(".........................");
				System.out.println("");
				
			}
			
			
		} catch (PSTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRO: "+e.getMessage());
		}
	}

}
