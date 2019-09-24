/* Copyright 2017 freecodeformat.com */
package cl.modular.apis.bean;

/**
 * Salidas para los servicios
 * 
 * @author ccontrerasc
 *
 */
public class DtoResponseCodigosEstadoHttp {

	/**
	 * Campos
	 */
	private String codigo;
	private String mensaje;
	private String descripcion;

	/** GET Y SET **/
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

}