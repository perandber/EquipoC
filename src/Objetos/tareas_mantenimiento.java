package Objetos;

/**@author Perceval
 * Objeto a construir 
 * Incluye el objeto "planes mantenimiento"*/
public class tareas_mantenimiento {

	private int id;
	private planes_mantenimiento plan;
	/**Descripcion de la tarea, maximo de 500 caracteres*/
	private String descripcion;
	private int orden;
	/**Instrucciones detalladas, caracteres ilimitados*/
	private String instrucciones;
	private boolean requiereEpi;
	/**Epis necesarios para la tarea, 500 caracteres*/
	private String episNecesarios;
	/**Reglas de seguridad, caracteres ilimitados*/
	private String reglasSeguridad;
	/**Reglas de medio ambiente, caracteres ilimitados*/
	private String medioAmbiente;
	
	
	//Constructores
	/**Constructor completo*/
	public tareas_mantenimiento(int id, planes_mantenimiento plan, String descripcion, int orden, String instrucciones,
			boolean requiereEpi, String episNecesarios, String reglasSeguridad, String medioAmbiente) {
		this.id = id;
		this.plan = plan;
		this.descripcion = descripcion;
		this.orden = orden;
		this.instrucciones = instrucciones;
		this.requiereEpi = requiereEpi;
		this.episNecesarios = episNecesarios;
		this.reglasSeguridad = reglasSeguridad;
		this.medioAmbiente = medioAmbiente;
	}
	
	/***/
}
