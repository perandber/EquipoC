package Objetos;

import Comun.interfaces;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

import DAO.maquinasDAO;

/**
 * Clase Objeto + Menú para la tabla "maquinas".
 * 
 * ESTRUCTURA (importante para el equipo):
 *   - Propiedades privadas + getters/setters (representan una fila de la BD)
 *   - toString() → cómo se imprime el objeto por consola
 *   - Menu() → muestra las opciones al usuario
 *   - Métodos Mostrar/Crear/Modificar/Borrar/Buscar → piden datos por consola
 *     y delegan el acceso a BD en maquinasDAO (que está en el paquete DAO)
 * 
 * El DAO solo se encarga de hablar con la base de datos.
 * Esta clase se encarga de hablar con el usuario.
 * 
 * @author Alexandru
 */
public class maquinas extends interfaces {

	// ── Propiedades (columnas de la tabla) ──────────────────────
	private int       id_maquina;
	private String    nombre;
	private String    tipo;
	private String    numero_serie;
	private String    ubicacion;
	private String    fabricante;
	private Date      fecha_compra;
	private String    estado;        // 'disponible', 'en_mantenimiento', 'averia', etc.
	private String    qr_code;
	private Integer   proveedor_id;  // Integer para aceptar NULL
	private boolean   activa;
	private Timestamp fecha_registro;

	// ── Scanner compartido para leer entrada del usuario ────────
	private static Scanner sc = new Scanner(System.in);


	// ── Constructores ────────────────────────────────────────
	public maquinas() { }

	public maquinas(int id_maquina, String nombre, String tipo, String numero_serie,
	                String ubicacion, String fabricante, Date fecha_compra, String estado,
	                String qr_code, Integer proveedor_id, boolean activa, Timestamp fecha_registro) {
		this.id_maquina     = id_maquina;
		this.nombre         = nombre;
		this.tipo           = tipo;
		this.numero_serie   = numero_serie;
		this.ubicacion      = ubicacion;
		this.fabricante     = fabricante;
		this.fecha_compra   = fecha_compra;
		this.estado         = estado;
		this.qr_code        = qr_code;
		this.proveedor_id   = proveedor_id;
		this.activa         = activa;
		this.fecha_registro = fecha_registro;
	}


	// ── Getters y Setters ─────────────────────────────────────
	public int getId_maquina()            { return id_maquina; }
	public void setId_maquina(int v)      { this.id_maquina = v; }

	public String getNombre()             { return nombre; }
	public void setNombre(String v)       { this.nombre = v; }

	public String getTipo()               { return tipo; }
	public void setTipo(String v)         { this.tipo = v; }

	public String getNumero_serie()       { return numero_serie; }
	public void setNumero_serie(String v) { this.numero_serie = v; }

	public String getUbicacion()          { return ubicacion; }
	public void setUbicacion(String v)    { this.ubicacion = v; }

	public String getFabricante()         { return fabricante; }
	public void setFabricante(String v)   { this.fabricante = v; }

	public Date getFecha_compra()         { return fecha_compra; }
	public void setFecha_compra(Date v)   { this.fecha_compra = v; }

	public String getEstado()             { return estado; }
	public void setEstado(String v)       { this.estado = v; }

	public String getQr_code()            { return qr_code; }
	public void setQr_code(String v)      { this.qr_code = v; }

	public Integer getProveedor_id()      { return proveedor_id; }
	public void setProveedor_id(Integer v){ this.proveedor_id = v; }

	public boolean isActiva()             { return activa; }
	public void setActiva(boolean v)      { this.activa = v; }

	public Timestamp getFecha_registro()        { return fecha_registro; }
	public void setFecha_registro(Timestamp v)  { this.fecha_registro = v; }


	// ── toString: cómo se imprime el objeto en consola ───────
	@Override
	public String toString() {
		return String.format(
			"[%d] %s | Tipo: %s | Serie: %s | Ubic: %s | Fab: %s | Estado: %s | Activa: %s",
			id_maquina,
			nombre != null ? nombre : "",
			tipo != null ? tipo : "",
			numero_serie != null ? numero_serie : "-",
			ubicacion != null ? ubicacion : "-",
			fabricante != null ? fabricante : "-",
			estado != null ? estado : "-",
			activa ? "Sí" : "No"
		);
	}


	// ============================================================
	// MENU — muestra las opciones al usuario
	// ============================================================
	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ MÁQUINAS ═══");
			System.out.println("0. Volver");
			System.out.println("1. Mostrar todas");
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


	// ============================================================
	// MOSTRAR — pide al DAO la lista y la imprime
	// ============================================================
	public void Mostrar() {
		maquinasDAO dao = new maquinasDAO();
		ArrayList<maquinas> lista = dao.listarTodas();
		if (lista == null) { System.out.println("Error al consultar la base de datos."); return; }

		if (lista.isEmpty()) {
			System.out.println("No hay máquinas registradas.");
		} else {
			System.out.println("\n── Máquinas registradas (" + lista.size() + ") ──");
			for (maquinas m : lista) System.out.println(m);
		}
	}


	// ============================================================
	// CREAR — pide datos al usuario y pide al DAO que inserte
	// ============================================================
	public void Crear() {
		System.out.println("\n── Nueva máquina ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); return; }

		System.out.print("Tipo: ");
		String tipo = sc.nextLine().trim();

		System.out.print("Número de serie (vacío = ninguno): ");
		String numeroSerie = sc.nextLine().trim();
		if (numeroSerie.isEmpty()) numeroSerie = null;

		System.out.print("Ubicación: ");
		String ubicacion = sc.nextLine().trim();

		System.out.print("Fabricante: ");
		String fabricante = sc.nextLine().trim();

		System.out.print("Fecha compra (YYYY-MM-DD, vacío = hoy): ");
		String fechaCompra = sc.nextLine().trim();
		if (fechaCompra.isEmpty()) fechaCompra = null;

		System.out.print("Estado [disponible]: ");
		String estado = sc.nextLine().trim();
		if (estado.isEmpty()) estado = "disponible";

		System.out.print("QR code (vacío = ninguno): ");
		String qr = sc.nextLine().trim();
		if (qr.isEmpty()) qr = null;

		System.out.print("ID proveedor (vacío = sin proveedor): ");
		String provStr = sc.nextLine().trim();
		Integer proveedorId = null;
		if (!provStr.isEmpty()) {
			try { proveedorId = Integer.parseInt(provStr); }
			catch (NumberFormatException e) { System.out.println("ID inválido, se deja vacío."); }
		}

		System.out.print("¿Activa? (s/n) [s]: ");
		String activaStr = sc.nextLine().trim();
		boolean activa = !activaStr.equalsIgnoreCase("n");

		// Creamos el objeto y pedimos al DAO que lo inserte
		maquinas m = new maquinas();
		m.setNombre(nombre);
		m.setTipo(tipo);
		m.setNumero_serie(numeroSerie);
		m.setUbicacion(ubicacion);
		m.setFabricante(fabricante);
		m.setEstado(estado);
		m.setQr_code(qr);
		m.setProveedor_id(proveedorId);
		m.setActiva(activa);

		maquinasDAO dao = new maquinasDAO();
		boolean ok = dao.insertar(m, fechaCompra);
		System.out.println(ok ? "Máquina creada correctamente." : "No se pudo crear la máquina.");
	}


	// ============================================================
	// MODIFICAR — pide un id y permite cambiar los campos
	// ============================================================
	public void Modificar() {
		System.out.print("\nID de la máquina a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return; }

		maquinasDAO dao = new maquinasDAO();
		maquinas m = dao.buscarPorId(id);
		if (m == null) { System.out.println("No existe una máquina con id " + id); return; }

		System.out.println("Máquina actual: " + m);
		System.out.println("(Deja vacío un campo para mantener el valor actual)");

		System.out.print("Nombre [" + m.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setNombre(v);

		System.out.print("Tipo [" + m.getTipo() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setTipo(v);

		System.out.print("Número serie [" + m.getNumero_serie() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setNumero_serie(v);

		System.out.print("Ubicación [" + m.getUbicacion() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setUbicacion(v);

		System.out.print("Fabricante [" + m.getFabricante() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setFabricante(v);

		System.out.print("Estado [" + m.getEstado() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setEstado(v);

		System.out.print("¿Activa? (s/n) [" + (m.isActiva() ? "s" : "n") + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setActiva(v.equalsIgnoreCase("s"));

		boolean ok = dao.actualizar(m);
		System.out.println(ok ? "Máquina modificada correctamente." : "No se pudo modificar.");
	}


	// ============================================================
	// BORRAR — pide un id y borra (con opción de desactivar)
	// ============================================================
	public void Borrar() {
		System.out.print("\nID de la máquina a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return; }

		System.out.print("¿Confirmas borrar la máquina " + id + "? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
			System.out.println("Cancelado.");
			return;
		}

		maquinasDAO dao = new maquinasDAO();
		int resultado = dao.borrar(id);

		switch (resultado) {
			case 1:
				System.out.println("Máquina borrada correctamente.");
				break;
			case 0:
				System.out.println("No se borró ninguna fila (¿existe ese id?).");
				break;
			case -1:
				// Foreign key constraint
				System.out.println("No se puede borrar: hay análisis, órdenes o piezas asociadas a esta máquina.");
				System.out.print("¿Quieres DESACTIVARLA en su lugar? (s/n): ");
				if (sc.nextLine().trim().equalsIgnoreCase("s")) {
					boolean ok = dao.desactivar(id);
					System.out.println(ok ? "Máquina desactivada." : "Error al desactivar.");
				}
				break;
			default:
				System.out.println("Error al borrar.");
		}
	}


	// ============================================================
	// BUSCAR — pide filtros y muestra resultados
	// ============================================================
	public void Buscar() {
		System.out.println("\n── Buscar máquinas (vacío = ignorar filtro) ──");

		System.out.print("Nombre contiene: ");
		String fNombre = sc.nextLine().trim();

		System.out.print("Tipo contiene: ");
		String fTipo = sc.nextLine().trim();

		System.out.print("Ubicación contiene: ");
		String fUbicacion = sc.nextLine().trim();

		System.out.print("Estado exacto: ");
		String fEstado = sc.nextLine().trim();

		maquinasDAO dao = new maquinasDAO();
		ArrayList<maquinas> resultados = dao.buscar(fNombre, fTipo, fUbicacion, fEstado);
		if (resultados == null) { System.out.println("Error al consultar."); return; }

		System.out.println("\n── Resultados (" + resultados.size() + ") ──");
		for (maquinas m : resultados) System.out.println(m);
	}
}