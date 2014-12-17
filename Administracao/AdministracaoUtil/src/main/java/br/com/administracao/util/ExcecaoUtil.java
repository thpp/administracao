package br.com.administracao.util;

public class ExcecaoUtil {
	public static Throwable getCause(Exception ex) {
		if (ex == null) {
			return null;
		}

		Throwable cause = ex;

		while (cause.getCause() != null) {
			cause = cause.getCause();
		}

		return cause;
	}
}
