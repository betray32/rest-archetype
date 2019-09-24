package cl.modular.apis.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Respuesta para cuando se consulta el token
 * 
 * @author Camilo Contreras
 *
 */
public class RespuestaConsulta {

	private String mensaje;
	
	@JsonProperty("minutosRestantesParaExpirar")
	private long minutosFaltantes;

	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	/** GET Y SET **/
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@JsonGetter("minutosRestantesParaExpirar")
	public long getMinutosFaltantes() {
		return minutosFaltantes;
	}

	public void setMinutosFaltantes(long minutosFaltantes) {
		this.minutosFaltantes = minutosFaltantes;
	}

}
