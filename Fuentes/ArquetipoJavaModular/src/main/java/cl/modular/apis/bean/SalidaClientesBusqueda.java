/* Copyright 2017 freecodeformat.com */
package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Salida general para los clientes
 * 
 * @author ccontrerasc
 *
 */
public class SalidaClientesBusqueda {

	@JsonProperty("DtoResponseCodigosEstadoHttp")
	private DtoResponseCodigosEstadoHttp DtoResponseCodigosEstadoHttp;

	@JsonProperty("dtoResponseSetResultados")
	private DtoResponseSetResultadosResumenClientes dtoresponsesetresultados;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET **/
	public void setDtoResponseCodigosEstadoHttp(DtoResponseCodigosEstadoHttp DtoResponseCodigosEstadoHttp) {
		this.DtoResponseCodigosEstadoHttp = DtoResponseCodigosEstadoHttp;
	}

	public DtoResponseCodigosEstadoHttp getDtoResponseCodigosEstadoHttp() {
		return DtoResponseCodigosEstadoHttp;
	}

	public void setDtoresponsesetresultados(DtoResponseSetResultadosResumenClientes dtoresponsesetresultados) {
		this.dtoresponsesetresultados = dtoresponsesetresultados;
	}

	public DtoResponseSetResultadosResumenClientes getDtoresponsesetresultados() {
		return dtoresponsesetresultados;
	}

}