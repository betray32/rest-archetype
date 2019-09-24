/* Copyright 2017 freecodeformat.com */
package cl.modular.apis.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parte del bean de salida para clientes
 * 
 * @author ccontrerasc
 *
 */
public class DtoResponseSetResultadosResumenClientes {

	@JsonProperty("dtoCliente")
	private List<DtoClienteBuscarResumenClientes> dtocliente;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setDtocliente(List<DtoClienteBuscarResumenClientes> dtocliente) {
		this.dtocliente = dtocliente;
	}

	public List<DtoClienteBuscarResumenClientes> getDtocliente() {
		return dtocliente;
	}

}