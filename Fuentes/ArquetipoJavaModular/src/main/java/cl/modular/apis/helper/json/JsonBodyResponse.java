package cl.modular.apis.helper.json;

import cl.modular.apis.bean.DtoResponseSetParametros;
import cl.modular.apis.helper.Constantes;

/**
 * Salidas del body de json
 * 
 * @author Camilo Contreras
 *
 */
public class JsonBodyResponse {

	/**
	 * Bad request
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros faltanParametrosEntrada() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("-1");
		res.setMsjError("Parametros de entrada incompletos o incorrectos");

		return res;

	}
	
	/**
	 * OK
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros okTransaccion() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("0");
		res.setMsjError("Operacion realizada exitosamente");

		return res;

	}

	/**
	 * Error desconocido
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros errorDesconocido() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("301");
		res.setMsjError("Ha ocurrido un error inesperado en la ejecucion");

		return res;

	}

	/**
	 * Error conectividad
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros errorConectividad() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("201");
		res.setMsjError("Error de conectividad con el servicio");

		return res;

	}
	
	/**
	 * Sin data salida
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros sinAutorizacionMetadata() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("104");
		res.setMsjError("METADATA NO AUTORIZADA");

		return res;

	}

	/**
	 * Sin data salida
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros sinDataSalida() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("105");
		res.setMsjError("No existen registros para los datos consultados");

		return res;

	}

	/**
	 * metadata token invalida
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros metadataTokenInvalida() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("106");
		res.setMsjError("Metadata de token enviada INVALIDA");

		return res;

	}

	/**
	 * Rut invalido
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros rutInvalido() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("107");
		res.setMsjError("Rut invalido");

		return res;

	}

	/**
	 * Headers invalidos
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros tokenHeadersInvalidos() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("103");
		res.setMsjError(Constantes.RES_ERROR_HEADER);

		return res;

	}

	/**
	 * Cliente no reconocido
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros clienteNoReconocido() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("102");
		res.setMsjError("Cliente no reconocido");
		return res;

	}

	/**
	 * Cliente no reconocido
	 * 
	 * @return
	 */
	public static DtoResponseSetParametros tokenInvalido() {

		DtoResponseSetParametros res = new DtoResponseSetParametros();

		res.setCodigoError("101");
		res.setMsjError(Constantes.RES_ERROR_TOKEN);

		return res;

	}

}
