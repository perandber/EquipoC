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
public class herramientasDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();

    @Override
    public void Menu() {

        int opcion;

        do {
            System.out.println("\n--- MENU HERRAMIENTAS ---");
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

    // LISTAR
    public void listar() {

        String sql = "SELECT * FROM herramientas";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_herramienta") + " - " +
                        rs.getString("nombre") + " - " +
                        rs.getString("estado")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERTAR
    public void insertar() {

        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Tipo: ");
        String tipo = sc.nextLine();

        System.out.print("Codigo interno: ");
        String codigo = sc.nextLine();

        System.out.print("Estado: ");
        String estado = sc.nextLine();

        System.out.print("Ubicacion: ");
        String ubicacion = sc.nextLine();

        System.out.print("Activa (true/false): ");
        boolean activa = sc.nextBoolean();

        String sql = "INSERT INTO herramientas (nombre, tipo, codigo_interno, estado, ubicacion, activa) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, codigo);
            ps.setString(4, estado);
            ps.setString(5, ubicacion);
            ps.setBoolean(6, activa);

            ps.executeUpdate();
            System.out.println("Insertado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar() {

        System.out.print("ID a eliminar: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM herramientas WHERE id_herramienta = ?";

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
