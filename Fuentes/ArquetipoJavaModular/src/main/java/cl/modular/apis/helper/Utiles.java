package cl.modular.apis.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXB;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.modular.apis.bean.ListadoPalabrasProhibidas;
import cl.modular.apis.res.ResourcesConfig;

/**
 * Utiles
 * 
 * @author Camilo Contreras
 *
 */
public class Utiles {

	/**
	 * Diccionario de palabras
	 */
	private static List<String> LISTA_PALABRAS_PROHIBIDAS = new ArrayList<>();
	private static List<String> LISTA_CARACTERES_PROHIBIDOS = new ArrayList<>();
	private static ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Mensajes
	 */
	private static final String EVALUAR_POST = "Procediendo a validar integridad de request [POST]";
	private static final String EVALUAR_GET = "Procediendo a validar integridad de request [GET]";
	private static final String ALERTA_PALABRA_ENCONTRADA = "[ALERTA] Palabra prohibida encontrada : ";
	private static final String ALERTA_BYPASS_DICCIONARIO = "[ALERTA] BYPASS VALIDACION CONTRA DICCIONARIO PALABRAS RESERVADAS - NO SE PUDO CARGAR";
	private static final String ALERTA_BYPASS_CARACTERES = "[ALERTA] BYPASS CARACTERES PROHIBIDOS - NO SE PUDO CARGAR";
	private static final String ALERTA_CARACTER_NO_PERMITIDO_ENCONTRADO = "[ALERTA] Caracter no permitido encontrado: ";

	/**
	 * LOG
	 */
	private static final Logger log = Logger.getLogger(Utiles.class);

	/**
	 * Cargar lista
	 */
	static {
		try {
			URL rutaDiccionario = ResourcesConfig.getUrlfile(Constantes.ARCHIVO_PALABRAS_RESERVADAS);
			ListadoPalabrasProhibidas palabrasProhibidas = MAPPER.readValue(rutaDiccionario, ListadoPalabrasProhibidas.class);

			LISTA_PALABRAS_PROHIBIDAS.addAll(palabrasProhibidas.getListadoPalabrasProhibidas());
			log.info("Diccionario Palabras Prohibidas Cargado Correctamente");

			LISTA_CARACTERES_PROHIBIDOS = palabrasProhibidas.getListadoCaracteresProhibidos();
			log.info("Diccionario Caracteres Prohibidos Cargado Correctamente");

		} catch (Exception e) {
			log.error("ERROR AL CARGAR VALIDACION METADATA, ERROR > " + Utiles.obtenerExcepcionRaiz(e));
		}
	}

	/**
	 * Permite validar si la cadena de entrada posee algun caracter no reconocido,
	 * esto se realiza para filtar por ethical hacking e inyecciones, por ende se
	 * toma todo el request y se validan unicamente elementos de json tales como
	 * {},:" , etc ademas de letras y numeros
	 * 
	 * 
	 * @param input
	 * @return
	 */
	public static boolean validarEntradaRequestPost(String input) {

		log.info(EVALUAR_POST);

		// Limpiar request de espacios, tabuladores y saltos de linea
		input = input.replaceAll("[\\n\\t ]", "");
		input = input.toLowerCase();

		boolean contieneCaracteresProhibidos = false;
		boolean contienePalabrasProhibidas = false;

		contieneCaracteresProhibidos = validarCaracteresProhibidos(input, contieneCaracteresProhibidos);
		contienePalabrasProhibidas = validarPalabrasProhibidas(input, contienePalabrasProhibidas);

		log.info("Contiene Caracteres Prohibidos : " + contieneCaracteresProhibidos);
		log.info("Contiene Palabras Prohibidas : " + contienePalabrasProhibidas);

		// Salida
		if (!contieneCaracteresProhibidos & !contienePalabrasProhibidas)
			return true;
		else
			return false;

	}

	/**
	 * Buscar palabras prohibidas
	 * 
	 * @param input
	 * @param contienePalabrasProhibidas
	 * @return
	 */
	private static boolean validarPalabrasProhibidas(String input, boolean contienePalabrasProhibidas) {

		if (LISTA_PALABRAS_PROHIBIDAS.isEmpty()) {
			log.error(ALERTA_BYPASS_DICCIONARIO);
		} else {

			// Validar si el request contiene palabras reservadas
			for (String word : LISTA_PALABRAS_PROHIBIDAS) {

				word = word.toLowerCase();

				if (contieneStringExacta(input, word)) {
					contienePalabrasProhibidas = true;
					log.info(ALERTA_PALABRA_ENCONTRADA + word);
					break;
				}
			}

		}

		return contienePalabrasProhibidas;
	}

	/**
	 * Buscar caracteres prohibidos
	 * 
	 * @param input
	 * @param contieneCaracteresProhibidos
	 * @return
	 */
	private static boolean validarCaracteresProhibidos(String input, boolean contieneCaracteresProhibidos) {

		if (LISTA_CARACTERES_PROHIBIDOS.isEmpty()) {
			log.error(ALERTA_BYPASS_CARACTERES);
		} else {

			// Validar si el request contiene palabras reservadas
			for (String word : LISTA_CARACTERES_PROHIBIDOS) {

				word = word.toLowerCase();

				if (input.contains(word)) {
					contieneCaracteresProhibidos = true;
					log.info(ALERTA_CARACTER_NO_PERMITIDO_ENCONTRADO + word);
					break;
				}
			}

		}

		return contieneCaracteresProhibidos;
	}

	/**
	 * Permite evaluar los parametros por get y tomar acciones en el caso de que
	 * estos no fueran permitidos
	 * 
	 * @param pathparam
	 * @param queryParam
	 * @return
	 */
	public static boolean validarEntradaRequestGet(MultivaluedMap<String, String> pathparam, MultivaluedMap<String, String> queryParam) {

		log.info(EVALUAR_GET);
		boolean requestValido = true;

		log.info("Evaluando PathParams...");
		for (Map.Entry<String, List<String>> entry : pathparam.entrySet()) {

			String key = entry.getKey();
			log.info("Key: " + key);

			List<String> valores = entry.getValue();
			String rut = valores.get(0).toLowerCase();

			log.info("Value : " + rut);

			requestValido = validarElementosGET(requestValido, rut);

		}

		log.info("Evaluando QueryParams...");
		for (Map.Entry<String, List<String>> entry : queryParam.entrySet()) {

			String key = entry.getKey();
			log.info("Key: " + key);

			log.info("> Evaluando valores del keyset");
			List<String> valores = entry.getValue();

			for (String valor : valores) {

				valor = valor.toLowerCase();
				log.info("Value : " + valor);

				requestValido = validarElementosGET(requestValido, valor);

			}

		}

		return requestValido;

	}

	/**
	 * @param requestValido
	 * @param valor
	 * @return
	 */
	private static boolean validarElementosGET(boolean requestValido, String valor) {

		/*
		 * Buscar simbolos
		 */
		if (LISTA_CARACTERES_PROHIBIDOS.isEmpty()) {
			log.error(ALERTA_BYPASS_CARACTERES);
		} else {

			// Validar si el request contiene palabras reservadas
			for (String word : LISTA_CARACTERES_PROHIBIDOS) {

				word = word.toLowerCase();

				if (valor.contains(word)) {
					requestValido = false;
					log.info(ALERTA_CARACTER_NO_PERMITIDO_ENCONTRADO + word);
					break;
				}
			}

		}

		/*
		 * Buscar palabras
		 */
		if (LISTA_PALABRAS_PROHIBIDAS.isEmpty()) {
			log.error(ALERTA_BYPASS_DICCIONARIO);
		} else {

			for (String word : LISTA_PALABRAS_PROHIBIDAS) {
				word = word.toLowerCase();

				if (contieneStringExacta(valor, word)) {
					requestValido = false;
					log.info(ALERTA_PALABRA_ENCONTRADA + word);
					break;
				}

			}
		}

		return requestValido;
	}

	/**
	 * ContieneStringExacta
	 * 
	 * @param source
	 * @param subItem
	 * @return
	 */
	private static boolean contieneStringExacta(String text, String word) {

		String REGEX_FIND_WORD = "(?i).*?\\b%s\\b.*?";
		String regex = String.format(REGEX_FIND_WORD, Pattern.quote(word));
		return text.matches(regex);

	}

	/**
	 * 
	 * Metodo encargado de formatear los decimales a dos numeros despues de la coma
	 * 
	 * @param numeroFormatea
	 * 
	 * @return
	 * 
	 */
	public static double formateaDecimal(double numeroFormatea) {
		return (double) Math.round(numeroFormatea * 100d) / 100d;
	}

	/**
	 * Limpia el rut de caracteres extras
	 * 
	 * @param rutLimpia
	 * @return
	 */
	public static String limpiaRut(String rutLimpia) {

		String sinPunto = rutLimpia.replace(".", "");
		return sinPunto.replace("-", "");

	}

	/**
	 * formateaNumerosenMiles
	 * 
	 * @param numeroFormatea
	 * @return
	 */
	public static String formateaNumerosenMiles(int numeroFormatea) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat myFormatter = (DecimalFormat) nf;
		return "$" + myFormatter.format(numeroFormatea);
	}

	/**
	 * formateaNumerosenMiles
	 * 
	 * @param numeroFormatea
	 * @return
	 */
	public static String formateaNumerosenMiles(Double numeroFormatea) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat myFormatter = (DecimalFormat) nf;
		return "$" + myFormatter.format(numeroFormatea);
	}

	/**
	 * Permite formatear los numeros en UF
	 * 
	 * @param input
	 * @return
	 */
	public static String formateaUF(Double input) {
		StringBuilder salida = new StringBuilder();
		salida.append("UF ");
		salida.append(formateaNumerosenMiles(input));

		return salida.toString();

	}

	/**
	 * formateaFecha
	 * 
	 * @param fechaFormatear
	 * @param formatoDeseado
	 * @return
	 */
	public static String formateaFecha(java.util.Date fechaFormatear, String formatoDeseado) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(fechaFormatear);
	}

	/**
	 * Permite limpiar los signos
	 * 
	 * @param input
	 * @return
	 */
	public static String limpiarSignos(String input) {

		String res = "";
		if (input != null && !"".equals(input)) {
			// Eliminar caracteres
			res = input.replaceAll("[^0-9]", "");

		} else {
			res = "0";
		}

		return res;
	}

	/**
	 * Deja la cadena solo con numeros
	 * 
	 * @param input
	 * @return
	 */
	public static String soloNumeros(String input) {

		String res = "";
		if (input != null && !"".equals(input)) {
			res = input.replaceAll("[^\\d]", "");
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Obtener la excepcion raiz, con esto no es necesario imprimir todo el objeto y solo se obtendra la causa especifica
	 * 
	 * Se llama asi: System.out.println(getRootException(e).getLocalizedMessage());
	 * 
	 * @param exception
	 * @return
	 */
	public static Throwable obtenerExcepcionRaiz(Throwable exception) {

		Throwable rootException = exception;
		while (rootException.getCause() != null) {
			rootException = rootException.getCause();
		}

		return rootException;

	}

	/**
	 * Permite limpiarle el / al final a las url
	 * 
	 * @param input
	 * @return
	 */
	public static String obtenerUrlLimpia(String input) {

		String res = input;
		log.info("URL analizada: " + input);

		try {
			String ultimoCaracter = input.substring(res.length() - 1, res.length());

			if (ultimoCaracter.equalsIgnoreCase("/")) {
				res = res.substring(0, res.length() - 1);
				log.info("La url ha sido limpiada, resultado: " + res);
			} else {
				log.info("Url OK");
			}

		} catch (Exception e) {

			log.error("ERROR > " + obtenerExcepcionRaiz(e));
		}

		return res;

	}

	/**
	 * Permite mediante un objeto cualquiera, devolverlo en cadena y formato XML
	 * 
	 * @param input
	 * @return
	 */
	public static String obtenerCadenaXML(Object input) {

		StringWriter sw = new StringWriter();
		JAXB.marshal(input, sw);
		String xmlString = sw.toString();

		return xmlString;

	}

	/**
	 * getFileFromResources
	 * 
	 * @param fileName
	 * @return
	 */
	public File getFileFromResources(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}

	}

	/**
	 * Encriptar en base 64
	 * 
	 * @param input
	 * @return
	 */
	public static String encriptarBase64(String nombreArchivo) {

		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = null;

		try {

			File input = new File(nombreArchivo);

			log.info("Ruta Archivo: " + input.getAbsolutePath());

			fileInputStreamReader = new FileInputStream(input);
			byte[] bytes = new byte[(int) input.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));

		} catch (Exception e) {
			log.error("Error en metodo: [encriptarBase64]", e);

		} finally {
			if (fileInputStreamReader != null)
				try {
					fileInputStreamReader.close();
				} catch (IOException e) {
					log.error("Error en metodo: [encriptarBase64]", e);
				}
		}

		return encodedBase64;

	}

}
