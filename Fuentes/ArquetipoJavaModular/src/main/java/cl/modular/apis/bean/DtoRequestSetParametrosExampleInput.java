package cl.modular.apis.bean;

import java.io.Serializable;

/**
 * Entrada de ejemplo para el DTO ExampleInput
 * 
 * @author Camilo Contreras
 *
 */
public class DtoRequestSetParametrosExampleInput implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	private String rutCliente;
	private String numCuenta;
	private int cicloFacturacion;

	/** GET Y SET **/
	public String getRutCliente() {
		return rutCliente;
	}

	public void setRutCliente(String rutCliente) {
		this.rutCliente = rutCliente;
	}

	public String getNumCuenta() {
		return numCuenta;
	}

	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}

	public int getCicloFacturacion() {
		return cicloFacturacion;
	}

	public void setCicloFacturacion(int cicloFacturacion) {
		this.cicloFacturacion = cicloFacturacion;
	}

}
