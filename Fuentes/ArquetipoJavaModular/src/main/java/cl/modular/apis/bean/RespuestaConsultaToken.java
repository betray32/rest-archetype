package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Respuesta cuando se genera el token
 * 
 * @author Camilo Contreras
 *
 */
public class RespuestaConsultaToken {

	@JsonProperty("dtoResponseCodigosEstado")
	private DtoResponseCodigosEstadoHttp dtoresponsecodigosestadohttp;

	@JsonProperty("respuestaConsultaToken")
	private RespuestaConsulta respuesta;

	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET **/
	
	@JsonGetter("dtoResponseCodigosEstado")
	public DtoResponseCodigosEstadoHttp getDtoresponsecodigosestadohttp() {
		return dtoresponsecodigosestadohttp;
	}

	public void setDtoresponsecodigosestadohttp(DtoResponseCodigosEstadoHttp dtoresponsecodigosestadohttp) {
		this.dtoresponsecodigosestadohttp = dtoresponsecodigosestadohttp;
	}

	public RespuestaConsulta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(RespuestaConsulta respuesta) {
		this.respuesta = respuesta;
	}

}
