package cn.com.esrichina.adapter;

/**
 * IaaS Adapter
 * 
 * @author Esri
 *
 */

public class AdapterException extends Exception {

	private static final long serialVersionUID = -6371427172486913862L;

	public AdapterException() {
	}

	public AdapterException(String message) {
		super(message);
	}

	public AdapterException(Throwable cause) {
		super(cause);
	}

	public AdapterException(String message, Throwable cause) {
		super(message, cause);
	}

}
