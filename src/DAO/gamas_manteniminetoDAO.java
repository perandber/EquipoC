package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
            System.out.println("1. Mostrar");
            System.out.println("2. Crear");
            System.out.println("3. Borrar");
            System.out.println("4. Modificar");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
                case 4 -> Modificar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida");
            }

        } while (opcion != 0);
    }

    // MOSTRAR
    @Override
    public boolean Mostrar() {

        ArrayList<Object> lista = Recibir();

        for (Object obj : lista) {
            System.out.println(obj);
        }

        return true;
    }

    // RECIBIR
    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM gamas_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String dato =
                        rs.getInt("id_gama") + " - " +
                        rs.getString("nombre") + " - " +
                        rs.getString("tipo_mantenimiento") + " - " +
                        rs.getString("tipo_gama") + " - " +
                        rs.getString("descripcion");

                lista.add(dato);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // CREAR
    @Override
    protected boolean Crear() {

        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Tipo mantenimiento: ");
        String tipoMant = sc.nextLine();

        System.out.print("Tipo gama: ");
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
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // BORRAR
    @Override
    protected boolean Borrar() {

        System.out.print("ID a eliminar: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM gamas_mantenimiento WHERE id_gama = ?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Eliminado correctamente");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // MODIFICAR
    @Override
    protected boolean Modificar() {

        System.out.print("ID a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Nuevo tipo mantenimiento: ");
        String tipoMant = sc.nextLine();

        System.out.print("Nueva descripcion: ");
        String descripcion = sc.nextLine();

        String sql = "UPDATE gamas_mantenimiento SET nombre = ?, tipo_mantenimiento = ?, descripcion = ? WHERE id_gama = ?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipoMant);
            ps.setString(3, descripcion);
            ps.setInt(4, id);

            ps.executeUpdate();

            System.out.println("Modificado correctamente");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
