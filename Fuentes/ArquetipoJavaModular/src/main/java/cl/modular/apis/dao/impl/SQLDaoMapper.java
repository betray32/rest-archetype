package cl.modular.apis.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.helper.ConstantesBD;

/**
 * Funciones y mapeos para los DAOS
 * 
 * @author ccontrerasc
 *
 */
public class SQLDaoMapper {
	
	/**
	 * LOGGER
	 */
	private static final Logger log = Logger.getLogger(SQLDaoMapper.class);

	/**
	 * Permite mapear los parametros de salida para el procedure deseado
	 * 
	 * @param dbComando
	 * 
	 * @param outParams
	 * @return
	 */
	public static LinkedHashMap<Integer, Object> getReturnParamsMapping(CallableStatement dbComando, Map<Integer, Integer> outParams) {

		LinkedHashMap<Integer, Object> res = new LinkedHashMap<>();

		for (Entry<Integer, Integer> entry : outParams.entrySet()) {
			int pos = entry.getKey();
			int tipoData = entry.getValue();

			Object campo = null;

			try {
				campo = obtenerTipoDatoMapping(dbComando, pos, tipoData);
			} catch (SQLException e) {
				log.error("Ha ocurrido un error al obtener el mapeo del campo");
				log.error("Posicion: " + pos + " , TipoData: " + tipoData);
			}

			log.info("Campo Salida Numero: " + pos + " , Contenido: " + campo);
			res.put(pos, campo);
		}

		return res;

	}

	/**
	 * Obtener el dato segun sea el tipo de mapeo
	 * 
	 * @param dbComando
	 * @param pos
	 * @param tipoData
	 * @return
	 * @throws SQLException
	 */
	public static Object obtenerTipoDatoMapping(CallableStatement dbComando, int pos, int tipoData) throws SQLException {

		Object res = new Object();

		switch (tipoData) {
		case java.sql.Types.NUMERIC:
			res = (Integer) dbComando.getInt(pos);
			break;

		case java.sql.Types.VARCHAR:
			res = (String) dbComando.getString(pos);
			break;

		case java.sql.Types.DATE:
			res = (java.util.Date) dbComando.getDate(pos);
			break;

		case java.sql.Types.FLOAT:
			res = (Float) dbComando.getFloat(pos);
			break;

		}

		return res;

	}

	/**
	 * Permite setear los parametros de manera dinamica por cada uno de los
	 * parametros del procedimiento, ademas de esto no importa en donde este la
	 * posicion del cursor (ya que usualmente este submetodo se utiliza en
	 * procedimientos que no entregan cursores de salida)
	 * 
	 * @param dbComando
	 * @param inputParams
	 *            Parametros de entrada
	 * @param outputParams
	 *            Definicion con los parametros de salida
	 * @throws SQLException
	 */
	public static int setParamsOracleInsertUpdate(CallableStatement dbComando, Map<Integer, DefinicionParametro> inputParams, Map<Integer, Integer> outputParams)
			throws SQLException {

		if (inputParams.size() > 0 && outputParams.size() > 0) {

			int posicionCursor = 0;

			// Agregar los parametros de entrada
			log.info("ASIGNAR ENTRADAS DEL SP ORACLE");

			int cont = 1;
			for (Map.Entry<Integer, DefinicionParametro> entry : inputParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor: " + entry.getValue().getValorCampo());

				SQLDaoMapper.mappingDataToType(dbComando, cont, entry);
				cont++;
			}

			/*
			 * Agregar los parametros de salida, se deben de comenzar a agregar los
			 * parametros desde el ultimo que se dejo en los parametros de entrada
			 */
			log.info("ASIGNAR PARAMETROS DE SALIDA DEL SP ORACLE");
			for (Map.Entry<Integer, Integer> entry : outputParams.entrySet()) {

				int fixedLenght = cont;
				log.info("Asignare en la posicion " + fixedLenght + " del SP el valor: " + entry.getValue());
				dbComando.registerOutParameter(fixedLenght, entry.getValue());

				cont++;

			}

			return posicionCursor;

		}

		return 0;

	}

	/**
	 * Permite setear los parametros tanto de entrada como de salida
	 * 
	 * @param dbComando
	 * @param inputParams
	 * @throws SQLException
	 */
	public static void setParamsSqlServerInOut(CallableStatement dbComando, Map<Integer, DefinicionParametro> inputParams, Map<String, Integer> outParams) throws SQLException {

		int cont = 1;
		if (dbComando != null && inputParams.size() > 0) {

			for (Entry<Integer, DefinicionParametro> entry : inputParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor: " + entry.getValue().getValorCampo());

				SQLDaoMapper.mappingDataToType(dbComando, cont, entry);

				cont++;
			}

		}

		/**
		 * Setear parametros de salida
		 */
		if (dbComando!= null && outParams.size() > 0) {

			for (Entry<String, Integer> entry : outParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor de tipo salida: " + entry.getKey());

				switch (entry.getKey()) {
				case ConstantesBD.TIPO_VARCHAR:
					dbComando.registerOutParameter(cont, ConstantesBD.ORACLE_VARCHAR_CODE);
					break;

				case ConstantesBD.TIPO_INTEGER:
					dbComando.registerOutParameter(cont, ConstantesBD.ORACLE_INTEGER_CODE);
					break;

				case ConstantesBD.TIPO_CURSOR:
					dbComando.registerOutParameter(cont, ConstantesBD.ORACLE_ARRAY_CODE);
					break;

				}

				cont++;
			}

		}

	}

	/**
	 * Permite setear los parametros para una solicitud de procedure oracle en el
	 * cual rescatamos unicamente la cantidad de filas afectadas (en este caso no
	 * importa
	 * 
	 * @param dbComando
	 * @param inputParams
	 * @param outputParams
	 * @return
	 * @throws SQLException
	 */
	public static int setParamsOracleRows(CallableStatement dbComando, Map<Integer, DefinicionParametro> inputParams) throws SQLException {

		if (dbComando != null && inputParams.size() > 0) {

			int posicionCursor = 0;

			// Agregar los parametros de entrada
			log.info("ASIGNAR ENTRADAS DEL SP ORACLE");

			int cont = 1;
			for (Map.Entry<Integer, DefinicionParametro> entry : inputParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor: " + entry.getValue().getValorCampo());

				SQLDaoMapper.mappingDataToType(dbComando, cont, entry);
				cont++;
			}

			return posicionCursor;

		}

		return 0;

	}

	/**
	 * Permite setear los parametros de manera dinamica por cada uno de los
	 * procedimientos
	 * 
	 * @param dbComando
	 * @throws SQLException
	 */
	public static void setParamsSqlServer(SQLServerPreparedStatement dbComando, Map<Integer, DefinicionParametro> inputParams) throws SQLException {

		if (dbComando != null && inputParams.size() > 0) {

			int cont = 1;
			for (Entry<Integer, DefinicionParametro> entry : inputParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor: " + entry.getValue().getValorCampo());

				SQLDaoMapper.mappingDataToType(dbComando, cont, entry);

				cont++;
			}

		}

	}

	/**
	 * Permite obtener datos que sean distintos de los del resultset
	 * 
	 * @param metadataParametros
	 * 
	 * @param dbComando
	 * @throws SQLException
	 */
	public static List<String> extraerParametrosAdicionales(Map<Integer, String> metadataParametros, CallableStatement dbComando) throws SQLException {

		List<String> res = new ArrayList<>();
		if (metadataParametros.size() > 0) {

			for (Map.Entry<Integer, String> entry : metadataParametros.entrySet()) {

				switch (entry.getValue()) {

				case ConstantesBD.TIPO_VARCHAR:
					res.add(dbComando.getString(entry.getKey()));
					break;

				case ConstantesBD.TIPO_INTEGER:
					res.add(String.valueOf(dbComando.getInt(entry.getKey())));
					break;

				}

			}

		}

		return res;

	}

	/**
	 * Permite setear los parametros de manera dinamica por cada uno de los
	 * procedimientos
	 * 
	 * @param dbComando
	 * @param inputParams
	 *            Parametros de entrada
	 * @param outputParams
	 *            Definicion con los parametros de salida
	 * @throws SQLException
	 */
	public static HashMap<Integer, Map<Integer, String>> setParamsOracle(CallableStatement dbComando, Map<Integer, DefinicionParametro> inputParams,
			Map<String, Integer> outputParams) throws SQLException {

		HashMap<Integer, Map<Integer, String>> res = new HashMap<>();

		if (dbComando != null && inputParams.size() > 0 && outputParams.size() > 0) {

			int posicionCursor = 0;

			// Agregar los parametros de entrada
			log.info("ASIGNAR ENTRADAS DEL SP ORACLE");

			int cont = 1;
			for (Map.Entry<Integer, DefinicionParametro> entry : inputParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor: " + entry.getValue().getValorCampo());

				SQLDaoMapper.mappingDataToType(dbComando, cont, entry);
				cont++;
			}

			/*
			 * Agregar los parametros de salida, se deben de comenzar a agregar los
			 * parametros desde el ultimo que se dejo en los parametros de entrada
			 */
			log.info("ASIGNAR PARAMETROS DE SALIDA DEL SP ORACLE");
			Map<Integer, String> metadataParametros = new HashMap<>();
			for (Map.Entry<String, Integer> entry : outputParams.entrySet()) {

				int fixedLenght = cont;
				log.info("Asignare en la posicion " + fixedLenght + " del SP el valor: " + entry.getValue());

				metadataParametros.put(fixedLenght, entry.getKey());

				dbComando.registerOutParameter(fixedLenght, entry.getValue());

				// Comprobar si este es el cursor de la lista
				if (entry.getKey().equals("CURSOR")) {
					posicionCursor = fixedLenght;
				}

				cont++;

			}

			res.put(posicionCursor, metadataParametros);

		}

		return res;

	}

	/**
	 * Permite setear los parametros de manera dinamica por cada uno de los
	 * procedimientos, SE APLICA CUANDO EL PROCEDURE NO TIENE PARAMETROS DE ENTRADA
	 * 
	 * @param dbComando
	 * @param inputParams
	 *            Parametros de entrada
	 * @param outputParams
	 *            Definicion con los parametros de salida
	 * @throws SQLException
	 */
	public static HashMap<Integer, Map<Integer, String>> setParamsOracleNoInput(CallableStatement dbComando, Map<String, Integer> outputParams) throws SQLException {

		HashMap<Integer, Map<Integer, String>> res = new HashMap<>();

		if (dbComando != null && outputParams.size() > 0) {

			int posicionCursor = 0;

			// Agregar los parametros de entrada
			log.info("ASIGNAR ENTRADAS DEL SP ORACLE");

			int cont = 1;

			/*
			 * Agregar los parametros de salida, se deben de comenzar a agregar los
			 * parametros desde el ultimo que se dejo en los parametros de entrada
			 */
			log.info("ASIGNAR PARAMETROS DE SALIDA DEL SP ORACLE");
			Map<Integer, String> metadataParametros = new HashMap<>();
			for (Map.Entry<String, Integer> entry : outputParams.entrySet()) {

				int fixedLenght = cont;
				log.info("Asignare en la posicion " + fixedLenght + " del SP el valor: " + entry.getValue());

				metadataParametros.put(fixedLenght, entry.getKey());

				dbComando.registerOutParameter(fixedLenght, entry.getValue());

				// Comprobar si este es el cursor de la lista
				if (entry.getKey().equals("CURSOR")) {
					posicionCursor = fixedLenght;
				}

				cont++;

			}

			res.put(posicionCursor, metadataParametros);

		}

		return res;

	}

	/**
	 * Permite setear los parametros de manera dinamica por cada uno de los
	 * procedimientos, SE APLICA CUANDO EL PROCEDURE NO TIENE PARAMETROS DE ENTRADA
	 * 
	 * @param dbComando
	 * @param inputParams
	 *            Parametros de entrada
	 * @param outputParams
	 *            Definicion con los parametros de salida
	 * @throws SQLException
	 */
	public static void setParamsOracleNoInputSinCursor(CallableStatement dbComando, Map<Integer, Integer> outputParams) throws SQLException {

		if (dbComando != null && outputParams.size() > 0) {

			// Agregar los parametros de entrada
			log.info("ASIGNAR ENTRADAS DEL SP ORACLE");

			int cont = 1;

			/*
			 * Agregar los parametros de salida, se deben de comenzar a agregar los
			 * parametros desde el ultimo que se dejo en los parametros de entrada
			 */
			log.info("ASIGNAR PARAMETROS DE SALIDA DEL SP ORACLE");
			for (Map.Entry<Integer, Integer> entry : outputParams.entrySet()) {

				int fixedLenght = cont;
				log.info("Asignare en la posicion " + fixedLenght + " del SP el valor: " + entry.getValue());
				dbComando.registerOutParameter(fixedLenght, entry.getValue());

				cont++;

			}

		}

	}

	/**
	 * Permite Contrastar los datos desde los procedures hacia los que iran a la
	 * base de datos
	 * 
	 * @param dbComando
	 * @param cont
	 * @param entry
	 * @throws SQLException
	 */
	public static void mappingDataToType(CallableStatement dbComando, int cont, Map.Entry<Integer, DefinicionParametro> entry) throws SQLException {

		switch (entry.getValue().getTipoCampo()) {
		case ConstantesBD.TIPO_VARCHAR:
			dbComando.setString(cont, String.valueOf(entry.getValue().getValorCampo()));
			break;

		case ConstantesBD.TIPO_NUMERIC:
			dbComando.setInt(cont, Integer.parseInt(entry.getValue().getValorCampo().toString()));
			break;
			
		case ConstantesBD.TIPO_NUMBER:
			BigDecimal value = new BigDecimal(entry.getValue().getValorCampo().toString());
			dbComando.setBigDecimal(cont, value);
			break;

		case ConstantesBD.TIPO_INTEGER:
			dbComando.setInt(cont, Integer.parseInt(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_FLOAT:
			dbComando.setFloat(cont, Float.parseFloat(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_SHORT:
			dbComando.setShort(cont, Short.parseShort(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_DATE:
			java.sql.Date tipoDataDate = (java.sql.Date) entry.getValue().getValorCampo();
			dbComando.setDate(cont, tipoDataDate);
			break;

		case ConstantesBD.TIPO_DATETIME:
			java.sql.Date tipoData = (java.sql.Date) entry.getValue().getValorCampo();
			dbComando.setDate(cont, tipoData);
			break;

		case ConstantesBD.TIPO_OBJECT:
			dbComando.setObject(cont, entry.getValue());
			break;

		case ConstantesBD.TIPO_LONG:
			dbComando.setLong(cont, Long.parseLong(entry.getValue().getValorCampo().toString()));
			break;

		}
	}

	/**
	 * Permite Contrastar los datos desde los procedures hacia los que iran a la
	 * base de datos
	 * 
	 * @param dbComando
	 * @param cont
	 * @param entry
	 * @throws SQLException
	 */
	public static void mappingDataToType(SQLServerPreparedStatement dbComando, int cont, Map.Entry<Integer, DefinicionParametro> entry) throws SQLException {

		switch (entry.getValue().getTipoCampo()) {
		case ConstantesBD.TIPO_VARCHAR:
			dbComando.setString(cont, String.valueOf(entry.getValue().getValorCampo()));
			break;

		case ConstantesBD.TIPO_NUMERIC:
			dbComando.setInt(cont, Integer.parseInt(entry.getValue().getValorCampo().toString()));
			break;
			
		case ConstantesBD.TIPO_NUMBER:
			BigDecimal value = new BigDecimal(entry.getValue().getValorCampo().toString());
			dbComando.setBigDecimal(cont, value);
			break;

		case ConstantesBD.TIPO_INTEGER:
			dbComando.setInt(cont, Integer.parseInt(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_FLOAT:
			dbComando.setFloat(cont, Float.parseFloat(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_LONG:
			dbComando.setLong(cont, Long.parseLong(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_DATETIME:
			java.sql.Date tipoDataDateTime = (java.sql.Date) entry.getValue().getValorCampo();
			dbComando.setDate(cont, tipoDataDateTime);
			break;

		case ConstantesBD.TIPO_DATE:
			java.sql.Date tipoDataDate = (java.sql.Date) entry.getValue().getValorCampo();
			dbComando.setDate(cont, tipoDataDate);
			break;

		case ConstantesBD.TIPO_STRUCTURE:
			@SuppressWarnings("unchecked")
			Map<String, SQLServerDataTable> definicionData = (Map<String, SQLServerDataTable>) entry.getValue().getValorCampo();

			for (Map.Entry<String, SQLServerDataTable> tipoTabla : definicionData.entrySet()) {
				dbComando.setStructured(cont, tipoTabla.getKey(), tipoTabla.getValue());
				break;
			}

			break;

		case ConstantesBD.TIPO_SHORT:
			dbComando.setShort(cont, Short.parseShort(entry.getValue().getValorCampo().toString()));
			break;

		}

	}

	/**
	 * Permite setear los parametros de manera dinamica por cada uno de los
	 * procedimientos
	 * 
	 * @param dbComando
	 * @throws SQLException
	 */
	public static void setParamsSqlServer(CallableStatement dbComando, Map<Integer, DefinicionParametro> inputParams) throws SQLException {

		if (dbComando != null && inputParams.size() > 0) {

			int cont = 1;
			for (Entry<Integer, DefinicionParametro> entry : inputParams.entrySet()) {
				log.info("Asignare en la posicion " + cont + " del SP el valor: " + entry.getValue().getValorCampo());

				SQLDaoMapper.mappingDataToType(dbComando, cont, entry);

				cont++;
			}

		}

	}

}
