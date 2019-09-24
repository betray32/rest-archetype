package cl.modular.apis.mgr.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.modular.apis.bean.DefinicionParametro;
import cl.modular.apis.bean.DtoClienteBuscarResumenClientes;
import cl.modular.apis.bean.DtoResponseSetResultadosResumenClientes;
import cl.modular.apis.bean.SalidaClientesBusqueda;
import cl.modular.apis.dao.QueryDao;
import cl.modular.apis.dao.impl.QueryDaoImpl;
import cl.modular.apis.dao.mappers.definition.GetBuscarResumenClientesMapper;
import cl.modular.apis.helper.Constantes;
import cl.modular.apis.helper.ConstantesBD;
import cl.modular.apis.helper.Validadores;
import cl.modular.apis.helper.json.JsonHeaderResponse;
import cl.modular.apis.mgr.ObtieneDataMgr;

/**
 * Implementa la interface para obtener la data, Aca se debe implementar la
 * logica de negocio, reglas unicas sobre el metodo que estamos tratando,
 * llamadas a procedures (daos) o web services
 * 
 * @author Camilo Contreras
 *
 */
public class ObtieneDataMgrImpl implements ObtieneDataMgr {

	/**
	 * LOGGER
	 */
	private static final Logger log = Logger.getLogger(ObtieneDataMgrImpl.class);

	/**
	 * Objeto de dao
	 */
	private QueryDao dataCliente;

	/**
	 * Conecta con la capa de DAO, este consulta una base de datos SQLSERVER
	 */
	@Override
	public SalidaClientesBusqueda obtieneData(String rut, String nombre) {

		SalidaClientesBusqueda res = new SalidaClientesBusqueda();

		// Validamos con el metodo estandard la entrada
		if (Validadores.parametrosEntradaIncorrectosUnCampo(rut)) {
			res.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.faltanParametrosEntrada());
			log.error("Faltan parametros de entrada para el servicio");
			return res;
		}

		// Inicializar objeto para consultar el procedimiento
		dataCliente = new QueryDaoImpl();

		// Parametros de entrada
		LinkedHashMap<Integer, DefinicionParametro> inParams = new LinkedHashMap<>();
		inParams.put(1, new DefinicionParametro(ConstantesBD.TIPO_VARCHAR, rut));
		inParams.put(2, new DefinicionParametro(ConstantesBD.TIPO_VARCHAR, nombre));
		inParams.put(3, new DefinicionParametro(ConstantesBD.TIPO_VARCHAR, ""));

		// Llamada al servicio
		List<DtoClienteBuscarResumenClientes> responseList = dataCliente.queryForProcedureSqlServer(Constantes.DATASOURCE_SQLSERVER, inParams, new GetBuscarResumenClientesMapper(),
				Constantes.PROCEDURE_DEFINICION_CLIENTE_RESUMEN);

		int codRetorno = dataCliente.codRetorno();

		/*
		 * Codigo -1: Indica error de conectividad con el repositorio.
		 */
		if (codRetorno == -1) {

			log.info("Error al consultar el servicio");
			res.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.internalServerError());

		} else {

			// Tratamiento de la respuesta
			if (responseList.size() > 0) {

				log.info("El largo de la lista resultante es: " + responseList.size());

				// Cabecera
				res.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.requestOK());

				// Cuerpo
				DtoResponseSetResultadosResumenClientes bodyObject = new DtoResponseSetResultadosResumenClientes();
				bodyObject.setDtocliente(responseList);
				res.setDtoresponsesetresultados(bodyObject);

			} else {

				log.info("El largo de la lista de respuesta al servicio es 0");
				res.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.sinContenidoSalida());
			}
		}

		return res;

	}

}
