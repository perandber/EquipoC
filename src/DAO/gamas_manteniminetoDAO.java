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
import Objetos.gamas_mantenimiento;

/**
 * @author Alvaro*/
public class gamas_manteniminetoDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();
    
    @Override
    public void Menu() {

        int op;

        do {
            System.out.println("\n--- GAMAS MANTENIMIENTO ---");
            System.out.println("1. Mostrar");
            System.out.println("2. Crear");
            System.out.println("3. Borrar");
            System.out.println("4. Modificar");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            op = sc.nextInt();

            switch (op) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
                case 4 -> Modificar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida");
            }

        } while (op != 0);
    }

    @Override
    public boolean Mostrar() {
        Recibir().forEach(System.out::println);
        return true;
    }

    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM gamas_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                gamas_mantenimiento g = new gamas_mantenimiento(
                        rs.getInt("id_gama"),
                        rs.getString("nombre"),
                        rs.getString("tipo_mantenimiento"),
                        rs.getString("tipo_gama"),
                        rs.getString("descripcion")
                );

                lista.add(g);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    protected boolean Crear() {

        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Tipo mantenimiento: ");
        String tipo = sc.nextLine();

        System.out.print("Tipo gama: ");
        String gama = sc.nextLine();

        String sql = "INSERT INTO gamas_mantenimiento VALUES (NULL,?,?,?,?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, gama);
            ps.setString(4, "");

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean Borrar() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM gamas_mantenimiento WHERE id_gama=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean Modificar() {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        String sql = "UPDATE gamas_mantenimiento SET nombre=? WHERE id_gama=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
