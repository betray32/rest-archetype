package cl.modular.apis.bean;

import java.io.Serializable;

/**
 * Permite definir que contendra y que tipo de dato contiene cada parametro para
 * llamar a los procedimientos
 * 
 * @author ccontrerasc
 *
 */
public class DefinicionParametro implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -6930573944517207652L;

	private String tipoCampo;

	private Object valorCampo;

	/**
	 * Constructor
	 * 
	 * @param tipoCampo
	 * @param valorCampo
	 */
	public DefinicionParametro(String tipoCampo, Object valorCampo) {
		super();
		this.tipoCampo = tipoCampo;
		this.valorCampo = valorCampo;
	}

	/** GET Y SET **/
	public String getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public Object getValorCampo() {
		return valorCampo;
	}

	public void setValorCampo(Object valorCampo) {
		this.valorCampo = valorCampo;
	}

}
