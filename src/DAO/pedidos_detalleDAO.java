package DAO;

import java.sql.*;
import java.util.Scanner;
import Comun.conexion;
import java.util.ArrayList;
import Comun.interfaces;

/**
 * @author Cristian*/
public class pedidos_detalleDAO extends interfaces {
    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

	/**
    * Menú para consultar o añadir líneas de detalle a pedidos existentes.
    */
    @Override
    public void Menu() {
        System.out.println("\n--- DETALLES DE PEDIDO ---");
        System.out.println("1. Ver artículos de un pedido");
        System.out.println("2. Añadir línea de detalle");
        System.out.println("0. Volver");

        int op = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar()) {
            if (op == 1) {
                System.out.print("ID Pedido a consultar: ");
                int id = Integer.parseInt(sc.nextLine());
                PreparedStatement ps = c.prepareStatement("SELECT * FROM pedidos_detalle WHERE id_pedido = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) System.out.println("Producto: " + rs.getString("producto") + " | Cant: " + rs.getInt("cantidad"));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }
}
