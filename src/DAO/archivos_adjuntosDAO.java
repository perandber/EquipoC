package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;

/**
 * @author Alvaro*/
public class archivos_adjuntosDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();

    @Override
    public void Menu() {

        int opcion;

        do {
            System.out.println("\n--- MENU ARCHIVOS ADJUNTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Insertar");
            System.out.println("3. Eliminar");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> listar();
                case 2 -> insertar();
                case 3 -> eliminar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida");
            }

        } while (opcion != 0);
    }

    // 🔹 LISTAR
    public void listar() {

        String sql = "SELECT * FROM archivos_adjuntos";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_archivo") + " - " +
                        rs.getString("nombre_archivo") + " - " +
                        rs.getString("tipo_referencia")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 INSERTAR
    public void insertar() {

        sc.nextLine(); // limpiar buffer

        System.out.print("Tipo referencia: ");
        String tipo = sc.nextLine();

        System.out.print("ID referencia: ");
        int idRef = sc.nextInt();
        sc.nextLine();

        System.out.print("Nombre archivo: ");
        String nombre = sc.nextLine();

        System.out.print("Ruta archivo: ");
        String ruta = sc.nextLine();

        System.out.print("ID usuario: ");
        int idUsuario = sc.nextInt();

        String sql = "INSERT INTO archivos_adjuntos (tipo_referencia, id_referencia, nombre_archivo, ruta_archivo, id_usuario_subida) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ps.setInt(2, idRef);
            ps.setString(3, nombre);
            ps.setString(4, ruta);
            ps.setInt(5, idUsuario);

            ps.executeUpdate();
            System.out.println("Insertado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 ELIMINAR
    public void eliminar() {

        System.out.print("ID a eliminar: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM archivos_adjuntos WHERE id_archivo = ?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Eliminado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
