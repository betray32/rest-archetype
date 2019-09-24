package cl.modular.apis.delegate;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.SalidaClientesBusqueda;
import cl.modular.apis.helper.Validadores;
import cl.modular.apis.helper.json.JsonHeaderResponse;
import cl.modular.apis.mgr.ObtieneDataMgr;
import cl.modular.apis.mgr.impl.ObtieneDataMgrImpl;

/**
 * Permite hacer contacto entre la capa de presentacion y las capas inferiores
 * 
 * @author Camilo Contreras
 *
 */
public class Delegate {

	/**
	 * LOGGER
	 */
	private static final Logger log = Logger.getLogger(Delegate.class);

	/**
	 * Permite obtener informacion del cliente
	 * 
	 * @return
	 */
	public SalidaClientesBusqueda obtenerInfoCliente(String rut, String nombre) {

		SalidaClientesBusqueda salida = new SalidaClientesBusqueda();
		ObtieneDataMgr capaDao = new ObtieneDataMgrImpl();

		// Validamos con el metodo estandard la entrada
		if (!Validadores.validarRut(rut) || nombre.length() >= 50) {
			salida.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.internalServerError());
			salida.setDtoresponsesetresultados(null);
			log.info("Input incorrecto");
		} else {
			salida = capaDao.obtieneData(rut, nombre);
		}

		return salida;

	}

}
