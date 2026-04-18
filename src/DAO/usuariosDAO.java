package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Comun.conexion;
import Objetos.usuarios;

/**
 * DAO de la tabla "usuarios".
 * Solo operaciones de BD. El menú está en Objetos/usuarios.java.
 * 
 * @author Alexandru
 */
public class usuariosDAO {


	// ── LISTAR TODOS ──────────────────────────────────────────
	// NO seleccionamos password_hash por seguridad
	public ArrayList<usuarios> listarTodos() {
		ArrayList<usuarios> lista = new ArrayList<>();
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT id_usuario, nombre, apellidos, email, rol, activo, fecha_creacion, ultimo_acceso " +
		             "FROM usuarios ORDER BY id_usuario";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar usuarios: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return lista;
	}


	// ── BUSCAR POR ID ────────────────────────────────────────
	public usuarios buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT id_usuario, nombre, apellidos, email, rol, activo, fecha_creacion, ultimo_acceso " +
		             "FROM usuarios WHERE id_usuario=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
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


	// ── BUSCAR con filtros ───────────────────────────────────
	public ArrayList<usuarios> buscar(String fNombre, String fEmail, String fRol, String fActivo) {
		ArrayList<usuarios> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
			"SELECT id_usuario, nombre, apellidos, email, rol, activo, fecha_creacion, ultimo_acceso " +
			"FROM usuarios WHERE 1=1");
		ArrayList<Object> params = new ArrayList<>();

		if (fNombre != null && !fNombre.isEmpty()) { sql.append(" AND nombre LIKE ?"); params.add("%" + fNombre + "%"); }
		if (fEmail  != null && !fEmail.isEmpty())  { sql.append(" AND email LIKE ?");  params.add("%" + fEmail + "%"); }
		if (fRol    != null && !fRol.isEmpty())    { sql.append(" AND rol = ?");       params.add(fRol); }
		if ("s".equalsIgnoreCase(fActivo))         { sql.append(" AND activo = 1"); }
		else if ("n".equalsIgnoreCase(fActivo))    { sql.append(" AND activo = 0"); }

		sql.append(" ORDER BY id_usuario");

		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
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


	// ── INSERTAR ─────────────────────────────────────────────
	// Devuelve: 1 OK, -1 email duplicado, 0 otro error
	public int insertar(usuarios u) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		String sql = "INSERT INTO usuarios (nombre, apellidos, email, password_hash, rol, activo) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getApellidos());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getPassword_hash());
			ps.setString(5, u.getRol());
			ps.setBoolean(6, u.isActivo());

			return ps.executeUpdate() > 0 ? 1 : 0;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) return -1;  // email duplicado
			System.out.println("Error al crear: " + e.getMessage());
			return 0;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// ── ACTUALIZAR ───────────────────────────────────────────
	// Si nuevaPass es null o vacío, NO cambia la contraseña
	public boolean actualizar(usuarios u, String nuevaPass) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql;
		if (nuevaPass != null && !nuevaPass.isEmpty()) {
			sql = "UPDATE usuarios SET nombre=?, apellidos=?, email=?, rol=?, activo=?, password_hash=? WHERE id_usuario=?";
		} else {
			sql = "UPDATE usuarios SET nombre=?, apellidos=?, email=?, rol=?, activo=? WHERE id_usuario=?";
		}

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getApellidos());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getRol());
			ps.setBoolean(5, u.isActivo());
			if (nuevaPass != null && !nuevaPass.isEmpty()) {
				ps.setString(6, nuevaPass);
				ps.setInt(7, u.getId_usuario());
			} else {
				ps.setInt(6, u.getId_usuario());
			}

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// ── BORRAR ───────────────────────────────────────────────
	// Devuelve: 1 OK, 0 no encontrado, -1 foreign key
	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id_usuario=?")) {
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


	// ── DESACTIVAR ───────────────────────────────────────────
	public boolean desactivar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		try (PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET activo=0 WHERE id_usuario=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al desactivar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// ── AUXILIAR ─────────────────────────────────────────────
	private usuarios filaAObjeto(ResultSet rs) throws SQLException {
		usuarios u = new usuarios();
		u.setId_usuario(rs.getInt("id_usuario"));
		u.setNombre(rs.getString("nombre"));
		u.setApellidos(rs.getString("apellidos"));
		u.setEmail(rs.getString("email"));
		u.setRol(rs.getString("rol"));
		u.setActivo(rs.getBoolean("activo"));
		u.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
		u.setUltimo_acceso(rs.getTimestamp("ultimo_acceso"));
		return u;
	}
}