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
import Objetos.herramientas;

/**
 * @author Alvaro*/
public class herramientasDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();

    @Override
    public void Menu() {

        int op;

        do {
            System.out.println("\n--- HERRAMIENTAS ---");
            System.out.println("1. Mostrar");
            System.out.println("2. Crear");
            System.out.println("3. Borrar");
            System.out.println("4. Modificar");
            System.out.println("0. Salir");

            op = sc.nextInt();

            switch (op) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
                case 4 -> Modificar();
            }

        } while (op != 0);
    }

    @Override
    public boolean Mostrar() {
        ArrayList<Object> lista = Recibir();
        lista.forEach(System.out::println);
        return true;
    }

    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM herramientas";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                herramientas h = new herramientas(
                        rs.getInt("id_herramienta"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("codigo_interno"),
                        rs.getString("estado"),
                        rs.getString("ubicacion"),
                        rs.getBoolean("activa")
                );

                lista.add(h);
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

        System.out.print("Tipo: ");
        String tipo = sc.nextLine();

        System.out.print("Codigo: ");
        String codigo = sc.nextLine();

        System.out.print("Estado: ");
        String estado = sc.nextLine();

        System.out.print("Ubicacion: ");
        String ubi = sc.nextLine();

        System.out.print("Activa: ");
        boolean activa = sc.nextBoolean();

        String sql = "INSERT INTO herramientas VALUES (NULL,?,?,?,?,?,?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, codigo);
            ps.setString(4, estado);
            ps.setString(5, ubi);
            ps.setBoolean(6, activa);

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

        String sql = "DELETE FROM herramientas WHERE id_herramienta=?";

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

        System.out.print("Nuevo estado: ");
        String estado = sc.nextLine();

        String sql = "UPDATE herramientas SET estado=? WHERE id_herramienta=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
