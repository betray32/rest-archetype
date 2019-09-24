/**
 * @author Camilo Contreras
 * 
 *         Filtros, estos son aplicados en cada solicitud que se ejecuta dentro
 *         del servicio, pricipalmente son dos
 * 
 *         CorsFilter: Permite definir filtros cors para este servicio mas
 *         informacion:
 *         https://developer.mozilla.org/es/docs/Web/HTTP/Access_control_CORS
 * 
 *         AutenticacionFilter: Permite validar si las llamadas a los servicios
 *         contienen como cabecera el token necesario para validar la ejecucion
 *         de este servicio
 */
package cl.modular.apis.filters;