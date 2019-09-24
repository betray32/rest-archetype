package cl.modular.apis.mgr.impl;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.dao.QueryDao;
import cl.modular.apis.dao.impl.QueryDaoImpl;
import cl.modular.apis.helper.Constantes;
import cl.modular.apis.helper.ConstantesBD;
import cl.modular.apis.mgr.ValidacionMetadataMgr;

/**
 * Permite validar la data en contraste al SP
 * 
 * @author Camilo Contreras
 *
 */
public class ValidacionMetadataMgrImpl implements ValidacionMetadataMgr {

	/**
	 * LOG
	 */
	private static final Logger log = Logger.getLogger(ValidacionMetadataMgrImpl.class);

	/**
	 * Objeto
	 */
	private QueryDao dataCliente;

	/**
	 * Consulta SP para validacion de data enviada por filtro
	 */
	@Override
	public boolean validacionMetadataFiltro(String rut, String nroProducto, int tipoProducto) {

		log.info("Se validara la Metadata en contraste a un SP");

		// Inicializar objeto para consultar el procedimiento
		dataCliente = new QueryDaoImpl();

		// Parametros de entrada
		LinkedHashMap<Integer, DefinicionParametro> inParams = new LinkedHashMap<>();
		inParams.put(1, new DefinicionParametro(ConstantesBD.TIPO_VARCHAR, rut));
		inParams.put(2, new DefinicionParametro(ConstantesBD.TIPO_VARCHAR, nroProducto));
		inParams.put(3, new DefinicionParametro(ConstantesBD.TIPO_INTEGER, tipoProducto));

		/*
		 * Se indican los parametros de salida para el procedure
		 */
		LinkedHashMap<Integer, Integer> outParams = new LinkedHashMap<>();
		outParams.put(4, java.sql.Types.NUMERIC);
		outParams.put(5, java.sql.Types.NUMERIC);
		outParams.put(6, java.sql.Types.VARCHAR);

		LinkedHashMap<Integer, Object> data = dataCliente.procedureOracleSinCursor(Constantes.DATASOURCE_ORACLE_METADATA, inParams, outParams,
				Constantes.PROCEDURE_DEFINICION_VALIDA_METADATA);

		if (data.size() > 0) {

			int respuesta = (Integer) data.get(4);

			if (respuesta == 1) {
				log.info("Metadata Validada Correctamente");
				return true;
			} else {
				log.error("Metada invalida por SP");
			}

		}

		return false;

	}

}
