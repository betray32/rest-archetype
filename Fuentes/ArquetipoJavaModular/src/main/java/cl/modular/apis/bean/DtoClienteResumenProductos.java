package cl.modular.apis.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resumen de productos, LISTA
 * 
 * @author ccontrerasc
 *
 */
public class DtoClienteResumenProductos {

	@JsonProperty("dtosProductosContratados")
	private List<DtoProductosContratados> dtosproductoscontratados;
	
	/**
	 * Reflection
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/** GET Y SET **/
	public void setDtosproductoscontratados(List<DtoProductosContratados> dtosproductoscontratados) {
		this.dtosproductoscontratados = dtosproductoscontratados;
	}

	public List<DtoProductosContratados> getDtosproductoscontratados() {
		return dtosproductoscontratados;
	}

}
