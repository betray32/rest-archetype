package cl.modular.apis.helper.json;

import cl.modular.apis.bean.DtoResponseCodigosEstadoHttp;

/**
 * Genera salidas de JSON para las cabeceras
 * 
 * @author ccontrerasc
 *
 */
public class JsonHeaderResponse {

	/**
	 * OK
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp requestOK() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("200");
		res.setMensaje("OK");
		res.setDescripcion("OK");

		return res;

	}

	/**
	 * Bad request
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp faltanParametrosEntrada() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("400");
		res.setMensaje("BAD REQUEST");
		res.setDescripcion("Parametros de entrada incompletos o incorrectos");

		return res;

	}
	
	/**
	 * TIMEOUT
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp timeOut() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("408");
		res.setMensaje("Request Timeout");
		res.setDescripcion("Timeout en request");

		return res;

	}
	
	/**
	 * NOT FOUND
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp notFound() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("404");
		res.setMensaje("Not Found");
		res.setDescripcion("Not Found");

		return res;

	}
	
	/**
	 * INTERNAL SERVER ERROR
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp internalServerError() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("500");
		res.setMensaje("ERROR");
		res.setDescripcion("Internal Server Error");

		return res;

	}
	
	/**
	 * NO CONTENT
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp sinContenidoSalida() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("204");
		res.setMensaje("NO CONTENT");
		res.setDescripcion("Sin contenido de salida");

		return res;

	}
	
	/**
	 * Unauthorized
	 * 
	 * @return
	 */
	public static DtoResponseCodigosEstadoHttp noAutorizada() {

		DtoResponseCodigosEstadoHttp res = new DtoResponseCodigosEstadoHttp();

		res.setCodigo("401");
		res.setMensaje("Unauthorized");
		res.setDescripcion("Solicitud no autorizada");

		return res;

	}

}
