/**
 * 
 */
package cl.modular.apis.delegate;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.SalidaResumenProductos;
import cl.modular.apis.helper.Utiles;
import cl.modular.apis.helper.Validadores;
import cl.modular.apis.helper.json.JsonHeaderResponse;
import cl.modular.apis.mgr.GetProductosDataMgr;
import cl.modular.apis.mgr.impl.GetProductosDataMgrImpl;

/**
 * Permite conectar la vista con la logica de negocio para 'Productos' del cliente
 * 
 * @author ccontrerasc
 *
 */
public class ProductosDelegate {

	/**
	 * LOGGER
	 */
	private static final Logger log = Logger.getLogger(ProductosDelegate.class);

	/**
	 * Resumen de Productos
	 * 
	 * @param pRut
	 * @return
	 */
	public SalidaResumenProductos resumenProductos(String pRut) {

		SalidaResumenProductos res = new SalidaResumenProductos();
		GetProductosDataMgr data = new GetProductosDataMgrImpl();

		if (!Validadores.validarRut(pRut)) {
			res.setDtoresponsecodigosestadohttp(JsonHeaderResponse.noAutorizada());
			res.setDtoresponsesetresultados(null);
			log.info("Input incorrecto");
		} else {
			String rutLimpio = Utiles.limpiaRut(pRut);
			res = data.productosResumen(rutLimpio);
		}

		return res;

	}

}
