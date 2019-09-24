package cl.modular.apis.rest.services;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import cl.modular.apis.bean.InputPostRequestExample;
import cl.modular.apis.bean.SalidaClientesBusqueda;
import cl.modular.apis.bean.SalidaGenerica;
import cl.modular.apis.bean.SalidaResumenProductos;
import cl.modular.apis.delegate.Delegate;
import cl.modular.apis.delegate.ProductosDelegate;
import cl.modular.apis.filters.Secured;
import cl.modular.apis.filters.ValidaIntegridadRequest;
import cl.modular.apis.helper.json.JsonBodyResponse;
import cl.modular.apis.helper.json.JsonHeaderResponse;

/**
 * Servicio de ejemplo
 * 
 * @author Camilo Contreras
 *
 */
@RequestScoped
@Path("Cliente")
public class ExampleService implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -5943886354174740536L;

	private static final Logger log = Logger.getLogger(ExampleService.class);

	/**
	 * CONSULTA UN SQLSERVER
	 * 
	 * Llamar con
	 * http://localhost:8080/ArquetipoJavaModular/Cliente/ConsultarCliente/60910000?Nombre=UNIVERSIDAD
	 * 
	 * Y con un Header: Token-Authorization :
	 * YXBwdG9vbGJveDphcHB0b29sYm94QHRlbGVmb25pY2EuY29t
	 * 
	 * Obtener el resumen del cliente consultado
	 * 
	 * @return
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@Secured
	@ValidaIntegridadRequest
	@Path("ConsultarCliente/{rut}")
	public SalidaClientesBusqueda consultarUsuario(@PathParam("rut") String rut, @QueryParam("Nombre") String inputName) {

		log.info("Se ha generado una solicitud al servicio [ConsultarCliente]");
		log.info("[REQUEST] RUT: " + rut + " , NOMBRE: " + inputName);

		Delegate conector = new Delegate();
		SalidaClientesBusqueda res = conector.obtenerInfoCliente(rut, inputName);

		log.info("Se ha finalizado una solicitud al servicio [ConsultarCliente]");
		log.info("[RESPONSE]");
		log.info(new Gson().toJson(res));
		return res;

	}

	/**
	 * Consulta un ORACLE
	 * 
	 * Llamar con
	 * http://localhost:8080/ArquetipoJavaModular/Cliente/ConsultarProductos/16137752K
	 * 
	 * Y con un Header: Token-Authorization :
	 * YXBwdG9vbGJveDphcHB0b29sYm94QHRlbGVmb25pY2EuY29t
	 * 
	 * Resumen de productos contratados por el cliente
	 * 
	 * @return
	 */
	@GET
	@Path("ConsultarProductos/{prut}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public SalidaResumenProductos resumenProductos(@PathParam("prut") String inputRut) {

		log.info("Se inicia una solicitud hacia el metodo: Rest Template ArquetipoJavaModular, Resumen Productos");
		SalidaResumenProductos res = new SalidaResumenProductos();

		ProductosDelegate delegate = new ProductosDelegate();
		res = delegate.resumenProductos(inputRut);

		log.info("Solicitud finalizada al metodo: Rest Template ArquetipoJavaModular, Resumen Productos");
		log.info(new Gson().toJson(res));

		return res;

	}

	/**
	 * Entrada de ejemplo para probar la ruta de entrada con el SP
	 * 
	 * @param inputRut
	 * @return
	 */
	@POST
	@Path("ConsultarDataSensibleEjemploFiltro")
	@Secured
	@ValidaIntegridadRequest
	@Produces(MediaType.APPLICATION_JSON)
	public SalidaGenerica postExampleInput(InputPostRequestExample input) {

		log.info("Se inicia una solicitud hacia el metodo: [Ejemplo Filtro Validacion SP Cuenta-RUT]");
		log.info("REQUEST: " + new Gson().toJson(input));
		SalidaGenerica res = new SalidaGenerica();

		log.info("Solicitud autorizada");
		res.setDtoResponseCodigosEstadoHttp(JsonHeaderResponse.requestOK());
		res.setDtoResponseSetParametros(JsonBodyResponse.okTransaccion());

		log.info("Solicitud finalizada al metodo: [Ejemplo Filtro Validacion SP Cuenta-RUT]");
		log.info(new Gson().toJson(res));

		return res;

	}

}
