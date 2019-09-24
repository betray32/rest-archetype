package cl.modular.apis.helper;

import cl.modular.apis.res.Resources;

/**
 * Constantes para este servicio
 * 
 * @author Camilo Contreras
 *
 */
public class Constantes {
	
	/**
	 * Archivo de parametros
	 */
	public static final String ARCHIVO_PALABRAS_RESERVADAS = "EH_PALABRAS_NOAUTORIZADAS.json";

	/**
	 * Variables desde las properties
	 */
	public static final String DATASOURCE_SQLSERVER = Resources.getResource("DATASOURCE_SQLSERVER");
	public static final String DATASOURCE_ORACLE = Resources.getResource("DATASOURCE_ORACLE");
	public static final String DATASOURCE_ORACLE_METADATA = Resources.getResource("DATASOURCE_ORACLE_METADATA");

	public static final String VERSION_SERVICIO = Resources.getResource("VERSION_SERVICIO");

	/**
	 * Codigos de validacion metadata
	 */
	public static final int CODIGOMETADATAFILTROCUENTA = 1;
	public static final int CODIGOMETADATAFILTROCREDITO = 2;
	public static final int CODIGOMETADATAFILTROTARJETA = 3;

	/**
	 * Headers
	 */
	public static final String HEADER_IP = "ipCliente";
	public static final String HEADER_COD_CANAL = "codigoCanal";
	public static final String HEADER_COD_APLICACION = "codigoAplicacion";
	public static final String HEADER_EMPRESA_APLICACION = "empresaAplicacion";
	public static final String HEADER_MODALIDAD = "modalidad";
	public static final String HEADER_TOKEN = "token-authorization";
	public static final String HEADER_CLIENT_ID = "clientid";
	public static final String HEADER_REQUEST_ID = "requestID";

	/**
	 * Repuestas
	 */
	public static final String RES_ERROR_TOKEN = "TOKEN INVALIDO, SIN AUTORIZACION";

	public static final String RES_ERROR_HEADER = "HEADERS INVALIDOS";

	/**
	 * Validacion de salida para la consulta
	 */
	public static final String VALIDACION_TOKEN_FINAL = "TOKEN VALIDO";

	/**
	 * Nombre del procedure para consultar clientes, desde SQLSERVEr
	 */
	public static final String PROCEDURE_DEFINICION_CLIENTE_RESUMEN = Resources.getResource("PROCEDURE_CLIENTES");

	/**
	 * Procedure para consultar por los productos
	 */
	public static final String PROCEDURE_DEFINICION_PRODUCTOS_RESUMEN = Resources.getResource("PROCEDURE_PRODUCTOS_RESUMEN");

	/**
	 * Valida definicion
	 */
	public static final String PROCEDURE_DEFINICION_VALIDA_METADATA = Resources.getResource("PROCEDURE_SP_VALIDA_METADATA");

	/**
	 * Endpoint para el servicio de token
	 */
	public static final String ENDPOINT_SERVICIO_TOKEN = Resources.getResource("ENDPOINT_SERVICIO_TOKEN");
	
}
