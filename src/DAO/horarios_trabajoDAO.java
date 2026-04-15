package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * @author Sergio*/
public class horarios_trabajoDAO extends interfaces {
    Scanner sc = new Scanner(System.in);

    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- 5. HORARIOS DE TRABAJO ---");
            System.out.println("1. Ver todos los horarios");
            System.out.println("2. Añadir nuevo turno");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");
            try { opcion = Integer.parseInt(sc.nextLine()); } catch (Exception e) { opcion = -1; }

            if(opcion == 1) Mostrar();
            else if(opcion == 2) Crear();
        } while (opcion != 0);
    }

    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT id_horario, nombre, hora_inicio, hora_fin FROM horarios_trabajo";
        
        try (Connection c = conexion.Conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(rs.getInt(1) + " - " + rs.getString(2) + " (" + rs.getTime(3) + " a " + rs.getTime(4) + ")");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    protected boolean Crear() {
        System.out.print("Nombre del horario (ej. Turno Mañana): ");
        String nom = sc.nextLine();
        System.out.print("Hora inicio (HH:MM:SS): ");
        String inicio = sc.nextLine();
        System.out.print("Hora fin (HH:MM:SS): ");
        String fin = sc.nextLine();

        // Ponemos 1 (true) de lunes a viernes
        String sql = "INSERT INTO horarios_trabajo (nombre, hora_inicio, hora_fin, lunes, martes, miercoles, jueves, viernes) VALUES (?, ?, ?, 1, 1, 1, 1, 1)";
        
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setTime(2, Time.valueOf(inicio));
            ps.setTime(3, Time.valueOf(fin));
            ps.executeUpdate();
            System.out.println("Horario creado.");
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override public boolean Mostrar() { for(Object o : Recibir()) System.out.println(o); return true; }
    @Override protected boolean Borrar() { return false; }
    @Override protected boolean Modificar() { return false; }
}