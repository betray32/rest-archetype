/* Copyright 2017 freecodeformat.com */
package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO CLIENTE
 * 
 * @author ccontrerasc
 *
 */
public class DtoClienteBuscarResumenClientes {

	@JsonProperty("Rut")
	private String rut;

	@JsonProperty("Nombre")
	private String nombre;

	@JsonProperty("TP")
	private String tp;

	@JsonProperty("Ingreso")
	private String ingreso;

	@JsonProperty("Banca")
	private String banca;

	@JsonProperty("Ejecutivo")
	private String ejecutivo;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET **/
	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getRut() {
		return rut;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}

	public String getTp() {
		return tp;
	}

	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	public String getIngreso() {
		return ingreso;
	}

	public void setBanca(String banca) {
		this.banca = banca;
	}

	public String getBanca() {
		return banca;
	}

	public void setEjecutivo(String ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

	public String getEjecutivo() {
		return ejecutivo;
	}

}