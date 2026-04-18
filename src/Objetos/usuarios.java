package Objetos;

import Comun.interfaces;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

import DAO.usuariosDAO;

/**
 * Clase Objeto + Menú para la tabla "usuarios".
 * 
 * NOTA sobre contraseñas:
 *   La aplicación web guarda contraseñas con BCrypt (PHP).
 *   Aquí en Java, si quieres compatibilidad, añade la librería jBCrypt
 *   y cambia la línea marcada con "MODO DEMO" en este archivo.
 * 
 * @author Alexandru
 */
public class usuarios extends interfaces {

	// ── Propiedades ──────────────────────────────────────────
	private int       id_usuario;
	private String    nombre;
	private String    apellidos;
	private String    email;
	private String    password_hash;   // NUNCA se muestra
	private String    rol;             // 'tecnico' o 'responsable'
	private boolean   activo;
	private Timestamp fecha_creacion;
	private Timestamp ultimo_acceso;

	private static Scanner sc = new Scanner(System.in);


	// ── Constructores ────────────────────────────────────────
	public usuarios() { }

	public usuarios(int id_usuario, String nombre, String apellidos, String email,
	                String password_hash, String rol, boolean activo,
	                Timestamp fecha_creacion, Timestamp ultimo_acceso) {
		this.id_usuario     = id_usuario;
		this.nombre         = nombre;
		this.apellidos      = apellidos;
		this.email          = email;
		this.password_hash  = password_hash;
		this.rol            = rol;
		this.activo         = activo;
		this.fecha_creacion = fecha_creacion;
		this.ultimo_acceso  = ultimo_acceso;
	}


	// ── Getters y Setters ────────────────────────────────────
	


	@Override
	public String toString() {
		return String.format(
			"[%d] %s %s | Email: %s | Rol: %s | Activo: %s | Último acceso: %s",
			id_usuario,
			nombre != null ? nombre : "",
			apellidos != null ? apellidos : "",
			email != null ? email : "",
			rol != null ? rol : "",
			activo ? "Sí" : "No",
			ultimo_acceso != null ? ultimo_acceso.toString() : "Nunca"
		);
	}


	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Timestamp fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Timestamp getUltimo_acceso() {
		return ultimo_acceso;
	}

	public void setUltimo_acceso(Timestamp ultimo_acceso) {
		this.ultimo_acceso = ultimo_acceso;
	}

	// ============================================================
	// MENU
	// ============================================================
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
				case 0: bucle = false;    break;
				case 1: Mostrar();        break;
				case 2: Crear();          break;
				case 3: Modificar();      break;
				case 4: Borrar();         break;
				case 5: Buscar();         break;
				default: System.out.println("Opción incorrecta");
			}
		}
	}


	// ── MOSTRAR ───────────────────────────────────────────────
	public void Mostrar() {
		usuariosDAO dao = new usuariosDAO();
		ArrayList<usuarios> lista = dao.listarTodos();
		if (lista == null) { System.out.println("Error al consultar."); return; }

		if (lista.isEmpty()) {
			System.out.println("No hay usuarios registrados.");
		} else {
			System.out.println("\n── Usuarios (" + lista.size() + ") ──");
			for (usuarios u : lista) System.out.println(u);
		}
	}


	// ── CREAR ─────────────────────────────────────────────────
	public void Crear() {
		System.out.println("\n── Nuevo usuario ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); return; }

		System.out.print("Apellidos: ");
		String apellidos = sc.nextLine().trim();

		System.out.print("Email: ");
		String email = sc.nextLine().trim();
		if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$")) {
			System.out.println("Email con formato inválido.");
			return;
		}

		System.out.print("Contraseña: ");
		String password = sc.nextLine();
		if (password.isEmpty()) { System.out.println("La contraseña es obligatoria."); return; }

		// MODO DEMO: guarda la contraseña tal cual (sin hash).
		// Para compatibilidad con la web: String hash = BCrypt.hashpw(password, BCrypt.gensalt());
		String hash = password;

		System.out.print("Rol (tecnico/responsable) [tecnico]: ");
		String rol = sc.nextLine().trim();
		if (rol.isEmpty()) rol = "tecnico";

		System.out.print("¿Activo? (s/n) [s]: ");
		String activoStr = sc.nextLine().trim();
		boolean activo = !activoStr.equalsIgnoreCase("n");

		usuarios u = new usuarios();
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
	}


	// ── MODIFICAR ─────────────────────────────────────────────
	public void Modificar() {
		System.out.print("\nID del usuario a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return; }

		usuariosDAO dao = new usuariosDAO();
		usuarios u = dao.buscarPorId(id);
		if (u == null) { System.out.println("No existe ese usuario."); return; }

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
	}


	// ── BORRAR ────────────────────────────────────────────────
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


	// ── BUSCAR ────────────────────────────────────────────────
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
		ArrayList<usuarios> resultados = dao.buscar(fNombre, fEmail, fRol, fActivo);
		if (resultados == null) { System.out.println("Error al consultar."); return; }

		System.out.println("\n── Resultados (" + resultados.size() + ") ──");
		for (usuarios u : resultados) System.out.println(u);
	}
}