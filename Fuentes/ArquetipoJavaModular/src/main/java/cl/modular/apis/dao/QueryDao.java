package cl.modular.apis.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.dao.mappers.schema.RowMapper;

/**
 * Interface para el dao de obtener la data desde las bases de datos
 * 
 * @author ccontrerasc
 *
 */
public interface QueryDao {
	
	/**
	 * Permite consultar una base de datos y obtener campos aislados
	 * 
	 * @param dataSource
	 * @param params
	 * @param outParams
	 * @param dbProcedimiento
	 * @param obtenerPosicion Numero del campo de salida que necesitas
	 * @return
	 */
	public int queryForProcedureSqlServerGetSpecificOut(String dataSource, Map<Integer, DefinicionParametro> params, Map<String, Integer> outParams, String dbProcedimiento, int obtenerPosicion);

	/**
	 * Permite consultar un procedure ubicado en una base de datos Microsoft SQL
	 * Server
	 * 
	 * @param dataSource
	 *            Datasource hacia el cual se conectara el metodo, debe estar en la
	 *            properties
	 * @param params
	 *            Parametros de entrada para el procedimiento
	 * @param rowMapper
	 *            Mapeo de salida que retornara este metodo
	 * @param dbProcedimiento
	 *            Nombre del procedure a llamar
	 * @return Lista de respuesta generica
	 */
	public <T> List<T> queryForProcedureSqlServer(String dataSource, Map<Integer, DefinicionParametro> params, RowMapper<T> rowMapper, String dbProcedimiento);

	/**
	 * Llama al procedure y obtiene el campo de salida especifico, sirve para
	 * procedimientos que retornan por ejemplo solo un parametro de salida el cual
	 * nos importa ej: respuesta, codigoSalida, etc
	 * 
	 * @param dataSource
	 * @param params
	 * @param dbProcedimiento
	 * @param obtenerPosicion,
	 *            Este campo indica la posicion exacta que se desea sacar
	 * @return
	 */
	public String insertUpdateProcedureSqlServer(String dataSource, Map<Integer, DefinicionParametro> params, Map<String, Integer> outParams, String dbProcedimiento,
			int obtenerPosicion);

	/**
	 * Llamar a un procedure con insertar o editar para insertar un campo en Oracle,
	 * toma la cantidad de filas afectadas en la salida
	 * 
	 * @param dataSource
	 * @param params
	 * @param outParams
	 * @param dbProcedimiento
	 * @return
	 */
	public int insertUpdateProcedureOracle(String dataSource, Map<Integer, DefinicionParametro> params, Map<Integer, Integer> outParams, String dbProcedimiento);

	/**
	 * Permite consultar un procedure oracle en donde el resultado son unicamente
	 * campos aislados y no un cursor, por ej un procedure que aplicaria en este
	 * metodo seria uno que devuelva solo dos campos, codigo de salida y
	 * descripcion.
	 * 
	 * @param dataSource
	 * @param params
	 * @param outParams
	 * @param dbProcedimiento
	 * @return
	 */
	public LinkedHashMap<Integer, Object> procedureOracleSinCursor(String dataSource, Map<Integer, DefinicionParametro> params, Map<Integer, Integer> outParams,
			String dbProcedimiento);

	/**
	 * Permite ejecutar un servicio de SQLServer con un procedimiento utilizando el
	 * driver nativo
	 * 
	 * @param dataSource
	 * @param params
	 * @param dbProcedimiento
	 * @return
	 */
	public int sqlServerNativeProcedure(String dataSource, LinkedHashMap<Integer, DefinicionParametro> params, String dbProcedimiento);

	/**
	 * Permite ejecutar un procedure que devuelva unicamente las filas afectadas
	 * 
	 * @param dataSource
	 * @param params
	 * @param dbProcedimiento
	 * @return
	 */
	public int sqlServerProcedureRows(String dataSource, LinkedHashMap<Integer, DefinicionParametro> params, String dbProcedimiento);

	/**
	 * Consultar por un procedimiento ubicado en una base de datos Oracle
	 * 
	 * @param dataSource
	 *            Datasource hacia cual se conecta el metodo debe estar en la
	 *            properties
	 * @param inParams
	 *            Parametros de entrada para el procedimiento
	 * @param outParams
	 *            Parametros de salida para el procedimiento
	 * @param rowMapper
	 *            Mapeo de salida que retornara al metodo
	 * @param dbProcedimiento
	 *            Nombre del procedure a llamar dentro de esta base de datos oracle
	 * @return Lista de respuesta generica
	 */
	public <T> List<T> queryForProcedureOracle(String dataSource, Map<Integer, DefinicionParametro> inParams, Map<String, Integer> outParams, RowMapper<T> rowMapper,
			String dbProcedimiento);

	/**
	 * Permite llamar a un procedure el cual no tenga parametros de entrada y
	 * responda cursores
	 * 
	 * @param dataSource
	 * @param outParams
	 * @param rowMapper
	 * @param dbProcedimiento
	 * @return
	 */
	public <T> List<T> queryForProcedureOracleNoParam(String dataSource, Map<String, Integer> outParams, RowMapper<T> rowMapper, String dbProcedimiento);

	/**
	 * Retorna objetos por posicion no cursores, y no recibe ningun parametro de
	 * entrada
	 * 
	 * @param dataSource
	 * @param params
	 * @param outParams
	 * @param dbProcedimiento
	 * @return
	 */
	public LinkedHashMap<Integer, Object> queryForProcedureOracleNoParamNoCursor(String dataSource, Map<Integer, Integer> outParams, String dbProcedimiento);

	/**
	 * Permite consultar un procedure oracle y rescatar la cantidad de filas
	 * afectadas por este procedimiento
	 * 
	 * @param dataSource
	 * @param inParams
	 * @param outParams
	 * @param rowMapper
	 * @param dbProcedimiento
	 * @return
	 */
	public int rowsForProcedureOracle(String dataSource, Map<Integer, DefinicionParametro> inParams, String dbProcedimiento);

	/**
	 * Permite obtener parametros adicionales
	 * 
	 * @return
	 */
	public List<String> getSalidaCompuesta();

	/**
	 * Codigo de retorno de la llamada al servicio
	 * 
	 * @return
	 */
	public int codRetorno();
}
