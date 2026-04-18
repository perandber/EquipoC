package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Comun.conexion;
import Objetos.maquina;

/**
 * @author Alexandru
 */
public class maquinasDAO {

	// LISTAR TODAS

	public ArrayList<maquina> listarTodas() {
		ArrayList<maquina> lista = new ArrayList<>();
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT * FROM maquinas ORDER BY id_maquina";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar máquinas: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return lista;
	}



	// BUSCAR POR ID

	public maquina buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM maquinas WHERE id_maquina=?")) {
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



	// BUSCAR con filtros dinámicos
	
	public ArrayList<maquina> buscar(String fNombre, String fTipo, String fUbicacion, String fEstado) {
		ArrayList<maquina> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM maquinas WHERE 1=1");
		ArrayList<String> params = new ArrayList<>();

		if (fNombre != null && !fNombre.isEmpty())       { sql.append(" AND nombre LIKE ?");    params.add("%" + fNombre + "%"); }
		if (fTipo != null && !fTipo.isEmpty())           { sql.append(" AND tipo LIKE ?");      params.add("%" + fTipo + "%"); }
		if (fUbicacion != null && !fUbicacion.isEmpty()) { sql.append(" AND ubicacion LIKE ?"); params.add("%" + fUbicacion + "%"); }
		if (fEstado != null && !fEstado.isEmpty())       { sql.append(" AND estado = ?");       params.add(fEstado); }

		sql.append(" ORDER BY id_maquina");

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
	// El parámetro fechaCompra se pasa aparte como String porque
	// el objeto usa java.sql.Date y puede venir null.

	public boolean insertar(maquina m, String fechaCompra) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "INSERT INTO maquinas (nombre, tipo, numero_serie, ubicacion, fabricante, " +
		             "fecha_compra, estado, qr_code, proveedor_id, activa) " +
		             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, m.getNombre());
			ps.setString(2, m.getTipo());
			ps.setString(3, m.getNumero_serie());
			ps.setString(4, m.getUbicacion());
			ps.setString(5, m.getFabricante());
			ps.setString(6, fechaCompra);     // null va bien con setString
			ps.setString(7, m.getEstado());
			ps.setString(8, m.getQr_code());
			if (m.getProveedor_id() != null) ps.setInt(9, m.getProveedor_id());
			else                             ps.setNull(9, java.sql.Types.INTEGER);
			ps.setBoolean(10, m.isActiva());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al insertar máquina: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}



	// ACTUALIZAR

	public boolean actualizar(maquina m) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "UPDATE maquinas SET nombre=?, tipo=?, numero_serie=?, ubicacion=?, " +
		             "fabricante=?, estado=?, activa=? WHERE id_maquina=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, m.getNombre());
			ps.setString(2, m.getTipo());
			ps.setString(3, m.getNumero_serie());
			ps.setString(4, m.getUbicacion());
			ps.setString(5, m.getFabricante());
			ps.setString(6, m.getEstado());
			ps.setBoolean(7, m.isActiva());
			ps.setInt(8, m.getId_maquina());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}



	// BORRAR
	// Devuelve: 1 si OK, 0 si no encontró nada, -1 si error de foreign key

	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM maquinas WHERE id_maquina=?")) {
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

	// DESACTIVAR (alternativa al borrado físico)

	public boolean desactivar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		try (PreparedStatement ps = con.prepareStatement("UPDATE maquinas SET activa=0 WHERE id_maquina=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al desactivar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}

	// AUXILIAR: convierte una fila del ResultSet en objeto maquinas

	private maquina filaAObjeto(ResultSet rs) throws SQLException {
		maquina m = new maquina();
		m.setId_maquina(rs.getInt("id_maquina"));
		m.setNombre(rs.getString("nombre"));
		m.setTipo(rs.getString("tipo"));
		m.setNumero_serie(rs.getString("numero_serie"));
		m.setUbicacion(rs.getString("ubicacion"));
		m.setFabricante(rs.getString("fabricante"));
		m.setFecha_compra(rs.getDate("fecha_compra"));
		m.setEstado(rs.getString("estado"));
		m.setQr_code(rs.getString("qr_code"));
		int prov = rs.getInt("proveedor_id");
		m.setProveedor_id(rs.wasNull() ? null : prov);
		m.setActiva(rs.getBoolean("activa"));
		m.setFecha_registro(rs.getTimestamp("fecha_registro"));
		return m;
	}
}