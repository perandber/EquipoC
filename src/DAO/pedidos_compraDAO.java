package DAO;

import java.sql.*;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * @author Cristian*/
public class pedidos_compraDAO extends interfaces {
    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

	/**
    * Menú principal para la gestión de cabeceras de pedidos.
    */
    @Override
    public void Menu() {
        System.out.println("\n--- PEDIDOS DE COMPRA ---");
        System.out.println("1. Ver historial de pedidos");
        System.out.println("2. Registrar cabecera de pedido");
        System.out.println("0. Volver");
        
        int op = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar()) {
            if (op == 1) {
                ResultSet rs = c.createStatement().executeQuery("SELECT * FROM pedidos_compra");
                while(rs.next()) System.out.println("ID Pedido: " + rs.getInt(1) + " | Fecha: " + rs.getDate(2));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }
}
