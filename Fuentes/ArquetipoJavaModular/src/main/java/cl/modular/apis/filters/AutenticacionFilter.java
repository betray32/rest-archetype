package cl.modular.apis.filters;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import cl.modular.apis.bean.DtoRequestDatosContextoHttp;
import cl.modular.apis.bean.DtoResponseCodigosEstadoHttp;
import cl.modular.apis.bean.SalidaGenerica;
import cl.modular.apis.helper.Constantes;
import cl.modular.apis.helper.Validadores;
import cl.modular.apis.helper.json.JsonHeaderResponse;
import cl.modular.apis.rest.client.TokenRestClient;

/**
 * Provider. Contiene un validador de "Token" de autorizacion existente en todos
 * los servicios. Exceptuando el servicio de Login en su funcionalidad de
 * "Autenticar"
 *
 * @author Pablo Carrasco
 * @version 1.0
 * @since 06-11-2017
 * 
 */
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AutenticacionFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest sr;

	/**
	 * LOG
	 */
	private static final Logger log = Logger.getLogger(AutenticacionFilter.class);

	/**
	 * Verifica el token de autorizacion proporcionado por el request
	 * {@requestContext ContainerRequestContext}, y responde con
	 * DTOResponseCodigosEstadoHttp si es un token invalido.
	 *
	 * @Param requestContext {@requestContext ContainerRequestContext} Contiene el
	 *        request del servicio.
	 * 
	 * @Return De ser valido el token, el validador deja proceder con el servicio de
	 *         origen. De lo contrario, responde con un DTOAdaptadorResponse
	 *         incluyendo un DTOResponseCodigosEstadoHttp correctamente formateado.
	 * 
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) {

		log.info("[VALIDAR AUTORIZACION DE LA APLICACION] Validando la cabecera del servicio y token enviado");

		String ip = sr.getRemoteAddr();
		log.info("IP del cliente: " + ip);

		DtoResponseCodigosEstadoHttp detalleCabeceraRespuesta = new DtoResponseCodigosEstadoHttp();

		if (!requestContext.getRequest().getMethod().equals("OPTIONS")) {

			Boolean tokenEsValido = false;

			DtoRequestDatosContextoHttp contexto = new DtoRequestDatosContextoHttp();

			contexto.setModalidad(requestContext.getHeaderString(Constantes.HEADER_MODALIDAD));

			// General
			contexto.setIpCliente(requestContext.getHeaderString(Constantes.HEADER_IP));
			contexto.setCodigoCanal(requestContext.getHeaderString(Constantes.HEADER_COD_CANAL));
			contexto.setCodigoAplicacion(requestContext.getHeaderString(Constantes.HEADER_COD_APLICACION));
			contexto.setEmpresaAplicacion(requestContext.getHeaderString(Constantes.HEADER_EMPRESA_APLICACION));
			
			// Consultar
			contexto.setClientId(requestContext.getHeaderString(Constantes.HEADER_CLIENT_ID));
			contexto.setRequestID(requestContext.getHeaderString(Constantes.HEADER_REQUEST_ID));
			contexto.setToken(requestContext.getHeaderString(Constantes.HEADER_TOKEN));

			log.info("[HEADERS]");
			log.info(new JSONObject(contexto).toString(1));

			// Validar los datos de entrada
			if (Validadores.validarCamposContexto(contexto)) {

				/*
				 * Si estan presentes los campos se llama al servicio rest para la consulta por
				 * el token
				 */
				TokenRestClient clienteRest = new TokenRestClient();
				if (clienteRest.consultarToken(contexto)) {

					tokenEsValido = true;
					log.info("[EL TOKEN ENVIADO ES VALIDO]");

				} else {

					// USUARIO NO AUTORIZADO
					detalleCabeceraRespuesta = JsonHeaderResponse.noAutorizada();

				}

			} else {
				detalleCabeceraRespuesta = JsonHeaderResponse.faltanParametrosEntrada();
			}

			if (!tokenEsValido) {

				log.error("Header invalido");

				// Se genera un Response con el codigo 401 - UNAUTHORIZED
				SalidaGenerica salida = new SalidaGenerica();

				salida.setDtoResponseCodigosEstadoHttp(detalleCabeceraRespuesta);

				log.info("[RESPONSE]");
				log.info(new JSONObject(salida).toString(1));

				Response response = Response.ok().entity(salida).type(MediaType.APPLICATION_JSON).build();

				// Se interrumpe el flujo del servicio original, para responder con el Response
				// generado.
				requestContext.abortWith(response);
			}

		}
	}

}
