package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion; // Tu clase de conexión
import Comun.interfaces;
/**
 * @author Sergio*/
public class analisis_maquinasDAO extends interfaces {
    
    // El scanner para pedir datos por consola
    Scanner sc = new Scanner(System.in);

    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- 16. ANALISIS DE MAQUINAS ---");
            System.out.println("1. Mostrar todos los analisis");
            System.out.println("2. Registrar nuevo analisis (Insert)");
            System.out.println("3. Eliminar un analisis (Delete)");
            System.out.println("0. Volver al menu principal");
            System.out.print("Opcion: ");
            
            // Leemos la opción del usuario
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) { opcion = -1; }

            switch (opcion) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
            }
        } while (opcion != 0);
    }

    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        // Esta es la orden que le enviamos a la base de datos
        String sql = "SELECT * FROM analisis_maquinas";

        // Abrimos la conexión usando tu método estático conexion.Conectar()
        try (Connection c = conexion.Conectar(); 
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // rs.next() va saltando de fila en fila de la base de datos
            while (rs.next()) {
                // Creamos un texto con la información de la fila
                String info = "ID: " + rs.getInt("id_analisis") + 
                             " | Maquina: " + rs.getInt("id_maquina") + 
                             " | Var: " + rs.getString("nombre_variable") + 
                             " | Valor: " + rs.getDouble("valor") + " " + rs.getString("unidad_medida");
                lista.add(info); // Lo guardamos en la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al recibir datos de analisis");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean Mostrar() {
        // Llamamos a Recibir() para que nos de la lista de la BD
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay registros en la tabla.");
        } else {
            // Imprimimos cada línea que guardamos en la lista
            for (Object obj : datos) {
                System.out.println(obj);
            }
        }
        return true;
    }

    @Override
    protected boolean Crear() {
        // Pedimos los datos al usuario
        System.out.print("ID de la maquina: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre de la variable (ej. Presion): ");
        String var = sc.nextLine();
        System.out.print("Valor medido: ");
        double val = Double.parseDouble(sc.nextLine());
        System.out.print("Unidad de medida (ej. bar): ");
        String uni = sc.nextLine();

        // El SQL con '?' para que sea seguro. NOW() pone la fecha de hoy automáticamente.
        String sql = "INSERT INTO analisis_maquinas (id_maquina, nombre_variable, valor, unidad_medida, fecha_registro) VALUES (?, ?, ?, ?, NOW())";

        try (Connection c = conexion.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            // Rellenamos los '?' en orden
            ps.setInt(1, idM);
            ps.setString(2, var);
            ps.setDouble(3, val);
            ps.setString(4, uni);
            
            ps.executeUpdate(); // Ejecuta la inserción en la BD
            System.out.println("Analisis guardado correctamente.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean Borrar() {
        System.out.print("Introduce el ID del analisis a borrar: ");
        int id = Integer.parseInt(sc.nextLine());
        
        String sql = "DELETE FROM analisis_maquinas WHERE id_analisis = ?";

        try (Connection c = conexion.Conectar(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Registro eliminado.");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override protected boolean Modificar() {
    	return false; 
    	} 
}