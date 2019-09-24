/* Copyright 2017 freecodeformat.com */
package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Detalle de la lista para el resumen de productos
 * 
 * @author ccontrerasc
 *
 */
public class DtoProductosContratados {

	@JsonProperty("MODULO")
	private String modulo;

	@JsonProperty("PRODUCTO ")
	private String producto;

	@JsonProperty("TIPO")
	private String tipo;

	@JsonProperty("OPERACION")
	private String operacion;

	@JsonProperty("TIPOPRODUCTO")
	private String tipoproducto;

	@JsonProperty("DESCRIPCION")
	private String descripcion;

	@JsonProperty("SALDO ")
	private String saldo;

	@JsonProperty("CUPO")
	private String cupo;

	@JsonProperty("DEUDA ")
	private String deuda;

	@JsonProperty("MONEDA")
	private String moneda;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET **/
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getModulo() {
		return modulo;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setTipoproducto(String tipoproducto) {
		this.tipoproducto = tipoproducto;
	}

	public String getTipoproducto() {
		return tipoproducto;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setCupo(String cupo) {
		this.cupo = cupo;
	}

	public String getCupo() {
		return cupo;
	}

	public void setDeuda(String deuda) {
		this.deuda = deuda;
	}

	public String getDeuda() {
		return deuda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMoneda() {
		return moneda;
	}

}