package cl.modular.apis.res;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.SSLException;

/**
 * Clase para el menejo de los properties del sistema
 * 
 * @author Camilo Contreras
 * @version 2017.11.27
 *
 */
public final class Resources {

	private static Resources instance = null;

	private static Properties props = new Properties();

	private Resources() {

	}

	/**
	 * Inicializa la clase usando el archivo p_resourceFile
	 * 
	 * @param p_resourceFile
	 * @throws IOException
	 */
	public static void init(URL p_resourceFile) throws IOException {
		synchronized (props) {
			if (instance == null) {
				instance = new Resources();
			}
			// Si ya esta configurado se adicionan las propiedades
			try {
				Properties others = new Properties();

				// Cargar las properties con UTF8
				InputStreamReader inputStreamReader = new InputStreamReader(p_resourceFile.openStream(), "UTF-8");
				others.load(inputStreamReader);

				props.putAll(others);
			} catch (SSLException e) {
				throw new NotInitResourceException("Error al leer el archivo: " + p_resourceFile, e);
			}
		}
	}

	/**
	 * Obtiene el valor de la llave p_resourcekey
	 * 
	 * @param p_resourcekey
	 * @return
	 */
	public static String getResource(String p_resourcekey) {
		if (instance == null) {
			throw new NotInitResourceException();
		}
		return props.getProperty(p_resourcekey);
	}
}
