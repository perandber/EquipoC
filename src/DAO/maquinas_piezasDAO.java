package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * @author Cristian
 */
public class maquinas_piezasDAO extends interfaces {

	private conexion con = new conexion();
	private Scanner sc = new Scanner(System.in);

	@Override
	public void Menu() {
		boolean salir = false;
		while (!salir) {
			System.out.println("\n--- VINCULACIÓN MÁQUINAS Y PIEZAS ---");
			System.out.println("1. Mostrar todas las vinculaciones");
			System.out.println("2. Vincular pieza a máquina");
			System.out.println("3. Modificar vinculación");
			System.out.println("4. Eliminar vinculación");
			System.out.println("0. Volver");
			System.out.print("Seleccione una opción: ");

			try {
				int opcion = Integer.parseInt(sc.nextLine());
				switch (opcion) {
					case 1 -> Mostrar();
					case 2 -> Crear();
					case 3 -> Modificar();
					case 4 -> Borrar();
					case 0 -> salir = true;
					default -> System.out.println("Opción no válida.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: Introduce un número válido.");
			}
		}
	}

	@Override
	public boolean Mostrar() {
		String sql = "SELECT * FROM maquinas_piezas";
		try (Connection c = con.Conectar();
			 Statement st = c.createStatement();
			 ResultSet rs = st.executeQuery(sql)) {
			
			System.out.println("\nID Máquina | ID Pieza");
			System.out.println("-----------------------");
			while (rs.next()) {
				System.out.printf("    %d      |    %d\n", rs.getInt(1), rs.getInt(2));
			}
			return true;
		} catch (SQLException e) {
			System.out.println("Error al mostrar: " + e.getMessage());
			return false;
		}
	}

	@Override
	protected boolean Crear() {
		System.out.print("ID de la Máquina: ");
		int idM = Integer.parseInt(sc.nextLine());
		System.out.print("ID de la Pieza/Repuesto: ");
		int idP = Integer.parseInt(sc.nextLine());

		String sql = "INSERT INTO maquinas_piezas (id_maquina, id_pieza) VALUES (?, ?)";
		try (Connection c = con.Conectar();
			 PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, idM);
			ps.setInt(2, idP);
			ps.executeUpdate();
			System.out.println("Vinculación creada con éxito.");
			return true;
		} catch (SQLException e) {
			System.out.println("Error al vincular: " + e.getMessage());
			return false;
		}
	}

	@Override
	protected boolean Borrar() {
		System.out.print("ID de la Máquina de la vinculación a borrar: ");
		int idM = Integer.parseInt(sc.nextLine());
		System.out.print("ID de la Pieza de la vinculación a borrar: ");
		int idP = Integer.parseInt(sc.nextLine());

		String sql = "DELETE FROM maquinas_piezas WHERE id_maquina = ? AND id_pieza = ?";
		try (Connection c = con.Conectar();
			 PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, idM);
			ps.setInt(2, idP);
			int filas = ps.executeUpdate();
			if (filas > 0) {
				System.out.println("Vinculación eliminada.");
				return true;
			} else {
				System.out.println("No se encontró esa vinculación específica.");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error al borrar: " + e.getMessage());
			return false;
		}
	}

	@Override
	protected boolean Modificar() {
		System.out.println("Nota: Para modificar una relación primaria, se recomienda borrar y crear una nueva.");
		System.out.print("ID Máquina actual: ");
		int idMAntiguo = Integer.parseInt(sc.nextLine());
		System.out.print("ID Pieza actual: ");
		int idPAntiguo = Integer.parseInt(sc.nextLine());
		System.out.print("Nuevo ID Pieza: ");
		int idPNuevo = Integer.parseInt(sc.nextLine());

		String sql = "UPDATE maquinas_piezas SET id_pieza = ? WHERE id_maquina = ? AND id_pieza = ?";
		try (Connection c = con.Conectar();
			 PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, idPNuevo);
			ps.setInt(2, idMAntiguo);
			ps.setInt(3, idPAntiguo);
			int filas = ps.executeUpdate();
			if (filas > 0) System.out.println("Vinculación actualizada.");
			return filas > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		}
	}

	@Override
	public ArrayList<Object> Recibir() {
		return null;
	}
}