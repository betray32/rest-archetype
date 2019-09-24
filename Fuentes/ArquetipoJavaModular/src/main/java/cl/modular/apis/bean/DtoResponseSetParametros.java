package cl.modular.apis.bean;

/**
 * Set parametros
 * 
 * @author Camilo Contreras
 *
 */
public class DtoResponseSetParametros {

	/**
	 * Codigos de error
	 */
	private String codigoError;
	private String msjError;

	/** GET Y SET **/
	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public String getMsjError() {
		return msjError;
	}

	public void setMsjError(String msjError) {
		this.msjError = msjError;
	}
}
