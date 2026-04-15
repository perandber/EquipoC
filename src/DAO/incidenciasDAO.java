package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

public class incidenciasDAO extends interfaces {
    Scanner sc = new Scanner(System.in);

    @Override
    public void Menu() {
        int op;
        do {
            System.out.println("\n--- 6. INCIDENCIAS ---");
            System.out.println("1. Ver lista de fallos");
            System.out.println("2. Reportar nueva averia");
            System.out.println("3. Resolver incidencia (Update)");
            System.out.println("0. Volver");
            try { op = Integer.parseInt(sc.nextLine()); } catch (Exception e) { op = -1; }

            if(op == 1) Mostrar();
            else if(op == 2) Crear();
            else if(op == 3) Modificar();
        } while (op != 0);
    }

    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        // Mostramos el ID, el problema y en qué estado está
        String sql = "SELECT id_incidencia, descripcion, estado FROM incidencias";
        try (Connection c = conexion.Conectar(); ResultSet rs = c.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                lista.add("Ticket #" + rs.getInt(1) + " | " + rs.getString(2) + " | Estado: " + rs.getString(3));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    protected boolean Crear() {
        System.out.print("ID de la maquina averiada: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Describe el problema: ");
        String desc = sc.nextLine();

        // El estado inicial siempre es 'abierta'
        String sql = "INSERT INTO incidencias (id_maquina, descripcion, prioridad, estado) VALUES (?, ?, 'media', 'abierta')";
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idM);
            ps.setString(2, desc);
            ps.executeUpdate();
            System.out.println("Incidencia reportada correctamente.");
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override
    protected boolean Modificar() {
        System.out.print("ID de la incidencia a cerrar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Solucion aplicada: ");
        String sol = sc.nextLine();

        // Cambiamos el estado a 'resuelta' y grabamos la solución
        String sql = "UPDATE incidencias SET estado = 'resuelta', solucion_aplicada = ?, fecha_resolucion = NOW() WHERE id_incidencia = ?";
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sol);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Incidencia marcada como RESUELTA.");
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override public boolean Mostrar() { for(Object o : Recibir()) System.out.println(o); return true; }
    @Override protected boolean Borrar() { return false; }
}