package cl.modular.apis.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entrada para el servicio de Token en consultar
 * 
 * @author Camilo Contreras
 *
 */
public class InputTokenWS {

	@JsonProperty("dtoRequestDatosContextoHttp")
	private DtoRequestDatosContextoHttp requestDatosContextoHttp;

	/** GET Y SET **/
	public DtoRequestDatosContextoHttp getRequestDatosContextoHttp() {
		return requestDatosContextoHttp;
	}

	public void setRequestDatosContextoHttp(DtoRequestDatosContextoHttp requestDatosContextoHttp) {
		this.requestDatosContextoHttp = requestDatosContextoHttp;
	}
}
