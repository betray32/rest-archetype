package cl.modular.apis.helper;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * Clase que implementa el logger para los servicios, REQUEST y RESPONSE
 * 
 * @author c.contreras.caceres
 * @date 11-08-2016
 *
 */
public class LogMessageHandler implements SOAPHandler<SOAPMessageContext> {

	/**
	 * Se instancia el Logger del portal
	 */
	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogMessageHandler.class);

	/**
	 * Loguea todos los mensajes
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		SOAPMessage msg = context.getMessage();

		try {

			/*
			 * Se obtiene la respuesta y se transforma a un String para poder
			 * almacenarlo en el logger y mostrarlo por consola
			 */
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			msg.writeTo(out);
			String mensajeSoap = new String(out.toByteArray());

			log.info(mensajeSoap);

		} catch (Exception ex) {
			log.error("Error al registrar el log del servicio, ERROR: " + ex.getMessage());
		}

		return true;

	}

	@Override
	public void close(javax.xml.ws.handler.MessageContext arg0) {

	}

	@Override
	public boolean handleFault(SOAPMessageContext arg0) {

		return true;
	}

	@Override
	public Set<QName> getHeaders() {

		return null;
	}

}
