package cl.modular.apis.res;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Clase para la configuracion de la aplicacion
 * 
 * @author Camilo Contreras
 *
 */
public final class ResourcesConfig {

	private ResourcesConfig() {
		// sin implementacion
	}

	/**
	 * Nombre del archivo con la configuracion del Log4j
	 */
	private static final String LOG4J_FILE_NAME = "ArquetipoJavaModular-log4j.properties";

	/**
	 * Nombre del archivo con la configuracion propia de la aplicacion
	 */
	private static final String RESOURCE_FILE_NAME = "ArquetipoJavaModular-resource.properties";

	/**
	 * Inicia todos los properties de la aplicacion
	 */
	public static void config() {

		URL urlLog4j = ResourcesConfig.getUrlfile(ResourcesConfig.LOG4J_FILE_NAME);
		URL urlProps = ResourcesConfig.getUrlfile(ResourcesConfig.RESOURCE_FILE_NAME);

		validarProperties(urlLog4j, urlProps);

		// Inicializar LOG4J
		PropertyConfigurator.configure(urlLog4j);
		Logger logger = Logger.getLogger(ResourcesConfig.class);
		logger.debug("log4j configurado: " + urlLog4j);

		try {

			// Inicializar Properties del aplicativo
			Resources.init(urlProps);
			logger.debug("recursos configurado: " + urlProps);

		} catch (IOException e) {
			logger.error("Error resources config: " + e, e);
			throw new NotInitResourceException();
		}
	}

	/**
	 * Valida que los properties existan
	 * 
	 * @param urlLog4j
	 *            archivo log4j
	 * @param urlProps
	 *            archivo resources
	 * @param urlPropsCred
	 *            archivo credential
	 */
	private static void validarProperties(URL urlLog4j, URL urlProps) {
		boolean loadok = true;
		StringBuilder mensajeError = new StringBuilder();

		if (urlLog4j == null) {
			loadok = false;
			mensajeError.append("Archivo log4j no encontrado: " + ResourcesConfig.LOG4J_FILE_NAME + ".\n");
		}

		if (urlProps == null) {
			loadok = false;
			mensajeError.append("Archivo properties no encontrado: " + ResourcesConfig.RESOURCE_FILE_NAME + ".\n");
		}

		if (loadok == false) {
			throw new NotInitResourceException(mensajeError.toString() + " No encontrado en CLASSPATH: " + getClassPath());
		}
	}

	/**
	 * busca el archivo filename en el classpath del servidor
	 * 
	 * @param filename
	 * @return
	 */
	public static URL getUrlfile(String filename) {
		URL fileURL = ResourcesConfig.class.getClassLoader().getResource(filename);
		if (fileURL == null) {
			fileURL = ResourcesConfig.class.getResource("/" + filename);
		}
		return fileURL;
	}

	/**
	 * Retorna el ClassPath usado
	 * 
	 * @return
	 */
	private static String getClassPath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		URL[] urls = ((URLClassLoader) cl).getURLs();

		StringBuilder sb = new StringBuilder();
		for (URL url : urls) {
			sb.append(url.getFile());
			sb.append(",\n");
		}
		return sb.toString();
	}
}
