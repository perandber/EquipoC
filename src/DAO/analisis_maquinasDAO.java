package DAO;

import Comun.interfaces;

/**
 * @author Sergio*/
public class analisis_maquinasDAO extends interfaces{

	@Override
	public void Menu() {
		int opcion;
        do {
            System.out.println("\n--- GESTIÓN ANÁLISIS DE MÁQUINAS ---");
            System.out.println("1. Mostrar todos los registros");
            System.out.println("2. Crear nuevo análisis");
            System.out.println("3. Borrar registro");
            System.out.println("4. Modificar valor de análisis");
            System.out.println("0. Volver");
            System.out.print("Selecciona: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
                case 4 -> Modificar();
            }
        } while (opcion != 0);
	}
	@Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        // Orden SQL: Traer todos los datos de la tabla analisis_maquinas
        String sql = "SELECT * FROM analisis_maquinas";

        // El bloque try-with-resources cierra la conexión automáticamente
        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Juntamos todas las columnas en un solo String para mostrarlo fácil
                String fila = rs.getInt("id_analisis") + " | Máquina ID: " + rs.getInt("id_maquina") + 
                             " | Variable: " + rs.getString("nombre_variable") + " | Valor: " + rs.getDouble("valor") +
                             " " + rs.getString("unidad_medida");
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean Mostrar() {
        ArrayList<Object> lista = Recibir();
        if(lista.isEmpty()) System.out.println("No hay datos.");
        for (Object obj : lista) System.out.println(obj);
        return true;
    }

    @Override
    protected boolean Crear() {
        // Pedimos los datos necesarios según las columnas del SQL
        System.out.print("ID Máquina: "); int idM = sc.nextInt();
        sc.nextLine(); // Limpiar buffer
        System.out.print("Nombre variable (ej. Presion): "); String var = sc.nextLine();
        System.out.print("Valor: "); double val = sc.nextDouble();
        sc.nextLine();
        System.out.print("Unidad (ej. bar, ºC): "); String uni = sc.nextLine();

        // El ? sirve para evitar errores y ataques en la base de datos
        String sql = "INSERT INTO analisis_maquinas (id_maquina, nombre_variable, valor, unidad_medida, fecha_registro) VALUES (?, ?, ?, ?, NOW())";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idM);
            ps.setString(2, var);
            ps.setDouble(3, val);
            ps.setString(4, uni);
            ps.executeUpdate();
            System.out.println("Análisis guardado con éxito.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean Borrar() {
        System.out.print("ID de análisis a eliminar: ");
        int id = sc.nextInt();
        String sql = "DELETE FROM analisis_maquinas WHERE id_analisis = ?";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override
    protected boolean Modificar() {
        System.out.print("ID de análisis a modificar: ");
        int id = sc.nextInt();
        System.out.print("Nuevo valor numérico: ");
        double nuevoVal = sc.nextDouble();

        String sql = "UPDATE analisis_maquinas SET valor = ? WHERE id_analisis = ?";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, nuevoVal);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Valor actualizado.");
            return true;
        } catch (SQLException e) { return false; }
    }

}
