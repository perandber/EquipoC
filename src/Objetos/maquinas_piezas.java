package Objetos;

import Comun.interfaces;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import DAO.maquinas_piezasDAO;

/**
 * Clase Objeto + Menú para la tabla de piezas de repuesto.
 * La tabla en la BD se llama "piezas_repuesto" (igual que en la web).
 * 
 * @author Cristian
 */
public class maquinas_piezas extends interfaces {

	// ── Propiedades ──────────────────────────────────────────
	private int        id_pieza;
	private String     codigo_proveedor;
	private String     codigo_interno;
	private String     nombre;
	private String     descripcion;
	private int        stock_actual;
	private int        stock_minimo;
	private int        stock_maximo;
	private BigDecimal precio_unitario;
	private String     ubicacion_almacen;
	private Integer    id_proveedor;   // Integer para aceptar NULL

	private static Scanner sc = new Scanner(System.in);


	// ── Constructores ────────────────────────────────────────
	public maquinas_piezas() {
		
	}
	
	public maquinas_piezas(int id_pieza, String codigo_proveedor, String codigo_interno, String nombre,
			String descripcion, int stock_actual, int stock_minimo, int stock_maximo, BigDecimal precio_unitario,
			String ubicacion_almacen, Integer id_proveedor) {
		super();
		this.id_pieza = id_pieza;
		this.codigo_proveedor = codigo_proveedor;
		this.codigo_interno = codigo_interno;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.stock_actual = stock_actual;
		this.stock_minimo = stock_minimo;
		this.stock_maximo = stock_maximo;
		this.precio_unitario = precio_unitario;
		this.ubicacion_almacen = ubicacion_almacen;
		this.id_proveedor = id_proveedor;
	}


	// ── Getters y Setters ────────────────────────────────────
	

	public int getId_pieza() {
		return id_pieza;
	}


	public void setId_pieza(int id_pieza) {
		this.id_pieza = id_pieza;
	}


	public String getCodigo_proveedor() {
		return codigo_proveedor;
	}


	public void setCodigo_proveedor(String codigo_proveedor) {
		this.codigo_proveedor = codigo_proveedor;
	}


	public String getCodigo_interno() {
		return codigo_interno;
	}


	public void setCodigo_interno(String codigo_interno) {
		this.codigo_interno = codigo_interno;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getStock_actual() {
		return stock_actual;
	}


	public void setStock_actual(int stock_actual) {
		this.stock_actual = stock_actual;
	}


	public int getStock_minimo() {
		return stock_minimo;
	}


	public void setStock_minimo(int stock_minimo) {
		this.stock_minimo = stock_minimo;
	}


	public int getStock_maximo() {
		return stock_maximo;
	}


	public void setStock_maximo(int stock_maximo) {
		this.stock_maximo = stock_maximo;
	}


	public BigDecimal getPrecio_unitario() {
		return precio_unitario;
	}


	public void setPrecio_unitario(BigDecimal precio_unitario) {
		this.precio_unitario = precio_unitario;
	}


	public String getUbicacion_almacen() {
		return ubicacion_almacen;
	}


	public void setUbicacion_almacen(String ubicacion_almacen) {
		this.ubicacion_almacen = ubicacion_almacen;
	}


	public Integer getId_proveedor() {
		return id_proveedor;
	}


	public void setId_proveedor(Integer id_proveedor) {
		this.id_proveedor = id_proveedor;
	}

	@Override
	public String toString() {
		return String.format(
			"[%d] %s | Cod.Int: %s | Stock: %d (mín %d / máx %d) | Precio: %s | Ubic: %s",
			id_pieza,
			nombre != null ? nombre : "",
			codigo_interno != null ? codigo_interno : "-",
			stock_actual,
			stock_minimo,
			stock_maximo,
			precio_unitario != null ? precio_unitario.toString() + " €" : "-",
			ubicacion_almacen != null ? ubicacion_almacen : "-"
		);
	}


	


	// ============================================================
	// MENU
	// ============================================================
	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ PIEZAS DE REPUESTO ═══");
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


	// ── MOSTRAR ───────────────────────────────────────────────
	public boolean Mostrar() {
		maquinas_piezasDAO dao = new maquinas_piezasDAO();
		ArrayList<maquinas_piezas> lista = dao.listarTodas();
		if (lista == null) { System.out.println("Error al consultar."); 
		return false; }

		if (lista.isEmpty()) {
			System.out.println("No hay piezas registradas.");
		} else {
			System.out.println("\n── Piezas (" + lista.size() + ") ──");
			for (maquinas_piezas p : lista) System.out.println(p);
			return true;
		}
		return false;
	}


	// ── CREAR ─────────────────────────────────────────────────
	public boolean Crear() {
		System.out.println("\n── Nueva pieza ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); 
		return false; }

		System.out.print("Código proveedor: ");
		String codProv = sc.nextLine().trim();

		System.out.print("Código interno: ");
		String codInt = sc.nextLine().trim();

		System.out.print("Descripción: ");
		String descripcion = sc.nextLine().trim();

		int stockActual = pedirEntero("Stock actual [0]: ", 0);
		int stockMinimo = pedirEntero("Stock mínimo [0]: ", 0);
		int stockMaximo = pedirEntero("Stock máximo [0]: ", 0);

		System.out.print("Precio unitario [0]: ");
		String precioStr = sc.nextLine().trim();
		BigDecimal precio;
		try {
			precio = precioStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(precioStr);
		} catch (NumberFormatException e) {
			System.out.println("Precio inválido, uso 0.");
			precio = BigDecimal.ZERO;
		}

		System.out.print("Ubicación almacén: ");
		String ubicacion = sc.nextLine().trim();

		System.out.print("ID proveedor (vacío = ninguno): ");
		String provStr = sc.nextLine().trim();
		Integer idProv = null;
		if (!provStr.isEmpty()) {
			try { idProv = Integer.parseInt(provStr); }
			catch (NumberFormatException e) { System.out.println("ID inválido, se deja vacío."); }
		}

		maquinas_piezas p = new maquinas_piezas();
		p.setNombre(nombre);
		p.setCodigo_proveedor(codProv);
		p.setCodigo_interno(codInt);
		p.setDescripcion(descripcion);
		p.setStock_actual(stockActual);
		p.setStock_minimo(stockMinimo);
		p.setStock_maximo(stockMaximo);
		p.setPrecio_unitario(precio);
		p.setUbicacion_almacen(ubicacion);
		p.setId_proveedor(idProv);

		maquinas_piezasDAO dao = new maquinas_piezasDAO();
		boolean ok = dao.insertar(p);
		System.out.println(ok ? "Pieza creada." : "No se pudo crear.");
		
		return ok;
	}


	// ── MODIFICAR ─────────────────────────────────────────────
	public boolean Modificar() {
		System.out.print("\nID de la pieza a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); 
		return false; }

		maquinas_piezasDAO dao = new maquinas_piezasDAO();
		maquinas_piezas p = dao.buscarPorId(id);
		if (p == null) { System.out.println("No existe esa pieza."); 
		return false; }

		System.out.println("Actual: " + p);
		System.out.println("(Deja vacío para mantener el valor)");

		System.out.print("Nombre [" + p.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setNombre(v);

		System.out.print("Descripción [" + p.getDescripcion() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setDescripcion(v);

		System.out.print("Stock actual [" + p.getStock_actual() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setStock_actual(Integer.parseInt(v)); }
			catch (NumberFormatException e) { System.out.println("Valor ignorado."); }
		}

		System.out.print("Stock mínimo [" + p.getStock_minimo() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setStock_minimo(Integer.parseInt(v)); }
			catch (NumberFormatException e) { System.out.println("Valor ignorado."); }
		}

		System.out.print("Precio [" + p.getPrecio_unitario() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setPrecio_unitario(new BigDecimal(v)); }
			catch (NumberFormatException e) { System.out.println("Valor ignorado."); }
		}

		System.out.print("Ubicación [" + p.getUbicacion_almacen() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setUbicacion_almacen(v);

		boolean ok = dao.actualizar(p);
		System.out.println(ok ? "Pieza modificada." : "No se pudo modificar.");
		return ok;
	}


	// ── BORRAR ────────────────────────────────────────────────
	public boolean Borrar() {
		System.out.print("\nID de la pieza a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return false; }

		System.out.print("¿Confirmas? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) { 
			System.out.println("Cancelado.");
		return true; }

		maquinas_piezasDAO dao = new maquinas_piezasDAO();
		int resultado = dao.borrar(id);
		switch (resultado) {
			case 1:  System.out.println("Pieza borrada."); break;
			case 0:  System.out.println("No se borró nada."); break;
			case -1: System.out.println("No se puede borrar: la pieza está usada en órdenes o pedidos."); break;
		}
		return false;
	}


	// ── BUSCAR ────────────────────────────────────────────────
	public void Buscar() {
		System.out.println("\n── Buscar piezas (vacío = ignorar filtro) ──");

		System.out.print("Nombre contiene: ");
		String fNombre = sc.nextLine().trim();

		System.out.print("Código interno contiene: ");
		String fCodInt = sc.nextLine().trim();

		System.out.print("Stock máximo mostrado (solo piezas con stock ≤ X): ");
		String fStockStr = sc.nextLine().trim();
		Integer fStock = null;
		if (!fStockStr.isEmpty()) {
			try { fStock = Integer.parseInt(fStockStr); }
			catch (NumberFormatException e) { System.out.println("Stock ignorado."); }
		}

		maquinas_piezasDAO dao = new maquinas_piezasDAO();
		ArrayList<maquinas_piezas> resultados = dao.buscar(fNombre, fCodInt, fStock);
		if (resultados == null) { System.out.println("Error al consultar."); return; }

		System.out.println("\n── Resultados (" + resultados.size() + ") ──");
		for (maquinas_piezas p : resultados) System.out.println(p);
	}


	// ── Auxiliar: pedir un entero con valor por defecto ───────
	private int pedirEntero(String prompt, int porDefecto) {
		System.out.print(prompt);
		String v = sc.nextLine().trim();
		if (v.isEmpty()) return porDefecto;
		try { return Integer.parseInt(v); }
		catch (NumberFormatException e) {
			System.out.println("No es un número válido, uso " + porDefecto);
			return porDefecto;
		}
	}

	@Override
	public ArrayList<Object> Recibir() {
		// TODO Auto-generated method stub
		return null;
	}
}