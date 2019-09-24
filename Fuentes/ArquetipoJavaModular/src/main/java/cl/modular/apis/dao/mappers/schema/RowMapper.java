package cl.modular.apis.dao.mappers.schema;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase para el transformar los ResultSet en los VO correspondientes
 * 
 * @author Camilo Contreras
 *
 * @param <T>
 */
public interface RowMapper<T> {

	/**
	 * Toma el ResultSet y lo transforma en T
	 * 
	 * @param paramResultSet
	 * @return
	 * @throws SQLException
	 */
	public abstract T mapper(ResultSet paramResultSet) throws SQLException;
}
