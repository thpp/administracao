package br.com.administracao.execao;

@SuppressWarnings("serial")
public class WebException extends RuntimeException {

	public WebException() {
		super();
	}

	public WebException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WebException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(Throwable cause) {
		super(cause);
	}
}