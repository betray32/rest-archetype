package cl.modular.apis.rest.client;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import cl.modular.apis.bean.DtoRequestDatosContextoHttp;
import cl.modular.apis.bean.RespuestaConsultaToken;
import cl.modular.apis.helper.Constantes;

/**
 * Token Rest Client
 * 
 * @author Camilo Contreras
 *
 */
public class TokenRestClient {

	/**
	 * LOG
	 */
	private static final Logger log = Logger.getLogger(TokenRestClient.class);

	private ResteasyClient client = null;

	/**
	 * Permite llamar al servicio de tokens y consultar por la validez del token
	 * enviado
	 * 
	 * @param inputContext
	 * 
	 * @return
	 */
	public boolean consultarToken(DtoRequestDatosContextoHttp inputContext) {

		Response response = null;
		try {

			client = new ResteasyClientBuilder().build();
			log.info("REST CLIENT: Iniciando cliente REST: " + Constantes.ENDPOINT_SERVICIO_TOKEN);

			Builder builder = client.target(Constantes.ENDPOINT_SERVICIO_TOKEN).request();

			builder.header(Constantes.HEADER_TOKEN, inputContext.getToken());
			builder.header(Constantes.HEADER_CLIENT_ID, inputContext.getClientId());
			builder.header(Constantes.HEADER_REQUEST_ID, inputContext.getRequestID());

			builder.header(Constantes.HEADER_COD_APLICACION, inputContext.getCodigoAplicacion());
			builder.header(Constantes.HEADER_COD_CANAL, inputContext.getCodigoCanal());
			builder.header(Constantes.HEADER_EMPRESA_APLICACION, inputContext.getEmpresaAplicacion());
			builder.header(Constantes.HEADER_IP, inputContext.getIpCliente());

			builder.header(Constantes.HEADER_MODALIDAD, "consultar");

			Invocation invocacion = builder.buildGet();
			response = invocacion.invoke();

			int status = response.getStatus();

			// NO OK
			if (status != 200) {

				log.error("Error llamando a servicio  : " + response.getStatus());
				throw new RuntimeException("Error llamando a servicio  : " + response.getStatus());

			}

			log.info("REST CLIENT: Resultado de la operacion: " + status);

			// OK
			RespuestaConsultaToken res = response.readEntity(RespuestaConsultaToken.class);
			int codigoServicioToken = Integer.parseInt(res.getDtoresponsecodigosestadohttp().getCodigo());

			switch (codigoServicioToken) {
			case 0:
				log.info("Token OK, se acepta la solicitud para este servicio");
				return true;

			case 101:
				log.error("Token Invalido, se deniega la solicitud para este servicio");
				return false;

			default:
				log.error("Token Invalido, se deniega la solicitud para este servicio");
				return false;

			}

		} catch (Exception e) {
			log.error("Error al invocar el servicio de token, ERROR > " + e.getMessage());
		}

		return false;
	}

}
