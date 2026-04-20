package DAO;

import java.sql.*;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * @author Cristian*/
public class ordenes_trabajoDAO extends interfaces {
    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

	/**
    * Interfaz de usuario por consola para gestionar las órdenes de trabajo.
    */

    @Override
    public void Menu() {
        System.out.println("\n--- ÓRDENES DE TRABAJO ---");
        System.out.println("1. Listar Órdenes");
        System.out.println("2. Nueva Orden");
        System.out.println("0. Volver");
        
        int op = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar()) {
            if (op == 1) listar(c);
            else if (op == 2) crear(c);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

	/**
    * Obtiene y despliega por consola el listado completo de órdenes registradas.
    * @param c Conexión proporcionada por el pool o clase conexion.
    * @throws SQLException Error al leer la tabla 'ordenes_trabajo'.
    */

    private void listar(Connection c) throws SQLException {
        String sql = "SELECT * FROM ordenes_trabajo";
        ResultSet rs = c.createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + " | Desc: " + rs.getString("descripcion"));
        }
    }

	/**
    * Inserta una nueva orden de trabajo solicitando la descripción al usuario.
    * @param c Conexión activa.
    * @throws SQLException Error en la inserción de datos.
    */
   
    private void crear(Connection c) throws SQLException {
        System.out.print("Descripción de la orden: ");
        String desc = sc.nextLine();
        String sql = "INSERT INTO ordenes_trabajo (descripcion) VALUES (?)";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, desc);
        ps.executeUpdate();
        System.out.println("Orden creada.");
    }
}
