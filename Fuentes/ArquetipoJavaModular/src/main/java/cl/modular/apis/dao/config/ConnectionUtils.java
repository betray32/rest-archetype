/**
 * 
 */
package cl.modular.apis.dao.config;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.helper.ConstantesBD;

/**
 * @author ccontrerasc
 *
 */
public class ConnectionUtils {

	/**
	 * Permite obtener datos que sean distintos de los del resultset, para ello
	 * busca datos que sean distintos de cursor y los mete a una lista que
	 * posteriormente devolvera hacia el dao que lo este consultando
	 * 
	 * @param dbComando
	 * @throws SQLException
	 */
	public static List<String> extraerParametrosAdicionales(Map<Integer, String> metadataParametros, CallableStatement dbComando) throws SQLException {

		List<String> salidaCompuesta = null;

		if (metadataParametros.size() > 0) {

			salidaCompuesta = new ArrayList<>();
			for (Map.Entry<Integer, String> entry : metadataParametros.entrySet()) {

				switch (entry.getValue()) {
				
				case ConstantesBD.TIPO_VARCHAR:
					salidaCompuesta.add(dbComando.getString(entry.getKey()));
					break;

				case ConstantesBD.TIPO_INTEGER:
					salidaCompuesta.add(String.valueOf(dbComando.getInt(entry.getKey())));
					break;
					
				case ConstantesBD.TIPO_DATETIME:
					salidaCompuesta.add(String.valueOf(dbComando.getDate(entry.getKey())));
					break;
					
				case ConstantesBD.TIPO_FLOAT:
					salidaCompuesta.add(String.valueOf(dbComando.getFloat(entry.getKey())));
					break;

				}

			}

		}

		return salidaCompuesta;

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
			
		case ConstantesBD.TIPO_INTEGER:
			dbComando.setInt(cont, Integer.parseInt(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_FLOAT:
			dbComando.setFloat(cont, Float.parseFloat(entry.getValue().getValorCampo().toString()));
			break;

		case ConstantesBD.TIPO_OBJECT:
			dbComando.setObject(cont, entry.getValue().getValorCampo());
			break;

		}
	}

}
