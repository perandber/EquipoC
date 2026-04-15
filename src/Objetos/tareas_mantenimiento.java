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
	
	
	//Constructor
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


	//Getters y setters
	private planes_mantenimiento getPlan() {
		return plan;
	}


	private void setPlan(planes_mantenimiento plan) {
		this.plan = plan;
	}


	private String getDescripcion() {
		return descripcion;
	}


	private void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	private int getOrden() {
		return orden;
	}


	private void setOrden(int orden) {
		this.orden = orden;
	}


	private String getInstrucciones() {
		return instrucciones;
	}


	private void setInstrucciones(String instrucciones) {
		this.instrucciones = instrucciones;
	}


	private boolean isRequiereEpi() {
		return requiereEpi;
	}


	private void setRequiereEpi(boolean requiereEpi) {
		this.requiereEpi = requiereEpi;
	}


	private String getEpisNecesarios() {
		return episNecesarios;
	}


	private void setEpisNecesarios(String episNecesarios) {
		this.episNecesarios = episNecesarios;
	}


	private String getReglasSeguridad() {
		return reglasSeguridad;
	}


	private void setReglasSeguridad(String reglasSeguridad) {
		this.reglasSeguridad = reglasSeguridad;
	}


	private String getMedioAmbiente() {
		return medioAmbiente;
	}


	private void setMedioAmbiente(String medioAmbiente) {
		this.medioAmbiente = medioAmbiente;
	}


	private int getId() {
		return id;
	}
	
	
}
