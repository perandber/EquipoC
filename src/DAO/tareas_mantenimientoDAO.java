package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Comun.conexion;
import Comun.interfaces;
import Objetos.planes_mantenimiento;
import Objetos.tareas_mantenimiento;

/**
 * @author Perceval*/
public class tareas_mantenimientoDAO extends interfaces{
	ArrayList<tareas_mantenimiento> tareas = new ArrayList<tareas_mantenimiento>(); 
	ArrayList<planes_mantenimiento> planes = new ArrayList<planes_mantenimiento>();

	@Override
	public void Menu() {

	}


	@Override
	public ArrayList Recibir() {
		ArrayList<tareas_mantenimiento> tareas = new ArrayList<tareas_mantenimiento>();
		Connection con = conexion.Conectar();
		Statement stat = null;
		ResultSet rs = null;
		
		//Recibir objetos de la clase agregada
		//planes = planes_manteniminetoDAO.Recibir();
		
		//Crear query
		try {
			stat = con.createStatement();
			
			//Query a ejecutar
			rs = stat.executeQuery ("select * from tareas_mantenimiento");
			
			//Bucle, una iteracion por cada fila recibida
			while (rs.next()) {
				int id_tarea = rs.getInt(1);
				int id_plan = rs.getInt(2);
				String descripcion = rs.getString(3);
				int orden = rs.getInt(4);
				String instruciones = rs.getString(5);
				boolean requiereEpi = rs.getBoolean(6);
				String epis = rs.getString(7);
				String seguridad = rs.getString(8);
				String medioAmbiente = rs.getString(9);
				
				planes_mantenimiento plan;
				for (planes_mantenimiento planTest : planes) {
					if (plan.getId = id_plan) {
						plan = planTest;
					}
				}
				
				tareas.add(new tareas_mantenimiento(id_tarea, plan, descripcion, orden, instruciones, requiereEpi, epis, seguridad, medioAmbiente));
			}
		} catch (SQLException e) {
			System.out.println("Error al intentar recibir informacion de la base de datos");
		} finally {
			
			try {
				if (rs != null) {
					rs.close();
				}
				if (stat != null ) {
					stat.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.out.println("Error al cerrar conexion a la base de datos");
			}
		}
		return tareas;
	}

	@Override
	protected boolean Crear() {
		return false;
	}

	@Override
	protected boolean Borrar() {
		return false;
	}



	@Override
	public boolean Mostrar() {
		return false;
	}



	@Override
	protected boolean Modificar() {
		return false;
	}


	
}
