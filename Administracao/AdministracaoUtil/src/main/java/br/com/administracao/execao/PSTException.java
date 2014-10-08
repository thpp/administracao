package br.com.administracao.execao;

@SuppressWarnings("serial")
public class PSTException extends Exception {

	public PSTException() {
		super();
	}

	public PSTException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PSTException(String message, Throwable cause) {
		super(message, cause);
	}

	public PSTException(String message) {
		super(message);
	}

	public PSTException(Throwable cause) {
		super(cause);
	}
}
