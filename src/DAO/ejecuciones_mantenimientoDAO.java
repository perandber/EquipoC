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

/**
 * @author Alvaro*/
public class ejecuciones_mantenimientoDAO extends interfaces{

	Scanner sc = new Scanner(System.in);
    conexion con = new conexion();

    @Override
    public void Menu() {

        int opcion;

        do {
            System.out.println("\n--- MENU EJECUCIONES ---");
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
        String sql = "SELECT * FROM ejecuciones_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String dato =
                        rs.getInt("id_ejecucion") + " - " +
                        rs.getInt("id_orden") + " - " +
                        rs.getDate("fecha_ejecucion") + " - " +
                        rs.getTime("hora_inicio") + " - " +
                        rs.getTime("hora_fin") + " - " +
                        rs.getInt("id_tecnico") + " - " +
                        rs.getString("observaciones") + " - " +
                        rs.getString("resultado");

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

        System.out.print("ID orden: ");
        int idOrden = sc.nextInt();
        sc.nextLine();

        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        System.out.print("Hora inicio (HH:MM:SS): ");
        String horaInicio = sc.nextLine();

        System.out.print("Hora fin (HH:MM:SS): ");
        String horaFin = sc.nextLine();

        System.out.print("ID tecnico: ");
        int idTecnico = sc.nextInt();
        sc.nextLine();

        System.out.print("Observaciones: ");
        String obs = sc.nextLine();

        System.out.print("Resultado: ");
        String resultado = sc.nextLine();

        String sql = "INSERT INTO ejecuciones_mantenimiento (id_orden, fecha_ejecucion, hora_inicio, hora_fin, id_tecnico, observaciones, resultado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setTime(3, Time.valueOf(horaInicio));
            ps.setTime(4, Time.valueOf(horaFin));
            ps.setInt(5, idTecnico);
            ps.setString(6, obs);
            ps.setString(7, resultado);

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

        String sql = "DELETE FROM ejecuciones_mantenimiento WHERE id_ejecucion = ?";

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

        System.out.print("ID ejecucion a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nueva observacion: ");
        String obs = sc.nextLine();

        System.out.print("Nuevo resultado: ");
        String resultado = sc.nextLine();

        String sql = "UPDATE ejecuciones_mantenimiento SET observaciones = ?, resultado = ? WHERE id_ejecucion = ?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, obs);
            ps.setString(2, resultado);
            ps.setInt(3, id);

            ps.executeUpdate();

            System.out.println("Modificado correctamente");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
