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
public class gamas_manteniminetoDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();

    @Override
    public void Menu() {

        int opcion;

        do {
            System.out.println("\n--- MENU GAMAS MANTENIMIENTO ---");
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

        String sql = "SELECT * FROM gamas_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_gama") + " - " +
                        rs.getString("nombre") + " - " +
                        rs.getString("tipo_mantenimiento")
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

        System.out.print("Tipo mantenimiento (preventivo/correctivo/predictivo): ");
        String tipoMant = sc.nextLine();

        System.out.print("Tipo gama (neumatico/electrico/mecanico): ");
        String tipoGama = sc.nextLine();

        System.out.print("Descripcion: ");
        String descripcion = sc.nextLine();

        String sql = "INSERT INTO gamas_mantenimiento (nombre, tipo_mantenimiento, tipo_gama, descripcion) VALUES (?, ?, ?, ?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipoMant);
            ps.setString(3, tipoGama);
            ps.setString(4, descripcion);

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

        String sql = "DELETE FROM gamas_mantenimiento WHERE id_gama = ?";

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
