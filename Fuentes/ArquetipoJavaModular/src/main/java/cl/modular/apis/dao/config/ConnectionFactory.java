package cl.modular.apis.dao.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Administrar la conexion a la base de datos por hebra
 * 
 * @author Camilo Contreras
 *
 */
public final class ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class);
	private static ConnectionFactory instance = null;
	private static DataSource dataSource = null;

	/**
	 * Crea la clase e inicializa el datasource
	 * 
	 * @throws SQLException
	 */
	private ConnectionFactory(String datasource) throws SQLException {

		try {

			Context ctx = new InitialContext();
			ConnectionFactory.dataSource = (DataSource) ctx.lookup(datasource);
			LOGGER.info("Datasource '" + datasource + "' conectado correctamente");

		} catch (NamingException e) {
			LOGGER.error("Error al obtener coneccion: " + e, e);
			throw new SQLException();
		}

	}

	/**
	 * Obtiene una conexion
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection obtenerConexion(String datasource) throws SQLException {

		// Si esta nula crearla
		if (ConnectionFactory.instance == null) {
			ConnectionFactory.instance = new ConnectionFactory(datasource);
		} else {

			// Si no esta nula, nulearla y crear otra
			ConnectionFactory.instance = null;
			ConnectionFactory.instance = new ConnectionFactory(datasource);
		}

		Connection conn = ConnectionFactory.dataSource.getConnection();

		return conn;
	}
}
