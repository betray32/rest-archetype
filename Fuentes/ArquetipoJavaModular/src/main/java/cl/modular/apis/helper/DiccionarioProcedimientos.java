package cl.modular.apis.helper;

/**
 * Diccionario con los procedimientos a llamar en este servicio
 * 
 * @author ccontrerasc
 *
 */
public class DiccionarioProcedimientos {

	/**
	 * Permite armar un procedimiento de acuerdo a los parametros que este contiene
	 * 
	 * @return
	 */
	public static String armarProcedure(String nombreProcedure, int parametros) {

		StringBuilder res = new StringBuilder();

		res.append("{CALL ");
		res.append(nombreProcedure + "(");

		// Agregar parametros
		for (int x = 1; x <= parametros; x++) {

			if (x == parametros) {
				res.append("?");
			} else {
				res.append("?,");
			}
		}

		res.append(")}");

		return res.toString();
	}
	
	/**
	 * Permite armar un procedure con el numero de parametros pero empezando la
	 * cadena como Exec
	 * 
	 * @param nombreProcedure
	 * @param parametros
	 * @return
	 */
	public static String armarProcedureSqlServerExec(String nombreProcedure, int parametros) {
		StringBuilder res = new StringBuilder();

		res.append("EXEC ");
		res.append(nombreProcedure + " ");

		// Agregar parametros
		for (int x = 1; x <= parametros; x++) {

			if (x == parametros) {
				res.append("?");
			} else {
				res.append("?,");
			}
		}
		
		return res.toString();
	}
	
}
