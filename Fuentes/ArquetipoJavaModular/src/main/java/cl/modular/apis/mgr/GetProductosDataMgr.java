package cl.modular.apis.mgr;

import cl.modular.apis.bean.SalidaResumenProductos;

/**
 * Capa intermedia para Productos
 * 
 * @author ccontrerasc
 *
 */
public interface GetProductosDataMgr {

	/**
	 * Permite consultar por los resumen de los productos
	 * 
	 * @param rut
	 *            InputRut
	 * @return Objeto complejo con el resultado de la transaccion
	 */
	public SalidaResumenProductos productosResumen(String rut);

}
