package DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import Comun.conexion;
import Objetos.maquinas_piezas;

/**
 * @author Alexandru
 */
public class maquinas_piezasDAO {

	// ── Nombre de la tabla y clave primaria ──
	// Si tu BD usa otros nombres, cámbialos aquí.
	private static final String TABLA = "piezas_repuesto";
	private static final String PK    = "id_pieza";


	// LISTAR TODAS 
	public ArrayList<maquinas_piezas> listarTodas() {
		ArrayList<maquinas_piezas> lista = new ArrayList<>();
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT * FROM " + TABLA + " ORDER BY " + PK;
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar piezas: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return lista;
	}


	// BUSCAR POR ID 
	public maquinas_piezas buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM " + TABLA + " WHERE " + PK + "=?")) {
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
	public ArrayList<maquinas_piezas> buscar(String fNombre, String fCodInt, Integer fStockMax) {
		ArrayList<maquinas_piezas> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLA + " WHERE 1=1");
		ArrayList<Object> params = new ArrayList<>();

		if (fNombre != null && !fNombre.isEmpty()) { sql.append(" AND nombre LIKE ?");         params.add("%" + fNombre + "%"); }
		if (fCodInt != null && !fCodInt.isEmpty()) { sql.append(" AND codigo_interno LIKE ?"); params.add("%" + fCodInt + "%"); }
		if (fStockMax != null)                     { sql.append(" AND stock_actual <= ?");    params.add(fStockMax); }

		sql.append(" ORDER BY " + PK);

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


	// INSERTAR 
	public boolean insertar(maquinas_piezas p) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "INSERT INTO " + TABLA + " (codigo_proveedor, codigo_interno, nombre, descripcion, " +
		             "stock_actual, stock_minimo, stock_maximo, precio_unitario, ubicacion_almacen, id_proveedor) " +
		             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getCodigo_proveedor());
			ps.setString(2, p.getCodigo_interno());
			ps.setString(3, p.getNombre());
			ps.setString(4, p.getDescripcion());
			ps.setInt(5, p.getStock_actual());
			ps.setInt(6, p.getStock_minimo());
			ps.setInt(7, p.getStock_maximo());
			ps.setBigDecimal(8, p.getPrecio_unitario() != null ? p.getPrecio_unitario() : BigDecimal.ZERO);
			ps.setString(9, p.getUbicacion_almacen());
			if (p.getId_proveedor() != null) ps.setInt(10, p.getId_proveedor());
			else                             ps.setNull(10, Types.INTEGER);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al crear pieza: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	// ACTUALIZAR
	public boolean actualizar(maquinas_piezas p) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "UPDATE " + TABLA + " SET nombre=?, descripcion=?, stock_actual=?, " +
		             "stock_minimo=?, precio_unitario=?, ubicacion_almacen=? WHERE " + PK + "=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getDescripcion());
			ps.setInt(3, p.getStock_actual());
			ps.setInt(4, p.getStock_minimo());
			ps.setBigDecimal(5, p.getPrecio_unitario() != null ? p.getPrecio_unitario() : BigDecimal.ZERO);
			ps.setString(6, p.getUbicacion_almacen());
			ps.setInt(7, p.getId_pieza());

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

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM " + TABLA + " WHERE " + PK + "=?")) {
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
	private maquinas_piezas filaAObjeto(ResultSet rs) throws SQLException {
		maquinas_piezas p = new maquinas_piezas();
		p.setId_pieza(rs.getInt(PK));
		p.setCodigo_proveedor(rs.getString("codigo_proveedor"));
		p.setCodigo_interno(rs.getString("codigo_interno"));
		p.setNombre(rs.getString("nombre"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setStock_actual(rs.getInt("stock_actual"));
		p.setStock_minimo(rs.getInt("stock_minimo"));
		p.setStock_maximo(rs.getInt("stock_maximo"));
		p.setPrecio_unitario(rs.getBigDecimal("precio_unitario"));
		p.setUbicacion_almacen(rs.getString("ubicacion_almacen"));
		int idProv = rs.getInt("id_proveedor");
		p.setId_proveedor(rs.wasNull() ? null : idProv);
		return p;
	}
}