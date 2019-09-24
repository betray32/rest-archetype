package cl.modular.apis.soap.client;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;

import com.modular.ws.client.sms.Mensajeria;
import com.modular.ws.client.sms.MensajeriaSoap;

import cl.modular.apis.helper.LogMessageHandler;

/**
 * Este es un servicio de ejemplo para el arquetipo, permite conectar el cliente
 * SOAP generado automaticamente con las demas capas del proyecto
 * 
 * Consumer para el servicio de SMS
 * 
 * @author Camilo Contreras
 *
 */
public class ConsumerSMSClient implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 826800620752488432L;

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = Logger.getLogger(ConsumerSMSClient.class);

	/**
	 * Enviar el sms
	 * 
	 * @return
	 */
	public int enviarSMS(String mensaje, String numeroCelular) {

		try {
			LOGGER.info("Conectando al servicio de SMS...");
			LOGGER.info("Endpoint: #RUTA_ENDPOINT");

			// Indicar donde esta el WSDL dentro del proyecto y referenciarlo
			URL wsdlUrl = Thread.currentThread().getContextClassLoader().getResource("wsdl/Mensajeria.wsdl");
			Mensajeria service = new Mensajeria(wsdlUrl);
			MensajeriaSoap portType = service.getMensajeriaSoap();

			// Apuntar el servicio hacia el ENDPOINT deseado
			BindingProvider bp = (BindingProvider) portType;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "#RUTA_ENDPOINT_MODIFICAR!");

			// Configurar y activar el Logger para este Servicio
			Binding binding = bp.getBinding();
			@SuppressWarnings("rawtypes")
			List<Handler> handlerChain = binding.getHandlerChain();
			handlerChain.add(new LogMessageHandler());
			binding.setHandlerChain(handlerChain);

			LOGGER.info("Conexion OK, Conectando al metodo de EnvioSMS...");

			// Invocar el servicio
			String res = portType.envioSMS(mensaje, numeroCelular);

			// Obtener la respuesta
			LOGGER.info("Metodo OK, Responde: " + res);
			return Integer.parseInt(res);

		} catch (Exception e) {
			LOGGER.error("Error al consumir el cliente de SMS, ERROR > " + e.getMessage());
			LOGGER.error("Traza: ", e);
			return -1;
		}

	}
}
