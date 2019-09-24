package cl.modular.apis.mgr;

import cl.modular.apis.bean.SalidaClientesBusqueda;

/**
 * Clase que permite servir como capa intermedia
 * 
 * @author Camilo Contreras
 *
 */
public interface ObtieneDataMgr {

	/**
	 * Permite obtener de la cuenta del usuario
	 * 
	 * @return
	 */
	public SalidaClientesBusqueda obtieneData(String rut, String nombre);

}
