package cl.modular.apis.rest.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configuracion General de RestEasy, al dejar la URl unicamente con un '/' se
 * asume que directamente el nombre del servicio
 * 
 * @author Camilo Contreras
 *
 */
@ApplicationPath("/")
public class RestConfig extends Application {

}
