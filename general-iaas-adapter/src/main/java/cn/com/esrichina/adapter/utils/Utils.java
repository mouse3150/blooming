package cn.com.esrichina.adapter.utils;

import cn.com.esrichina.adapter.AdapterException;
import cn.com.esrichina.adapter.AdapterRuntimeException;

/**
 * 
 * @author Esri
 *
 */
public final class Utils {

	private Utils() {
	};

	public static void propageteAdapterException(Throwable cause, String message) throws AdapterException {
		throw new AdapterException(message, cause);
	}

	public static void propageteAdapterException(String message) throws AdapterException {
		throw new AdapterException(message);
	}

	public static void propageteAdapterException() throws AdapterException {
		throw new AdapterException();
	}

	public static void propageteException(String message, Throwable casue) {
		throw new AdapterRuntimeException(message, casue);
	}

	public static void propageteException(Throwable casue) {
		throw new AdapterRuntimeException(casue);
	}

	public static void propageteException(String message) {
		throw new AdapterRuntimeException(message);
	}

	/**
	 * Check the <code>value</code>, if the value is null or empty, throw a
	 * {@link AdapterException}.
	 * 
	 * @param value
	 * @param message
	 * @return the <code>value</code>
	 * @throws AdapterException
	 */
	public static String checkNotNullAdapterException(String value, String message) throws AdapterException {
		if (value == null || value.length() <= 0) {
			propageteAdapterException(message);
		}
		return value;
	}

	/**
	 * Check the <code>value</code>, if the value is null or empty, throw a
	 * {@link AdapterException}.
	 * 
	 * @param value
	 * @param message
	 * @return the <code>value</code>
	 * @throws AdapterException
	 */
	public static String checkNotNullAdapterException(String value) throws AdapterException {
		if (value == null || value.length() <= 0) {
			propageteAdapterException("Value can not be null");
		}
		return value;
	}

	/**
	 * Check the <code>value</code>, if the value is null or empty, throw a
	 * {@link RuntimeException}.
	 * 
	 * @param value
	 * @param message
	 * @return the <code>value</code>
	 */
	public static String checkNotNull(String value, String message) {
		if (value == null || value.length() <= 0) {
			throw new RuntimeException(message);
		}
		return value;
	}

	/**
	 * Check the <code>value</code>, if the value is null or empty, throw a
	 * {@link RuntimeException}.
	 * 
	 * @param value
	 * @return the <code>value</code>
	 */
	public static String checkNotNull(String value) {
		return checkNotNull(value, "Value can not be null");
	}

	/**
	 * Check the <code>Object</code>, if the value is null or empty, throw a
	 * {@link RuntimeException}.
	 * 
	 * @param value
	 * @param message
	 * @return the <code>Object</code>
	 */
	public static Object checkNotNull(Object obj, String message) {
		if (obj == null) {
			throw new RuntimeException(message);
		}
		return obj;
	}

	/**
	 * Check the <code>Object</code>, if the value is null or empty, throw a
	 * {@link RuntimeException}.
	 * 
	 * @param value
	 * @param message
	 * @return the <code>Object</code>
	 */
	public static Object checkNotNull(Object obj) {
		return checkNotNull(obj, "Object can not be null");
	}

}
