package DAO;

import java.sql.*;
import java.util.ArrayList;
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

    @Override
    public boolean Mostrar() {
        try (Connection c = con.Conectar()) {
            listar(c);
            return true;
        } catch (SQLException e) {
            System.out.println("Error al mostrar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_trabajo";
        try (Connection c = con.Conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Aquí podrías añadir un objeto Orden si tuvieras la entidad definida
                lista.add("ID: " + rs.getInt("id") + " - " + rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al recibir datos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    protected boolean Crear() {
        try (Connection c = con.Conectar()) {
            crear(c);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    protected boolean Borrar() {
        System.out.print("ID de la orden a borrar: ");
        int id = Integer.parseInt(sc.nextLine());
        String sql = "DELETE FROM ordenes_trabajo WHERE id = ?";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    protected boolean Modificar() {
       System.out.print("ID de la orden a modificar: ");
       int id = Integer.parseInt(sc.nextLine());
       System.out.print("Nueva descripción: ");
       String desc = sc.nextLine();
       String sql = "UPDATE ordenes_trabajo SET descripcion = ? WHERE id = ?";
       try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
           ps.setString(1, desc);
           ps.setInt(2, id);
           return ps.executeUpdate() > 0;
       } catch (SQLException e) {
           return false;
       }
    }
}
