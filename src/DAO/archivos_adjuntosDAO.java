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
import Objetos.archivos_adjuntos;

/**
 * @author Alvaro*/
public class archivos_adjuntosDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();
    
    @Override
    public void Menu() {

        int op;

        do {
            System.out.println("\n--- ARCHIVOS ADJUNTOS ---");
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

        String sql = "SELECT * FROM archivos_adjuntos";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                archivos_adjuntos a = new archivos_adjuntos(
                        rs.getInt("id_archivo"),
                        rs.getString("nombre_archivo"),
                        rs.getString("tipo_referencia"),
                        rs.getString("ruta_archivo"),
                        rs.getInt("id_usuario_subida")
                );

                lista.add(a);
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

        System.out.print("Ruta: ");
        String ruta = sc.nextLine();

        String sql = "INSERT INTO archivos_adjuntos VALUES (NULL,?,?,?,1)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, "tarea");
            ps.setString(3, ruta);

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

        String sql = "DELETE FROM archivos_adjuntos WHERE id_archivo=?";

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

        String sql = "UPDATE archivos_adjuntos SET nombre_archivo=? WHERE id_archivo=?";

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
