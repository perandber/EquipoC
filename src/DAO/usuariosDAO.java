package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Objetos.usuario;

/**
 * DAO de la tabla "usuarios".
 * Solo operaciones de BD. El menú está en Objetos/usuarios.java.
 * 
 * @author Alexandru
 */
public class usuariosDAO {

	private static Scanner sc = new Scanner(System.in);

	// LISTAR TODOS 
	// NO seleccionamos password_hash por seguridad
	public ArrayList<usuario> listarTodos() {
		ArrayList<usuario> lista = new ArrayList<>();
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


	//  BUSCAR POR ID 
	public usuario buscarPorId(int id) {
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
	public ArrayList<usuario> buscar(String fNombre, String fEmail, String fRol, String fActivo) {
		ArrayList<usuario> lista = new ArrayList<>();
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
	public int insertar(usuario u) {
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
	public boolean actualizar(usuario u, String nuevaPass) {
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


	// BORRAR 
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


	// DESACTIVAR 
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


	// AUXILIAR
	private usuario filaAObjeto(ResultSet rs) throws SQLException {
		usuario u = new usuario();
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
	
	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ USUARIOS ═══");
			System.out.println("0. Volver");
			System.out.println("1. Mostrar todos");
			System.out.println("2. Crear");
			System.out.println("3. Modificar");
			System.out.println("4. Borrar");
			System.out.println("5. Buscar");
			System.out.print("Opción: ");

			try { opcion = Integer.parseInt(sc.nextLine()); }
			catch (NumberFormatException e) { opcion = -1; }

			switch (opcion) {
				case 0: bucle = false;
				break;
				case 1: Mostrar(); 
				break;
				case 2: Crear();
				break;
				case 3: Modificar();
				break;
				case 4: Borrar();
				break;
				case 5: Buscar(); 
				break;
				default: System.out.println("Opción incorrecta");
			}
		}
	}


	// MOSTRAR
	public void Mostrar() {
		usuariosDAO dao = new usuariosDAO();
		ArrayList<usuario> lista = dao.listarTodos();
		if (lista == null) { System.out.println("Error al consultar."); return; }

		if (lista.isEmpty()) {
			System.out.println("No hay usuarios registrados.");
		} else {
			System.out.println("\n── Usuarios (" + lista.size() + ") ──");
			for (usuario u : lista) System.out.println(u);
		}
	}


	// CREAR
	public boolean Crear() {
		System.out.println("\n── Nuevo usuario ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); 
		return false; }

		System.out.print("Apellidos: ");
		String apellidos = sc.nextLine().trim();

		System.out.print("Email: ");
		String email = sc.nextLine().trim();
		if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$")) {
			System.out.println("Email con formato inválido.");
			return false;
		}

		System.out.print("Contraseña: ");
		String password = sc.nextLine();
		if (password.isEmpty()) { System.out.println("La contraseña es obligatoria."); 
		return false; }

		// MODO DEMO: guarda la contraseña tal cual (sin hash).
		// Para compatibilidad con la web: String hash = BCrypt.hashpw(password, BCrypt.gensalt());
		String hash = password;

		System.out.print("Rol (tecnico/responsable) [tecnico]: ");
		String rol = sc.nextLine().trim();
		if (rol.isEmpty()) rol = "tecnico";

		System.out.print("¿Activo? (s/n) [s]: ");
		String activoStr = sc.nextLine().trim();
		boolean activo = !activoStr.equalsIgnoreCase("n");

		usuario u = new usuario();
		u.setNombre(nombre);
		u.setApellidos(apellidos);
		u.setEmail(email);
		u.setPassword_hash(hash);
		u.setRol(rol);
		u.setActivo(activo);

		usuariosDAO dao = new usuariosDAO();
		int resultado = dao.insertar(u);
		switch (resultado) {
			case 1:  System.out.println("Usuario creado."); break;
			case -1: System.out.println("Ese email ya está registrado."); break;
			default: System.out.println("No se pudo crear el usuario.");
		}
		return true;
	}


	// MODIFICAR
	public boolean Modificar() {
		System.out.print("\nID del usuario a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); 
		return false; }

		usuariosDAO dao = new usuariosDAO();
		usuario u = dao.buscarPorId(id);
		if (u == null) { System.out.println("No existe ese usuario."); 
		return false; }

		System.out.println("Actual: " + u);
		System.out.println("(Deja vacío para mantener el valor)");

		System.out.print("Nombre [" + u.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setNombre(v);

		System.out.print("Apellidos [" + u.getApellidos() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setApellidos(v);

		System.out.print("Email [" + u.getEmail() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setEmail(v);

		System.out.print("Rol [" + u.getRol() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setRol(v);

		System.out.print("¿Activo? (s/n) [" + (u.isActivo() ? "s" : "n") + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setActivo(v.equalsIgnoreCase("s"));

		System.out.print("Nueva contraseña (vacío = no cambiar): ");
		String nuevaPass = sc.nextLine();
		// MODO DEMO: guardamos la contraseña tal cual

		boolean ok = dao.actualizar(u, nuevaPass);
		System.out.println(ok ? "Usuario modificado." : "No se pudo modificar.");
		return ok;
	}


	// BORRAR 
	public void Borrar() {
		System.out.print("\nID del usuario a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return; }

		System.out.print("¿Confirmas borrar? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) { System.out.println("Cancelado."); return; }

		usuariosDAO dao = new usuariosDAO();
		int resultado = dao.borrar(id);
		switch (resultado) {
			case 1:
				System.out.println("Usuario borrado.");
				break;
			case 0:
				System.out.println("No se borró nada.");
				break;
			case -1:
				System.out.println("No se puede borrar: el usuario tiene actividad registrada.");
				System.out.print("¿Desactivar en su lugar? (s/n): ");
				if (sc.nextLine().trim().equalsIgnoreCase("s")) {
					boolean ok = dao.desactivar(id);
					System.out.println(ok ? "Usuario desactivado." : "Error al desactivar.");
				}
				break;
		}
	}


	// BUSCAR
	public void Buscar() {
		System.out.println("\n── Buscar usuarios (vacío = ignorar filtro) ──");

		System.out.print("Nombre contiene: ");
		String fNombre = sc.nextLine().trim();

		System.out.print("Email contiene: ");
		String fEmail = sc.nextLine().trim();

		System.out.print("Rol exacto (tecnico/responsable): ");
		String fRol = sc.nextLine().trim();

		System.out.print("¿Solo activos? (s/n, vacío = cualquiera): ");
		String fActivo = sc.nextLine().trim();

		usuariosDAO dao = new usuariosDAO();
		ArrayList<usuario> resultados = dao.buscar(fNombre, fEmail, fRol, fActivo);
		if (resultados == null) { System.out.println("Error al consultar."); return; }

		System.out.println("\n── Resultados (" + resultados.size() + ") ──");
		for (usuario u : resultados) System.out.println(u);
	}

	public ArrayList<Object> Recibir() {
		// TODO Auto-generated method stub
		return null;
	}
}