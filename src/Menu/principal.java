package Menu;

import java.util.Scanner;

import Alexandru.*;
import Alvaro.*;
import Cristian.*;
import Perceval.*;
import Sergio.*;


/**
 * @author Perceval
 * Menu para elegir a que tabla acceder
 */
public class principal {

	private static boolean bucle = true ;//Si pasa a falso, se cierra la aplicacion
	static Scanner sc = new Scanner (System.in); //Crear escaner
	
	
	public static void main(String[] args) {
		maquinas maquina = new maquinas();
		piezas_repuesto repuesto = new piezas_repuesto();
		proveedores_maquinas proveedores = new proveedores_maquinas();
		usuarios usuario = new usuarios();
		archivos_adjuntos archivos = new archivos_adjuntos();
		ejecuciones_mantenimiento ejecuciones = new ejecuciones_mantenimiento();
		gamas_mantenimineto gamas = new gamas_mantenimineto();
		herramientas herramienta = new herramientas();
		maquinas_piezas piezas = new maquinas_piezas();
		ordenes_trabajo ordenes = new ordenes_trabajo();
		pedidos_compra compra = new pedidos_compra();
		pedidos_detalle detalles = new pedidos_detalle();
		tareas_ejecutadas ejecutadas = new tareas_ejecutadas();
		tareas_herramientas tareasHerramientas = new tareas_herramientas();
		tareas_mantenimineto mantenimiento = new tareas_mantenimineto();
		tareas_proyecto proyecto = new tareas_proyecto();
		analisis_maquinas analisis = new analisis_maquinas();
		horarios_trabajo horarios = new horarios_trabajo();
		incidencias incidencia = new incidencias();
		proyectos_gnatt gnatt = new proyectos_gnatt();
		
		int decision = 1000; //Opcion elejida por el usuario
		
		//Bucle, solo para si el usuario intenta cerrar la aplicacion
		while (bucle) {
			//Imprimir
			System.out.println("0. Salir\n"
					+ "1. Archivos adjuntos\n"
					+ "2. Ejecuciones mantenimiento\n"
					+ "3. Gamas mantenimiento\n"
					+ "4. Herramientas\n"
					+ "5. Horarios trabajo\n"
					+ "6. Incidencias\n"
					+ "7. Maquinas\n"
					+ "8. Maquinas piezas\n"
					+ "9. Ordenes trabajo\n"
					+ "10. Pedidos compra\n"
					+ "11. Pedido detalle\n"
					+ "12. Piezas repuesto\n"
					+ "13. Planes mantenimiento [No disponible]\n" //Esta al parecer no le toco a nadie
					+ "14. Proveedores maquinas\n"
					+ "15. Proyectos gantt\n"
					+ "16. Analisis maquinas\n"
					+ "17. Tareas ejecutadas\n"
					+ "18. Tareas herramientas\n"
					+ "19. Taeras mantenimiento\n"
					+ "20. Tareas proyecto\n"
					+ "21. Usuarios\n");
			//recibir decision del usuario
			try {
				/*Recogido a traves de "Integer.parseInt(sc.nextLine())"
				 *dado que, recogerlo a traves de nextInt trae un problema de repeticion infinita con try/catch.*/
				decision = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Dejar variable a un valor que lo marcara como incorrecto.
				decision = 1000;
			}
			
			
			//LLevar usuario a su decision
			switch (decision) {
			case 0:
				//Salir
				bucle = false;
				System.out.println("Saliendo del programa");
			case 1:
				archivos.Menu();
			break;
			case 2:
				ejecuciones.Menu();
			break;
			case 3:
				gamas.Menu();
			break;
			case 4:
				herramienta.Menu();
			break;
			case 5:
				horarios.Menu();
			break;
			case 6:
				incidencia.Menu();
			break;
			case 7:
				maquina.Menu();
			break;
			case 8:
				piezas.Menu();
			break;
			case 9:
				ordenes.Menu();
			break;
			case 10:
				compra.Menu();
			break;
			case 11:
				detalles.Menu();
			break;
			case 12:
				repuesto.Menu();
			break;
			case 13:
				System.out.println("No disponible");
			break;
			case 14:
				proveedores.Menu();
			break;
			case 15:
				gnatt.Menu();
			break;
			case 16:
				analisis.Menu();
			break;
			case 17:
				ejecutadas.Menu();
			break;
			case 18:
				tareasHerramientas.Menu();
			break;
			case 19:
				mantenimiento.Menu();
			break;
			case 20:
				proyecto.Menu();
			break;
			case 21:
				usuario.Menu();
			break;
			default:
				//Opcion fuera del rango
				System.out.println("Opcion incorrecta o no existente");
			break;
			}
		}
	}

}
