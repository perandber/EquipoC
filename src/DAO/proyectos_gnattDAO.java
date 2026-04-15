package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
/**
 * @author Sergio*/
public class proyectos_gnattDAO extends interfaces {
    Scanner sc = new Scanner(System.in);

    @Override
    public void Menu() {
        int op;
        do {
            System.out.println("\n--- 15. PROYECTOS GANTT ---");
            System.out.println("1. Listar proyectos activos");
            System.out.println("2. Crear nuevo proyecto");
            System.out.println("0. Volver");
            try { op = Integer.parseInt(sc.nextLine()); } catch (Exception e) { op = -1; }

            if(op == 1) Mostrar();
            else if(op == 2) Crear();
        } while (op != 0);
    }

    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        try (Connection c = conexion.Conectar(); 
             ResultSet rs = c.createStatement().executeQuery("SELECT id_proyecto, nombre, estado FROM proyectos_gantt")) {
            while (rs.next()) {
                lista.add(rs.getInt(1) + " - Project: " + rs.getString(2) + " [" + rs.getString(3) + "]");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    protected boolean Crear() {
        System.out.print("Nombre del nuevo proyecto: ");
        String nom = sc.nextLine();
        System.out.print("Fecha de inicio (YYYY-MM-DD): ");
        String inicio = sc.nextLine();

        String sql = "INSERT INTO proyectos_gantt (nombre, fecha_inicio, estado) VALUES (?, ?, 'planificado')";
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setDate(2, Date.valueOf(inicio)); // Convierte el texto en fecha para SQL
            ps.executeUpdate();
            System.out.println("Proyecto añadido al diagrama.");
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override public boolean Mostrar() { for(Object o : Recibir()) System.out.println(o); return true; }
    @Override protected boolean Borrar() { return false; }
    @Override protected boolean Modificar() { return false; }
}