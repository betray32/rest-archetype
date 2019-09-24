package cl.modular.apis.mgr.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.bean.DtoClienteResumenProductos;
import cl.modular.apis.bean.DtoProductosContratados;
import cl.modular.apis.bean.DtoResponseSetResultadosResumenProductos;
import cl.modular.apis.bean.SalidaResumenProductos;
import cl.modular.apis.dao.QueryDao;
import cl.modular.apis.dao.impl.QueryDaoImpl;
import cl.modular.apis.dao.mappers.definition.GetProductosResumenMapper;
import cl.modular.apis.helper.Constantes;
import cl.modular.apis.helper.ConstantesBD;
import cl.modular.apis.helper.Validadores;
import cl.modular.apis.helper.json.JsonHeaderResponse;
import cl.modular.apis.mgr.GetProductosDataMgr;

/**
 * Implementacion de los productos data MGR
 * 
 * @author ccontrerasc
 *
 */
public class GetProductosDataMgrImpl implements GetProductosDataMgr {

	/**
	 * LOGGER
	 */
	private static final Logger log = Logger.getLogger(GetProductosDataMgrImpl.class);

	/**
	 * Objeto
	 */
	private QueryDao dataCliente;

	/**
	 * Conecta con la capa de DAO, Consulta una base de datos ORACLE
	 */
	@Override
	public SalidaResumenProductos productosResumen(String rut) {

		SalidaResumenProductos res = new SalidaResumenProductos();

		// Validar los datos de entrada
		if (Validadores.parametrosEntradaIncorrectosUnCampo(rut)) {
			res.setDtoresponsecodigosestadohttp(JsonHeaderResponse.faltanParametrosEntrada());
			log.error("Faltan parametros de entrada para el servicio");
			return res;
		}

		// Inicializar objeto para consultar el procedimiento
		dataCliente = new QueryDaoImpl();

		// Parametros de entrada
		LinkedHashMap<Integer, DefinicionParametro> inParams = new LinkedHashMap<>();
		inParams.put(1, new DefinicionParametro(ConstantesBD.TIPO_VARCHAR, rut));

		/*
		 * Ya que oracle utiliza parametros de salida en los procedure, se deben de
		 * indicar los tipos de datos para las salidas de los servicios
		 */
		Map<String, Integer> outParams = new HashMap<>();
		outParams.put(ConstantesBD.TIPO_CURSOR, ConstantesBD.ORACLE_CURSOR_CODE);

		List<DtoProductosContratados> data = dataCliente.queryForProcedureOracle(Constantes.DATASOURCE_ORACLE, inParams, outParams, new GetProductosResumenMapper(),
				Constantes.PROCEDURE_DEFINICION_PRODUCTOS_RESUMEN);

		int codRetorno = dataCliente.codRetorno();

		/*
		 * Codigo -1: Indica error de conectividad con el repositorio.
		 */
		if (codRetorno == -1) {

			log.info("Error al consultar el servicio");
			res.setDtoresponsecodigosestadohttp(JsonHeaderResponse.internalServerError());

		} else {

			if (data.size() > 0) {

				log.info("El largo de la lista resultante es: " + data.size());

				// Cabecera
				res.setDtoresponsecodigosestadohttp(JsonHeaderResponse.requestOK());

				// Cuerpo
				DtoResponseSetResultadosResumenProductos bodyObject = new DtoResponseSetResultadosResumenProductos();
				DtoClienteResumenProductos contenedorLista = new DtoClienteResumenProductos();
				contenedorLista.setDtosproductoscontratados(data);
				bodyObject.setDtocliente(contenedorLista);
				res.setDtoresponsesetresultados(bodyObject);

			} else {

				log.info("El largo de la lista de respuesta al servicio es 0");
				res.setDtoresponsecodigosestadohttp(JsonHeaderResponse.sinContenidoSalida());
			}
		}

		return res;

	}

}
