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
public class herramientasDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();

    @Override
    public void Menu() {

        int opcion;

        do {
            System.out.println("\n--- MENU HERRAMIENTAS ---");
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
        String sql = "SELECT * FROM herramientas";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String dato =
                        rs.getInt("id_herramienta") + " - " +
                        rs.getString("nombre") + " - " +
                        rs.getString("tipo") + " - " +
                        rs.getString("codigo_interno") + " - " +
                        rs.getString("estado") + " - " +
                        rs.getString("ubicacion") + " - " +
                        rs.getBoolean("activa");

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

        String sql = "DELETE FROM herramientas WHERE id_herramienta = ?";

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

    // MODIFICAR
    @Override
    protected boolean Modificar() {

        System.out.print("ID a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Nuevo estado: ");
        String estado = sc.nextLine();

        String sql = "UPDATE herramientas SET nombre = ?, estado = ? WHERE id_herramienta = ?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, estado);
            ps.setInt(3, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
