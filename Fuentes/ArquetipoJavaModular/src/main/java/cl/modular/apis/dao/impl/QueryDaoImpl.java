package cl.modular.apis.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.dao.QueryDao;
import cl.modular.apis.dao.config.ConnectionFactory;
import cl.modular.apis.dao.mappers.schema.RowMapper;
import cl.modular.apis.helper.DiccionarioProcedimientos;

/**
 * Permite obtener la data desde el Procedure
 * 
 * @author Camilo Contreras
 *
 */
public class QueryDaoImpl implements QueryDao {

	/*****************
	 * Mensajes
	 */
	private static final String HA_OCURRIDO_UN_PROBLEMA_AL_CONSULTAR_ORACLE_DETALLE = "Ha ocurrido un problema al consultar ORACLE, Detalle > ";
	private static final String STATEMENT_OK = "Statement OK!";
	private static final String ARMANDO_EL_STATEMENT = "Armando el Statement";
	private static final String PROCEDURE = "Procedure > ";
	private static final String PROCEDERE_A_ARMAR_EL_SP_PARA_CONECTARME_EL_INDICADO_ES = "Procedere a armar el SP para conectarme, el indicado es > ";
	private static final String CONEXION_OBTENIDA_EXITOSAMENTE = "Conexion obtenida exitosamente: ";
	private static final String PARAMETROS_DE_SALIDA_MAPEADOS_CORRECTAMENTE = "Parametros de salida mapeados correctamente";
	private static final String PROCEDIENDO_A_RESCATAR_LOS_PARAMETROS_DE_SALIDA = "Procediendo a rescatar los parametros de salida";
	private static final String PROCEDIMIENTO_CONSULTADO_EXITOSAMENTE = "Procedimiento consultado exitosamente";
	private static final String CONEXION_CERRADA_EXITOSAMENTE = "Conexion Cerrada Exitosamente";
	private static final String PROCEDURE_EJECUTADO_OK = "Procedure ejecutado OK";
	private static final String EJECUTANDO_PROCEDURE = "Ejecutando Procedure...";
	private static final String SETEANDO_PARAMETROS = "Seteando Parametros...";
	private static final String CONEXION_OK = "Conexion OK";
	private static final String ERROR_CONSULTAR_EXCEPCION = "Ha ocurrido un problema al consultar, Detalle > ";
	private static final String CONECTANDO_ORACLE = "Conectando al recurso DataSource DB Oracle indicada > ";
	private static final String ERROR_CERRAR_CONEXIONES = "Ha ocurrido un problema al cerrar la conexion, Detalle > ";
	private static final String CONECTANDO_PROCEDURE = "Conectando al Procedure indicado > ";
	private static final String CONECTANDO_SQLSERVER = "Conectando al recurso DataSource SQLServer indicado > ";

	/**
	 * LOGGER
	 */
	private static final Logger log = Logger.getLogger(QueryDaoImpl.class);

	/**
	 * Codigo de retorno para la consulta
	 */
	private int codRetorno;

	/**
	 * Esta salida permite obtener parametros que NO sean de un ResultSet
	 */
	private List<String> salidaCompuesta;

	/**
	 * Permite consultar una base de datos y obtener campos aislados
	 */
	@Override
	public int queryForProcedureSqlServerGetSpecificOut(String dataSource, Map<Integer, DefinicionParametro> params, Map<String, Integer> outParams, String dbProcedimiento, int obtenerPosicion) {

		Connection dbConexion = null;
		CallableStatement dbComando = null;
		ResultSet dbResultados = null;

		try {

			log.info(CONECTANDO_SQLSERVER + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, params.size() + outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsSqlServerInOut(dbComando, params, outParams);

			log.info(EJECUTANDO_PROCEDURE);

			if (!dbComando.execute()) {
				codRetorno = dbComando.getInt(obtenerPosicion);
				log.info(PROCEDURE_EJECUTADO_OK);
				return codRetorno;
			}

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION, e);
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando, dbResultados);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return -1;

	}

	/**
	 * Obtener los datos del cliente en la busqueda, SQL Server, Se setean los
	 * parametros de entrada y se obtiene simplemente el resultado asumiendo que
	 * este es una lista de respuesta
	 */
	@Override
	public <T> List<T> queryForProcedureSqlServer(String dataSource, Map<Integer, DefinicionParametro> params, RowMapper<T> rowMapper, String dbProcedimiento) {

		// Generacion de Objetos
		List<T> models = new ArrayList<>();
		Connection dbConexion = null;
		CallableStatement dbComando = null;
		ResultSet dbResultados = null;

		try {

			log.info(CONECTANDO_SQLSERVER + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, params.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsSqlServer(dbComando, params);

			log.info(EJECUTANDO_PROCEDURE);
			dbResultados = dbComando.executeQuery();
			log.info(PROCEDURE_EJECUTADO_OK);

			while (dbResultados.next()) {
				T model =  rowMapper.mapper(dbResultados);
				models.add(model);
			}

			log.info(PROCEDIMIENTO_CONSULTADO_EXITOSAMENTE);
			codRetorno = 1;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando, dbResultados);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return models;

	}

	/**
	 * Llama al procedure y obtiene el campo de salida especifico
	 */
	@Override
	public String insertUpdateProcedureSqlServer(String dataSource, Map<Integer, DefinicionParametro> params, Map<String, Integer> outParams, String dbProcedimiento, int obtenerPosicion) {

		// Generacion de Objetos
		Connection dbConexion = null;
		CallableStatement dbComando = null;
		ResultSet resultado = null;

		try {

			log.info(CONECTANDO_SQLSERVER + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, params.size() + outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsSqlServerInOut(dbComando, params, outParams);

			log.info(EJECUTANDO_PROCEDURE);
			resultado = dbComando.executeQuery();
			log.info(PROCEDURE_EJECUTADO_OK);

			String codigoResultado = null;
			while (resultado.next()) {
				codigoResultado = resultado.getString(obtenerPosicion);
				break;
			}

			return codigoResultado;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando, resultado);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return null;
	}

	/**
	 * Llamar a un procedure con insertar o editar para insertar un campo en Oracle,
	 * toma la cantidad de filas afectadas en la salida
	 */
	@Override
	public int insertUpdateProcedureOracle(String dataSource, Map<Integer, DefinicionParametro> params, Map<Integer, Integer> outParams, String dbProcedimiento) {

		// Generacion de Objetos
		Connection dbConexion = null;
		CallableStatement dbComando = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, params.size() + outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsOracleInsertUpdate(dbComando, params, outParams);

			log.info(EJECUTANDO_PROCEDURE);
			int codigoResultado = dbComando.executeUpdate();
			log.info(PROCEDURE_EJECUTADO_OK);

			return codigoResultado;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return -1;
	}

	/**
	 * Permite ejecutar un procedure y obtener la cantidad de filas afectadas por
	 * este procedimiento
	 */
	@Override
	public int rowsForProcedureOracle(String dataSource, Map<Integer, DefinicionParametro> inParams, String dbProcedimiento) {

		// Generacion de Objetos
		Connection dbConexion = null;
		CallableStatement dbComando = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, inParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsOracleRows(dbComando, inParams);

			log.info(EJECUTANDO_PROCEDURE);
			int rows = dbComando.executeUpdate();
			log.info(PROCEDURE_EJECUTADO_OK);

			return rows;

		} catch (Exception e) {
			log.error(HA_OCURRIDO_UN_PROBLEMA_AL_CONSULTAR_ORACLE_DETALLE + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return -1;

	}

	/**
	 * Obtener los datos en la busqueda, Oracle, en estos procedimientos se debe
	 * definir explicitamente los parametros tanto de entrada como de salida, ademas
	 * una vez ejecutados se debe de indicar en que posicion esta el Cursor, por lo
	 * cual esto tambien se debe de determinar en tiempo de ejecucion
	 */
	@Override
	public <T> List<T> queryForProcedureOracle(String dataSource, Map<Integer, DefinicionParametro> inParams, Map<String, Integer> outParams, RowMapper<T> rowMapper, String dbProcedimiento) {

		// Generacion de Objetos
		List<T> models = new ArrayList<>();
		Connection dbConexion = null;
		CallableStatement dbComando = null;
		ResultSet dbResultados = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, inParams.size() + outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			HashMap<Integer, Map<Integer, String>> res = SQLDaoMapper.setParamsOracle(dbComando, inParams, outParams);

			int posicionCursor = 0;
			Map<Integer, String> metadataParametros = new HashMap<>();

			for (Map.Entry<Integer, Map<Integer, String>> entry : res.entrySet()) {
				posicionCursor = entry.getKey();
				metadataParametros = entry.getValue();
			}

			log.info(EJECUTANDO_PROCEDURE);
			dbComando.execute();
			log.info(PROCEDURE_EJECUTADO_OK);

			dbResultados = (ResultSet) dbComando.getObject(posicionCursor);

			if (dbResultados == null) {
				log.info("Procedure no retorna resultado: " + dbProcedimiento);
			} else {

				while (dbResultados.next()) {
					T model =  rowMapper.mapper(dbResultados);
					models.add(model);
				}

				SQLDaoMapper.extraerParametrosAdicionales(metadataParametros, dbComando);

				log.info(PROCEDIMIENTO_CONSULTADO_EXITOSAMENTE);
				codRetorno = 1;

			}

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando, dbResultados);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return models;

	}

	/**
	 * Permite llamar a un procedure el cual no tenga parametros de entrada y
	 * responda cursores
	 */
	@Override
	public <T> List<T> queryForProcedureOracleNoParam(String dataSource, Map<String, Integer> outParams, RowMapper<T> rowMapper, String dbProcedimiento) {

		// Generacion de Objetos
		List<T> models = new ArrayList<>();
		Connection dbConexion = null;
		CallableStatement dbComando = null;
		ResultSet dbResultados = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			HashMap<Integer, Map<Integer, String>> res = SQLDaoMapper.setParamsOracleNoInput(dbComando, outParams);

			int posicionCursor = 0;
			Map<Integer, String> metadataParametros = new HashMap<>();

			for (Map.Entry<Integer, Map<Integer, String>> entry : res.entrySet()) {
				posicionCursor = entry.getKey();
				metadataParametros = entry.getValue();
			}

			log.info(EJECUTANDO_PROCEDURE);
			dbComando.execute();
			log.info(PROCEDURE_EJECUTADO_OK);

			dbResultados = (ResultSet) dbComando.getObject(posicionCursor);

			if (dbResultados == null) {
				log.info("Procedure no retorna resultado: " + dbProcedimiento);
			} else {

				while (dbResultados.next()) {
					T model =  rowMapper.mapper(dbResultados);
					models.add(model);
				}

				SQLDaoMapper.extraerParametrosAdicionales(metadataParametros, dbComando);

				log.info(PROCEDIMIENTO_CONSULTADO_EXITOSAMENTE);
				codRetorno = 1;

			}

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando, dbResultados);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return models;

	}

	/**
	 * Procedure que consulta campos pero no entrega cursor de salida sino que
	 * unicamente campos aislados
	 */
	@Override
	public LinkedHashMap<Integer, Object> procedureOracleSinCursor(String dataSource, Map<Integer, DefinicionParametro> params, Map<Integer, Integer> outParams, String dbProcedimiento) {

		// Generacion de Objetos
		Connection dbConexion = null;
		CallableStatement dbComando = null;
		LinkedHashMap<Integer, Object> res = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, params.size() + outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsOracleInsertUpdate(dbComando, params, outParams);

			log.info(EJECUTANDO_PROCEDURE);
			dbComando.execute();
			log.info(PROCEDURE_EJECUTADO_OK);

			log.info(PROCEDIENDO_A_RESCATAR_LOS_PARAMETROS_DE_SALIDA);
			res = SQLDaoMapper.getReturnParamsMapping(dbComando, outParams);
			log.info(PARAMETROS_DE_SALIDA_MAPEADOS_CORRECTAMENTE);

			return res;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			res = new LinkedHashMap<>();
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
			}
		}

		return res;

	}

	/**
	 * Permite llamar una conexion sqlserver con el driver nativo
	 */
	@Override
	public int sqlServerNativeProcedure(String dataSource, LinkedHashMap<Integer, DefinicionParametro> params, String dbProcedimiento) {

		// Generacion de Objetos
		SQLServerPreparedStatement pStmt = null;
		Connection con = null;

		try {

			log.info("Realizando la conexion a la BD SQLServer indicada > " + dataSource);
			con = ConnectionFactory.obtenerConexion(dataSource);

			/*
			 * Transformar la conexion desde el Datasource de JBOSS EAP hacia la conexion
			 * nativa de sql Server
			 */
			org.jboss.jca.adapters.jdbc.WrappedConnection wc = (org.jboss.jca.adapters.jdbc.WrappedConnection) con;
			con = null;
			con = wc.getUnderlyingConnection();

			log.info(CONEXION_OBTENIDA_EXITOSAMENTE + con);

			// Armar string del procedure
			log.info(PROCEDERE_A_ARMAR_EL_SP_PARA_CONECTARME_EL_INDICADO_ES + dbProcedimiento);
			String ececStoredProc = DiccionarioProcedimientos.armarProcedureSqlServerExec(dbProcedimiento, params.size());
			log.info(PROCEDURE + ececStoredProc);

			log.info(ARMANDO_EL_STATEMENT);
			pStmt = (SQLServerPreparedStatement) con.prepareStatement(ececStoredProc);
			log.info(STATEMENT_OK);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsSqlServer(pStmt, params);

			log.info(EJECUTANDO_PROCEDURE);
			int rows = pStmt.executeUpdate();
			log.info(PROCEDURE_EJECUTADO_OK);

			return rows;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(con, pStmt);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return 0;
	}

	/**
	 * Retorna objetos por posicion no cursores, y no recibe ningun parametro de
	 * entrada
	 */
	@Override
	public LinkedHashMap<Integer, Object> queryForProcedureOracleNoParamNoCursor(String dataSource, Map<Integer, Integer> outParams, String dbProcedimiento) {

		// Generacion de Objetos
		Connection dbConexion = null;
		CallableStatement dbComando = null;
		LinkedHashMap<Integer, Object> res = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, outParams.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsOracleNoInputSinCursor(dbComando, outParams);

			log.info(EJECUTANDO_PROCEDURE);
			dbComando.execute();
			log.info(PROCEDURE_EJECUTADO_OK);

			log.info(PROCEDIENDO_A_RESCATAR_LOS_PARAMETROS_DE_SALIDA);
			res = SQLDaoMapper.getReturnParamsMapping(dbComando, outParams);
			log.info(PARAMETROS_DE_SALIDA_MAPEADOS_CORRECTAMENTE);

			return res;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			res = new LinkedHashMap<>();
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
			}
		}

		return res;

	}

	/**
	 * Permite ejecutar un procedure que devuelva unicamente las filas afectadas
	 */
	@Override
	public int sqlServerProcedureRows(String dataSource, LinkedHashMap<Integer, DefinicionParametro> params, String dbProcedimiento) {

		// Generacion de Objetos
		Connection dbConexion = null;
		CallableStatement dbComando = null;

		try {

			log.info(CONECTANDO_ORACLE + dataSource);
			dbConexion = ConnectionFactory.obtenerConexion(dataSource);
			log.info(CONEXION_OK);

			log.info(CONECTANDO_PROCEDURE + dbProcedimiento);
			String procedure = DiccionarioProcedimientos.armarProcedure(dbProcedimiento, params.size());
			dbComando = dbConexion.prepareCall(procedure);
			log.info(PROCEDURE + procedure);

			log.info(SETEANDO_PARAMETROS);
			SQLDaoMapper.setParamsSqlServer(dbComando, params);

			log.info(EJECUTANDO_PROCEDURE);
			int rows = dbComando.executeUpdate();
			log.info(PROCEDURE_EJECUTADO_OK);

			return rows;

		} catch (Exception e) {
			log.error(ERROR_CONSULTAR_EXCEPCION + e.getMessage());
			codRetorno = -1;
		} finally {
			try {
				cerrarConexion(dbConexion, dbComando);
				log.info(CONEXION_CERRADA_EXITOSAMENTE);
			} catch (SQLException e) {
				log.error(ERROR_CERRAR_CONEXIONES + e.getMessage());
				codRetorno = -1;
			}
		}

		return -1;

	}

	/**
	 * Permite cerrar las conexiones
	 * 
	 * @param dbConexion
	 * @param dbComando
	 * @param dbResultados
	 * @throws SQLException
	 */
	private void cerrarConexion(Connection dbConexion, CallableStatement dbComando, ResultSet dbResultados) throws SQLException {
		if (dbResultados != null) {
			dbResultados.close();
		}

		if (dbComando != null) {
			dbComando.close();
		}

		if (dbConexion != null) {
			dbConexion.close();
		}
	}

	/**
	 * Permite cerrar las conexiones
	 * 
	 * @param dbConexion
	 * @param dbComando
	 * @param dbResultados
	 * @throws SQLException
	 */
	private void cerrarConexion(Connection dbConexion, CallableStatement dbComando) throws SQLException {

		if (dbComando != null) {
			dbComando.close();
		}

		if (dbConexion != null) {
			dbConexion.close();
		}
	}

	/**
	 * Permite cerrar las conexiones
	 * 
	 * @param dbConexion
	 * @param dbComando
	 * @param dbResultados
	 * @throws SQLException
	 */
	private void cerrarConexion(Connection dbConexion, SQLServerPreparedStatement dbComando) throws SQLException {

		if (dbComando != null) {
			dbComando.close();
		}

		if (dbConexion != null) {
			dbConexion.close();
		}
	}

	/**
	 * Obtener datos adicionales
	 */
	@Override
	public List<String> getSalidaCompuesta() {
		return salidaCompuesta;
	}

	/*
	 * Codigo de retorno de la llamada al servicio
	 */
	@Override
	public int codRetorno() {
		return codRetorno;
	}

}
