package cl.modular.apis.delegate;

import cl.modular.apis.mgr.ValidacionMetadataMgr;
import cl.modular.apis.mgr.impl.ValidacionMetadataMgrImpl;

/**
 * Validate metadata
 * 
 * @author Camilo Contreras
 *
 */
public class DelegateValidacionMetadata {

	/**
	 * Permite validar en contraste al SP la metadata enviada
	 * 
	 * @param rut
	 * @param nroProducto
	 * @return
	 */
	public boolean validacionMetadataFiltro(String rut, String nroProducto, int tipoProducto) {

		ValidacionMetadataMgr validacionMetadata = new ValidacionMetadataMgrImpl();
		return validacionMetadata.validacionMetadataFiltro(rut, nroProducto, tipoProducto);

	}

}
