package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resumen de productos, detalle
 * 
 * @author ccontrerasc
 *
 */
public class DtoResponseSetResultadosResumenProductos {

	@JsonProperty("dtoCliente")
	private DtoClienteResumenProductos dtocliente;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET */
	public DtoClienteResumenProductos getDtocliente() {
		return dtocliente;
	}

	public void setDtocliente(DtoClienteResumenProductos dtocliente) {
		this.dtocliente = dtocliente;
	}

}
