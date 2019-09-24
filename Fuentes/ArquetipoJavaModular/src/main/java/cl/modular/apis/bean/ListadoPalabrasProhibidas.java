package cl.modular.apis.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * ListadoPalabrasProhibidas
 * 
 * @author Camilo Contreras
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "listadoPalabrasProhibidas" })
public class ListadoPalabrasProhibidas {
	
	@JsonProperty("listadoCaracteresProhibidos")
	private List<String> listadoCaracteresProhibidos;

	@JsonProperty("listadoPalabrasProhibidas")
	private List<String> listadoPalabrasProhibidas;

	/** GET Y SET **/
	@JsonProperty("listadoPalabrasProhibidas")
	public List<String> getListadoPalabrasProhibidas() {
		return listadoPalabrasProhibidas;
	}

	@JsonProperty("listadoPalabrasProhibidas")
	public void setListadoPalabrasProhibidas(List<String> listadoPalabrasProhibidas) {
		this.listadoPalabrasProhibidas = listadoPalabrasProhibidas;
	}

	public List<String> getListadoCaracteresProhibidos() {
		return listadoCaracteresProhibidos;
	}

	public void setListadoCaracteresProhibidos(List<String> listadoCaracteresProhibidos) {
		this.listadoCaracteresProhibidos = listadoCaracteresProhibidos;
	}


}
