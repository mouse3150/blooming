package cn.com.esrichina.adapter;

/**
 * 
 * @author Esri
 *
 */
public class AdapterRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3051003063423999643L;

	public AdapterRuntimeException() {

	}

	public AdapterRuntimeException(String message) {
		super(message);
	}

	public AdapterRuntimeException(Throwable cause) {
		super(cause);
	}

	public AdapterRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
