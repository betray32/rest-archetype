package cl.modular.apis.dao.mappers.definition;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.DtoProductosContratados;
import cl.modular.apis.dao.mappers.schema.RowMapper;

/**
 * Mapea la salida para el procedure
 * 
 * @author ccontrerasc
 *
 */
public class GetProductosResumenMapper implements RowMapper<DtoProductosContratados> {

	private static final Logger log = Logger.getLogger(GetProductosResumenMapper.class);

	/**
	 * Obtener los datos de respuesta para el procedure
	 * RES_GET_BUSCAR_RESUMEN_CLIENTES
	 */
	public DtoProductosContratados mapper(ResultSet paramResultSet) throws SQLException {

		try {

			if (paramResultSet != null) {

				DtoProductosContratados o = new DtoProductosContratados();
				o.setModulo(paramResultSet.getString(1));
				o.setProducto(paramResultSet.getString(2));
				o.setTipo(paramResultSet.getString(3));
				o.setMoneda(paramResultSet.getString(4));
				o.setOperacion(paramResultSet.getString(5));
				o.setTipoproducto(paramResultSet.getString(6));
				o.setDescripcion(paramResultSet.getString(7));
				o.setSaldo(paramResultSet.getString(8));
				o.setCupo(paramResultSet.getString(9));
				o.setDeuda(paramResultSet.getString(10));

				return o;

			}

		} catch (Exception e) {
			log.error("Error al obtener los datos desde el ResultSet, ERROR: " + e.getMessage());
		}

		return null;
	}

}
