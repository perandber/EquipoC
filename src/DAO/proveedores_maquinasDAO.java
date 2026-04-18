package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Comun.conexion;
import Objetos.proveedores;

/**
 * DAO de la tabla "proveedores_maquinas".
 * Solo operaciones de BD. El menú está en Objetos/proveedores_maquinas.java.
 * 
 * @author Alexandru
 */
public class proveedores_maquinasDAO {


	// LISTAR TODOS
	public ArrayList<proveedores> listarTodos() {
		ArrayList<proveedores> lista = new ArrayList<>();
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT * FROM proveedores_maquinas ORDER BY id_proveedor";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar proveedores: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return lista;
	}


	// BUSCAR POR ID
	public proveedores buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM proveedores_maquinas WHERE id_proveedor=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return filaAObjeto(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error buscarPorId: " + e.getMessage());
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return null;
	}


	// BUSCAR con filtros
	public ArrayList<proveedores> buscar(String fNombre, String fContacto, String fEmail) {
		ArrayList<proveedores> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM proveedores_maquinas WHERE 1=1");
		ArrayList<String> params = new ArrayList<>();

		if (fNombre != null && !fNombre.isEmpty())     { sql.append(" AND nombre LIKE ?");   params.add("%" + fNombre + "%"); }
		if (fContacto != null && !fContacto.isEmpty()) { sql.append(" AND contacto LIKE ?"); params.add("%" + fContacto + "%"); }
		if (fEmail != null && !fEmail.isEmpty())       { sql.append(" AND email LIKE ?");    params.add("%" + fEmail + "%"); }

		sql.append(" ORDER BY id_proveedor");

		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++) ps.setString(i + 1, params.get(i));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) lista.add(filaAObjeto(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return lista;
	}


	// INSERTAR 
	public boolean insertar(proveedores p) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "INSERT INTO proveedores_maquinas (nombre, contacto, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getContacto());
			ps.setString(3, p.getTelefono());
			ps.setString(4, p.getEmail());
			ps.setString(5, p.getDireccion());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al crear proveedor: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// ACTUALIZAR
	public boolean actualizar(proveedores p) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "UPDATE proveedores_maquinas SET nombre=?, contacto=?, telefono=?, email=?, direccion=? WHERE id_proveedor=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getContacto());
			ps.setString(3, p.getTelefono());
			ps.setString(4, p.getEmail());
			ps.setString(5, p.getDireccion());
			ps.setInt(6, p.getId_proveedor());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// BORRAR 
	// Devuelve: 1 OK, 0 no encontrado, -1 foreign key
	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM proveedores_maquinas WHERE id_proveedor=?")) {
			ps.setInt(1, id);
			int filas = ps.executeUpdate();
			return filas > 0 ? 1 : 0;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1451) return -1;
			System.out.println("Error al borrar: " + e.getMessage());
			return 0;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// AUXILIAR 
	private proveedores filaAObjeto(ResultSet rs) throws SQLException {
		proveedores p = new proveedores();
		p.setId_proveedor(rs.getInt("id_proveedor"));
		p.setNombre(rs.getString("nombre"));
		p.setContacto(rs.getString("contacto"));
		p.setTelefono(rs.getString("telefono"));
		p.setEmail(rs.getString("email"));
		p.setDireccion(rs.getString("direccion"));
		return p;
	}
}