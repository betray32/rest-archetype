/* Copyright 2017 freecodeformat.com */
package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Salida para Resumen Productos
 * 
 * @author ccontrerasc
 *
 */
public class SalidaResumenProductos {

	@JsonProperty("dtoResponseCodigosEstadoHttp")
	private DtoResponseCodigosEstadoHttp dtoresponsecodigosestadohttp;

	@JsonProperty("dtoResponseSetResultados")
	private DtoResponseSetResultadosResumenProductos dtoresponsesetresultados;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET **/
	public DtoResponseCodigosEstadoHttp getDtoresponsecodigosestadohttp() {
		return dtoresponsecodigosestadohttp;
	}

	public void setDtoresponsecodigosestadohttp(DtoResponseCodigosEstadoHttp dtoresponsecodigosestadohttp) {
		this.dtoresponsecodigosestadohttp = dtoresponsecodigosestadohttp;
	}

	public DtoResponseSetResultadosResumenProductos getDtoresponsesetresultados() {
		return dtoresponsesetresultados;
	}

	public void setDtoresponsesetresultados(DtoResponseSetResultadosResumenProductos dtoresponsesetresultados) {
		this.dtoresponsesetresultados = dtoresponsesetresultados;
	}

}