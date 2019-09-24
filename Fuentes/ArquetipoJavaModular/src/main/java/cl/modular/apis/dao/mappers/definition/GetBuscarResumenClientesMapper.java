package cl.modular.apis.dao.mappers.definition;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.DtoClienteBuscarResumenClientes;
import cl.modular.apis.dao.mappers.schema.RowMapper;

/**
 * Obtiene informacion en modo Resumen del Cliente
 * 
 * @author Camilo Contreras
 *
 */
public class GetBuscarResumenClientesMapper implements RowMapper<DtoClienteBuscarResumenClientes> {

	private static final Logger log = Logger.getLogger(GetBuscarResumenClientesMapper.class);

	/**
	 * Obtener los datos de respuesta para el procedure
	 * RES_GET_BUSCAR_RESUMEN_CLIENTES
	 */
	public DtoClienteBuscarResumenClientes mapper(ResultSet paramResultSet) throws SQLException {

		try {

			if (paramResultSet != null) {

				DtoClienteBuscarResumenClientes o = new DtoClienteBuscarResumenClientes();
				o.setRut(paramResultSet.getString(1));
				o.setNombre(paramResultSet.getString(2));
				o.setTp(paramResultSet.getString(3));
				o.setIngreso(paramResultSet.getString(4));
				o.setBanca(paramResultSet.getString(5));
				o.setEjecutivo(paramResultSet.getString(6));

				return o;

			}

		} catch (Exception e) {
			log.error("Error al obtener los datos desde el ResultSet, ERROR: " + e.getMessage());
		}

		return null;
	}

}
