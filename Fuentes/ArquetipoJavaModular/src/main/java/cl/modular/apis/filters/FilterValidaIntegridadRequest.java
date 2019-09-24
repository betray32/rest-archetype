package cl.modular.apis.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import cl.modular.apis.bean.SalidaGenerica;
import cl.modular.apis.delegate.DelegateValidacionMetadata;
import cl.modular.apis.helper.Constantes;
import cl.modular.apis.helper.Utiles;
import cl.modular.apis.helper.json.JsonBodyResponse;
import cl.modular.apis.helper.json.JsonHeaderResponse;

/**
 * Validador filter
 * 
 * @author Camilo Contreras
 *
 */
@Provider
@ValidaIntegridadRequest
@Priority(Priorities.AUTHORIZATION)
public class FilterValidaIntegridadRequest implements ContainerRequestFilter {

	/**
	 * LOG
	 */
	private static final Logger log = Logger.getLogger(FilterValidaIntegridadRequest.class);

	/**
	 * delegateValidacionMetadata
	 */
	private DelegateValidacionMetadata delegateValidacionMetadata;

	/**
	 * Nombre de la estructura raiz desde donde se obtendra el objeto a comparar
	 */
	private static final String RAIZ_OBJETO_CLAVE = "dtoRequestSetParametros";

	/**
	 * Validador de seguridad previa ejecucion del servicio
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		try {

			log.info("[VALIDAR METADATA] Se iniciara la validacion para la metadata de la solicitud");

			boolean solicitudValidada = false;
			String clientID = requestContext.getHeaderString("ClientID");
			log.info("El client id ingresado por header: " + clientID);

			/*
			 * Obtener la ruta completa de entrada
			 */
			UriInfo incomingEndpoint = requestContext.getUriInfo();
			log.info("URL A CONECTAR: " + incomingEndpoint.getPath());
			log.info("Transporte: " + requestContext.getHeaderString("User-Agent"));

			/*
			 * Tipo metodo
			 */
			String tipoMetodo = requestContext.getRequest().getMethod();
			log.info("METHOD: " + tipoMetodo);

			/*
			 * Input JSON
			 */
			String request = IOUtils.toString(requestContext.getEntityStream(), java.nio.charset.StandardCharsets.UTF_8.name());

			/*
			 * Dependiendo en el tipo de metodo del servicio tengo que ir a buscar el input
			 * rut desde diferentes entradas
			 */
			switch (tipoMetodo) {
			case "GET":

				solicitudValidada = evaluarGetMethod(requestContext, solicitudValidada, clientID);
				break;

			case "POST":

				log.info("POST REQUEST =  " + request);
				solicitudValidada = evaluarPostMethod(requestContext, solicitudValidada, clientID, incomingEndpoint, request);
				break;

			default:
				log.error("[ALERTA] Invocando servicio con metodo prohibido");
				solicitudValidada = false;
				break;
			}

			// Comprobar validez del request
			if (!solicitudValidada) {

				log.error("Metadata Invalida");

				// Se genera un Response con el codigo 401 - UNAUTHORIZED
				SalidaGenerica salida = new SalidaGenerica();

				salida.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.noAutorizada());
				salida.setDtoResponseSetParametros(JsonBodyResponse.sinAutorizacionMetadata());

				Response response = Response.ok().entity(salida).type(MediaType.APPLICATION_JSON).build();

				log.info("[RESPONSE]");
				log.info(new JSONObject(salida).toString(1));

				/*
				 * Se interrumpe el flujo del servicio original, para responder con el Response
				 * generado.
				 */

				requestContext.abortWith(response);
			} else {
				log.info("[METADATA CORRECTA]");
			}

			requestContext.setEntityStream(new ByteArrayInputStream(request.getBytes()));
			log.info("Filtro metadata finalizado");

		} catch (IOException e) {
			log.error("Ocurrio un error al leer el contenido de la metadata, ERROR > " + e.getMessage());
		}

	}

	/**
	 * Evalua la data cuando la llamada es por POST
	 * 
	 * @param requestContext
	 * @param solicitudValidada
	 * @param clientID
	 * @param incomingEndpoint
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private boolean evaluarPostMethod(ContainerRequestContext requestContext, boolean solicitudValidada, String clientID, UriInfo incomingEndpoint, String request) throws IOException {
		
		String rutCliente;
		
		/*
		 * Validar integridad de request
		 */
		if (Utiles.validarEntradaRequestPost(request)) {

			log.info("Request OK, contiene elementos validos");

			/*
			 * Validar si existe el rut y si es asi hacer la comparacion
			 */
			rutCliente = obtieneValorPost(requestContext, request, null, "rutCliente");
			solicitudValidada = validacionPost(requestContext, solicitudValidada, clientID, incomingEndpoint, rutCliente, request);

		} else {
			log.error("Request invalido");
			solicitudValidada = false;
		}
		return solicitudValidada;
	}

	/**
	 * Evalua la data cuando la llamada es por GET
	 * 
	 * @param requestContext
	 * @param solicitudValidada
	 * @param clientID
	 * @return
	 */
	private boolean evaluarGetMethod(ContainerRequestContext requestContext, boolean solicitudValidada, String clientID) {
		String rutCliente;
		MultivaluedMap<String, String> pathparam = requestContext.getUriInfo().getPathParameters();
		MultivaluedMap<String, String> queryParam = requestContext.getUriInfo().getQueryParameters();

		if (Utiles.validarEntradaRequestGet(pathparam, queryParam)) {
			log.info("Request OK, contiene elementos validos");

			rutCliente = obtieneRutClienteGet(requestContext, "rut");
			if (!StringUtils.isBlank(rutCliente) && rutCliente.equalsIgnoreCase(clientID)) {

				solicitudValidada = true;
				log.info("RUT validado entre header y Json POST");

			} else {
				log.info("No existe relacion entre PATH PARAM RUT y Header");
			}

		} else {
			log.error("Request invalido");
			solicitudValidada = false;
		}
		return solicitudValidada;
	}

	/**
	 * Validacion de rut
	 * 
	 * @param solicitudValidada
	 * @param clientID
	 * @param rutCliente
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean validacionRut(boolean solicitudValidada, String clientID, String rutCliente) {

		log.info("El rut por header es: " + clientID + " , El rut de request es: " + rutCliente);

		if (!StringUtils.isBlank(rutCliente) && rutCliente.equalsIgnoreCase(clientID)) {
			solicitudValidada = true;
		}

		return solicitudValidada;
	}

	/**
	 * Permite validar cuando es POST
	 * 
	 * @param requestContext
	 * @param solicitudValidada
	 * @param clientID
	 * @param incomingEndpoint
	 * @param rutCliente
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private boolean validacionPost(ContainerRequestContext requestContext, boolean solicitudValidada, String clientID, UriInfo incomingEndpoint, String rutCliente, String request) throws IOException {

		if (!StringUtils.isBlank(rutCliente) && rutCliente.equalsIgnoreCase(clientID)) {

			solicitudValidada = true;
			log.info("RUT validado entre header y Json POST");

		} else {

			log.info("No existe relacion entre RUT y Json Post");

			if (StringUtils.isBlank(rutCliente)) {

				log.info("Al no estar presente el rut en Json se procedera a validar por numero de producto...");
				solicitudValidada = validadorPorPathUrl(requestContext, solicitudValidada, clientID, incomingEndpoint, request);
			}

		}

		return solicitudValidada;

	}

	/**
	 * Permite validar por PATH
	 * 
	 * @param requestContext
	 * @param solicitudValidada
	 * @param clientID
	 * @param incomingEndpoint
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private boolean validadorPorPathUrl(ContainerRequestContext requestContext, boolean solicitudValidada, String clientID, UriInfo incomingEndpoint, String request) throws IOException {

		String url = Utiles.obtenerUrlLimpia(incomingEndpoint.getPath());

		/*
		 * Caso contrario se busca dentro de algun numero de cuenta para comparar en el
		 * SP
		 */
		switch (url) {

		/*
		 * Permite validar la data en contraste al SP, se obtienen los objetos
		 * dependiendo de la estructura de entrada del mismo
		 */
		case "/Cliente/ConsultarDataSensibleEjemploFiltro":

			log.info("El path ingresado es: " + incomingEndpoint.getPath() + "");
			log.info("El endpoint solicitado debe validar la correlacion RUT-CUENTA");
			log.info("Data entrada");

			String cuenta = obtieneValorPost(requestContext, request, null, "numCuenta");

			log.info("Rut Header: " + clientID);
			log.info("Cuenta: " + cuenta);

			delegateValidacionMetadata = new DelegateValidacionMetadata();
			solicitudValidada = delegateValidacionMetadata.validacionMetadataFiltro(clientID, cuenta, Constantes.CODIGOMETADATAFILTROCUENTA);

			break;

		default:

			log.error("URL NO IDENTIFICADA");
			break;

		}

		return solicitudValidada;
	}

	/**
	 * Obtiene desde la URL el valor del parametro enviado como entrada al metodo
	 * 
	 * @param requestContext
	 * @param nombreParametro
	 * @return
	 */
	private String obtieneRutClienteGet(ContainerRequestContext requestContext, String nombreParametro) {
		MultivaluedMap<String, String> pathparam = requestContext.getUriInfo().getPathParameters();
		String rutClienteURL = pathparam.get(nombreParametro).get(0);
		return rutClienteURL;
	}

	/**
	 * Obtiene desde el body el valor del parametro enviado como entrada al metodo
	 * 
	 * @param requestContext
	 * @param request
	 * @param nombreParametro
	 * @return
	 * @throws IOException
	 */
	private String obtieneValorPost(ContainerRequestContext requestContext, String request, String objHijoRequesParametro, String nombreParametro) throws IOException {
		String rutClienteBody = "";
		try {
			JSONObject obj = new JSONObject(request);
			if (!StringUtils.isBlank(objHijoRequesParametro)) {
				rutClienteBody = obj.getJSONObject(RAIZ_OBJETO_CLAVE).getJSONObject(objHijoRequesParametro).getString(nombreParametro);
			} else {
				rutClienteBody = obj.getJSONObject(RAIZ_OBJETO_CLAVE).getString(nombreParametro);
			}
		} catch (JSONException e) {
			log.error("No se pudo obtener el campo " + nombreParametro + " desde el request " + e.getMessage());
		}
		return rutClienteBody;
	}

	/**
	 * Obtiene desde el body el valor del parametro enviado como entrada al metodo,
	 * NOTA: Se utiliza cuando el metodo a obtener esta directamente dede la raiz
	 * del objeto
	 * 
	 * @param requestContext
	 * @param request
	 * @param nombreParametro
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private String obtieneRutClientePostRaiz(ContainerRequestContext requestContext, String request, String nombreParametro) throws IOException {
		JSONObject obj = new JSONObject(request);
		// Obtener objeto json desde la raiz directamente
		String rutClienteBody = obj.getString(nombreParametro).toString();
		return rutClienteBody;
	}

}
