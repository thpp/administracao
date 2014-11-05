package br.com.administracao.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PSTUtil {
	private static Logger logger = Logger.getLogger(PSTUtil.class.getName());

	public static void fechar(Connection conexao) {
		if (conexao != null) {
			try {
				conexao.close();
				logger.info("Conexão encerrada com sucesso");
			} catch (SQLException ex) {
				logger.warning("Ocorreu um erro ao tentar fechar uma conexão");
			}
		}
	}

	public static void fechar(PreparedStatement comando) {
		if (comando != null) {
			try {
				comando.close();
				logger.info("PreparedStatement encerrado com sucesso");
			} catch (SQLException ex) {
				logger.warning("Ocorreu um erro ao tentar liberar um recurso do tipo PreparedStatement");
			}
		}
	}
	
	public static void fechar(CallableStatement comando) {
		if (comando != null) {
			try {
				comando.close();
				logger.info("CallableStatement encerrado com sucesso");
			} catch (SQLException ex) {
				logger.warning("Ocorreu um erro ao tentar liberar um recurso do tipo CallableStatement");
			}
		}
	}

	public static void fechar(ResultSet resultado) {
		if (resultado != null) {
			try {
				resultado.close();
				logger.info("ResultSet encerrado com sucesso");
			} catch (SQLException ex) {
				logger.warning("Ocorreu um erro ao tentar liberar um recurso do tipo ResultSet");
			}
		}
	}
}
