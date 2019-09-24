package cl.modular.apis.res;

/**
 * Clase de error que se genera cuando los properties no fueron inicializados
 * 
 * @author Camilo Contreras
 * @version 2017.11.23
 *
 */
public class NotInitResourceException extends RuntimeException {
	/**
	 * Constructor
	 */
	public NotInitResourceException() {
		super();
	}

	/**
	 * Error al iniciar resources
	 * 
	 * @param message
	 */
	public NotInitResourceException(String message) {
		super(message);
	}

	/**
	 * Error al iniciar resources
	 * 
	 * @param message
	 * @param e
	 */
	public NotInitResourceException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
