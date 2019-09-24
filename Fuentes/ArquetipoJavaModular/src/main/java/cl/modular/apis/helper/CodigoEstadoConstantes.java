package cl.modular.apis.helper;

/**
 * Final. Recursos de tipo texto, implementados en el
 * {DTORequestDatosContextoHttp} para establecer los codigos y mensajes del
 * estandar de respuesta de cada servicio.
 *
 * @author Camilo Contreras
 * 
 */
public class CodigoEstadoConstantes {

	private CodigoEstadoConstantes() {
		throw new IllegalStateException("CodigoEstadoConstantes");
	}

	// -------------------------------------------------------
	// *** Mensajes Codigo estado

	// Bad request 400
	public static final String BADREQUEST_CODIGO = "400";
	public static final String BADREQUEST_MENSAJE = "BAD REQUEST";
	public static final String BADREQUEST_DESCRIPCION = "El servidor no puede entender la solicitud debido a una sintaxis mal formada. El cliente NO DEBE repetir la solicitud sin modificaciones.";

	// Unprocessable Entity 422
	public static final String UNPROCESSABLE_ENTITY_CODIGO = "422";
	public static final String UNPROCESSABLE_ENTITY_MENSAJE = "UNPROCESSABLE ENTITY";
	public static final String UNPROCESSABLE_ENTITY_DESCRIPCION = "El servidor PUEDE entender la solicitud. PERO es semanticamente incorrecto. El cliente NO DEBE repetir la solicitud sin modificaciones.";
	public static final String UNPROCESSABLE_ENTITY_UTF_DESCRIPCION = "Codificacion UTF-8: El servidor NO puede entender la solicitud debido a una sintaxis mal formada. El cliente NO DEBE repetir la solicitud sin antes codificarla en UTF-8.";

	// UNAUTHORIZED 401
	public static final String UNAUTHORIZED_CODIGO = "401";
	public static final String UNAUTHORIZED_MENSAJE = "UNAUTHORIZED";
	public static final String UNAUTHORIZED_DESCRIPCION = "TOKEN DE AUTORIZACION NO VALIDO";

	// INTERNAL_SERVER_ERROR 401
	public static final String INTERNALERROR_CODIGO = "500";
	public static final String INTERNALERROR_MENSAJE = "INTERNAL_SERVER_ERROR";

	// OBTENCION DE DATOS EXITOSA 200
	public static final String OK_CODIGO = "200";
	public static final String OK_MENSAJE = "OK";
	public static final String OK_DESCRIPCION = "OBTENCION DE DATOS EXITOSA";

	// NO_CONTENT 204
	public static final String NOCONTENT_CODIGO = "204";
	public static final String NOCONTENT_MENSAJE = "NO_CONTENT";
	public static final String NOCONTENT_DESCRIPCION = "NO SE ENCONTRARON REGISTROS ASOCIADOS";

	// INSERT NO_CONTENT 204
	public static final String INSERT_NOCONTENT_DESCRIPCION = "NO SE ENCONTRARON REGISTROS DE RETORNO";

	// Mensaje Codigo estado
	public static final String BADREQUESTUTF = "Codificacion UTF-8: El servidor no puede entender la solicitud debido a una sintaxis mal formada. El cliente NO DEBE repetir la solicitud sin antes codificarla en UTF-8.";

}
