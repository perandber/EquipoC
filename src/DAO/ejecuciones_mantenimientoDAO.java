package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.ejecuciones_mantenimiento;

/**
 * @author Alvaro*/
public class ejecuciones_mantenimientoDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();
    
    @Override
    public void Menu() {

        int op;

        do {
            System.out.println("\n--- EJECUCIONES MANTENIMIENTO ---");
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
        String sql = "SELECT * FROM ejecuciones_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                ejecuciones_mantenimiento e = new ejecuciones_mantenimiento(
                        rs.getInt("id_ejecucion"),
                        rs.getInt("id_orden"),
                        rs.getDate("fecha_ejecucion"),
                        rs.getTime("hora_inicio"),
                        rs.getTime("hora_fin"),
                        rs.getInt("id_tecnico"),
                        rs.getString("observaciones"),
                        rs.getString("resultado")
                );

                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    protected boolean Crear() {

        System.out.print("ID orden: ");
        int id = sc.nextInt();

        String sql = "INSERT INTO ejecuciones_mantenimiento VALUES (NULL,?,NOW(),NULL,NULL,1,'','')";

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
    protected boolean Borrar() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM ejecuciones_mantenimiento WHERE id_ejecucion=?";

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

        System.out.print("Resultado: ");
        String res = sc.nextLine();

        String sql = "UPDATE ejecuciones_mantenimiento SET resultado=? WHERE id_ejecucion=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, res);
            ps.setInt(2, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
