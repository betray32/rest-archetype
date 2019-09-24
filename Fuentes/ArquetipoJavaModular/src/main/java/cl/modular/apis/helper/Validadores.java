package cl.modular.apis.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cl.modular.apis.bean.DtoRequestDatosContextoHttp;

/**
 * Valida parametros de entrada generales
 * 
 * @author ccontrerasc
 *
 */
public class Validadores {

	/**
	 * LOG
	 */
	private static final Logger log = Logger.getLogger(Validadores.class);
	
	/**
	 * Validar rut chileno
	 * 
	 * @param rut
	 * @return
	 */
	public static boolean validarRut(String rut) {

		boolean validacion = false;
		try {
			rut = rut.toUpperCase();
			rut = rut.replace(".", "");
			rut = rut.replace("-", "");
			int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

			char dv = rut.charAt(rut.length() - 1);

			int m = 0, s = 1;
			for (; rutAux != 0; rutAux /= 10) {
				s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
			}
			if (dv == (char) (s != 0 ? s + 47 : 75)) {
				validacion = true;
			}

		} catch (java.lang.NumberFormatException e) {
			log.error("Rut ingresado incorrecto > " + rut);
		} catch (Exception e) {
			log.error("Rut ingresado incorrecto > " + rut);
		}
		return validacion;
	}

	/**
	 * Validar los parametros de entrada en el caso de que sea el rut
	 * 
	 * @param campo1
	 * @return
	 */
	public static boolean parametrosEntradaIncorrectosUnCampo(String campo1) {
		if (campo1 == null || "".equals(campo1)) {
			return true;
		}

		return false;
	}

	/**
	 * Permite validar los campos del contexto, deben de ser todos no nulos para
	 * poder estar OK
	 * 
	 * @return
	 */
	public static boolean validarCamposContexto(DtoRequestDatosContextoHttp metadata) {

		boolean res = true;

		if (metadata == null) {
			log.error("Objeto metadata nulo");
			res = false;
		} else {

			if (metadata.getModalidad() != null) {

				if (!"".equals(metadata.getModalidad())) {

					// CONSULTAR
					if ("consultar".equalsIgnoreCase(metadata.getModalidad())) {

						log.info("Modalidad CONSULTAR");

						// En consultar se agregan los campos especiales
						if (StringUtils.isBlank(metadata.getToken())) {
							log.error("Token nulo o invalido");
							res = false;
						}

						if (StringUtils.isBlank(metadata.getClientId())) {
							log.error("ClientID nulo o invalido");
							res = false;
						}

						if (StringUtils.isBlank(metadata.getRequestID())) {
							log.error("RequestID nulo o invalido");
							res = false;
						}

						res = validacionMetadata(metadata, res);

					} else {
						log.error("La modalidad no corresponde a CONSULTAR");
						res = false;
					}
				} else {
					log.error("Modalidad vacia");
					res = false;
				}

			} else {
				log.error("Modalidad nula");
				res = false;
			}

		}

		return res;
	}

	/**
	 * @param metadata
	 * @param res
	 * @return
	 */
	private static boolean validacionMetadata(DtoRequestDatosContextoHttp metadata, boolean res) {
		if (StringUtils.isBlank(metadata.getCodigoAplicacion())) {
			log.error("COD APLICACION nulo o invalido");
			res = false;
		}

		if (StringUtils.isBlank(metadata.getCodigoCanal())) {
			log.error("COD CANAL nulo o invalido");
			res = false;
		}

		if (StringUtils.isBlank(metadata.getEmpresaAplicacion())) {
			log.error("EMPRESA APLICACION nulo o invalido");
			res = false;
		}

		if (StringUtils.isBlank(metadata.getIpCliente())) {
			log.error("IP CLIENTE nulo o invalido");
			res = false;
		}

		return res;
	}

}
