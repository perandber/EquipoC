package Objetos;

import Comun.interfaces;

import java.util.ArrayList;
import java.util.Scanner;

import DAO.proveedores_maquinasDAO;

/**
 * Clase Objeto + Menú para la tabla "proveedores_maquinas".
 * Sigue la misma estructura que Objetos/maquinas.java.
 * 
 * @author Alexandru
 */
public class proveedores extends interfaces {

	// ── Propiedades ──────────────────────────────────────────
	private int    id_proveedor;
	private String nombre;
	private String contacto;
	private String telefono;
	private String email;
	private String direccion;

	private static Scanner sc = new Scanner(System.in);


	// Constructores
	public proveedores() {
		
	}
	
	public proveedores(int id_proveedor, String nombre, String contacto, String telefono, String email,
			String direccion) {
		super();
		this.id_proveedor = id_proveedor;
		this.nombre = nombre;
		this.contacto = contacto;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
	}

	// Getters y Setters
	
	public int getId_proveedor() {
		return id_proveedor;
	}

	public void setId_proveedor(int id_proveedor) {
		this.id_proveedor = id_proveedor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return String.format(
			"[%d] %s | Contacto: %s | Tel: %s | Email: %s | Dir: %s",
			id_proveedor,
			nombre != null ? nombre : "",
			contacto != null ? contacto : "-",
			telefono != null ? telefono : "-",
			email != null ? email : "-",
			direccion != null ? direccion : "-"
		);
	}



	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ PROVEEDORES ═══");
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

	public boolean Mostrar() {
		proveedores_maquinasDAO dao = new proveedores_maquinasDAO();
		ArrayList<proveedores> lista = dao.listarTodos();
		if (lista == null) { System.out.println("Error al consultar."); 
		return false; }

		if (lista.isEmpty()) {
			System.out.println("No hay proveedores registrados.");
			return false;
		} else {
			System.out.println("\n── Proveedores (" + lista.size() + ") ──");
			for (proveedores p : lista) System.out.println(p);
		}
		return true;
	}

	public boolean Crear() {
		System.out.println("\n── Nuevo proveedor ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); 
		return false; }

		System.out.print("Contacto: ");
		String contacto = sc.nextLine().trim();

		System.out.print("Teléfono: ");
		String telefono = sc.nextLine().trim();

		System.out.print("Email: ");
		String email = sc.nextLine().trim();

		System.out.print("Dirección: ");
		String direccion = sc.nextLine().trim();

		proveedores p = new proveedores();
		p.setNombre(nombre);
		p.setContacto(contacto);
		p.setTelefono(telefono);
		p.setEmail(email);
		p.setDireccion(direccion);

		proveedores_maquinasDAO dao = new proveedores_maquinasDAO();
		boolean ok = dao.insertar(p);
		System.out.println(ok ? "Proveedor creado." : "No se pudo crear.");
		return ok;
	}

	public boolean Modificar() {
		System.out.print("\nID del proveedor a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); 
		return false; }

		proveedores_maquinasDAO dao = new proveedores_maquinasDAO();
		proveedores p = dao.buscarPorId(id);
		if (p == null) { System.out.println("No existe ese proveedor."); 
		return false; }

		System.out.println("Actual: " + p);
		System.out.println("(Deja vacío para mantener el valor)");

		System.out.print("Nombre [" + p.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setNombre(v);

		System.out.print("Contacto [" + p.getContacto() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setContacto(v);

		System.out.print("Teléfono [" + p.getTelefono() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setTelefono(v);

		System.out.print("Email [" + p.getEmail() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setEmail(v);

		System.out.print("Dirección [" + p.getDireccion() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setDireccion(v);

		boolean ok = dao.actualizar(p);
		System.out.println(ok ? "Proveedor modificado." : "No se pudo modificar.");
		return ok;
	}

	public boolean Borrar() {
		System.out.print("\nID del proveedor a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return; }

		System.out.print("¿Confirmas? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) { System.out.println("Cancelado."); return; }

		proveedores_maquinasDAO dao = new proveedores_maquinasDAO();
		int resultado = dao.borrar(id);
		switch (resultado) {
			case 1:  System.out.println("Proveedor borrado."); break;
			case 0:  System.out.println("No se borró nada."); break;
			case -1: System.out.println("No se puede borrar: hay máquinas o piezas asociadas a este proveedor."); break;
		}
	}

	public void Buscar() {
		System.out.println("\n── Buscar proveedores (vacío = ignorar filtro) ──");

		System.out.print("Nombre contiene: ");
		String fNombre = sc.nextLine().trim();

		System.out.print("Contacto contiene: ");
		String fContacto = sc.nextLine().trim();

		System.out.print("Email contiene: ");
		String fEmail = sc.nextLine().trim();

		proveedores_maquinasDAO dao = new proveedores_maquinasDAO();
		ArrayList<proveedores> resultados = dao.buscar(fNombre, fContacto, fEmail);
		if (resultados == null) { System.out.println("Error al consultar."); return; }

		System.out.println("\n── Resultados (" + resultados.size() + ") ──");
		for (proveedores p : resultados) System.out.println(p);
	}

	@Override
	public ArrayList<Object> Recibir() {
		// TODO Auto-generated method stub
		return null;
	}
}