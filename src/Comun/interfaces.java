package Comun;

/**
 * @author Perceval
*/
public abstract class interfaces {

	/**Menu para mostrar al usuario todas las opciones disponbibles*/
	public abstract void Menu();
	
	public abstract boolean Mostrar();
	
	public abstract boolean Recibir();
	
	protected abstract boolean Crear();
	
	protected abstract boolean Borrar();
	
	protected abstract boolean Modificar();
}
