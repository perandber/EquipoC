package Objetos;

public class herramientas {

    private int id_herramienta;
    private String nombre;
    private String tipo;
    private String codigo_interno;
    private String estado;
    private String ubicacion;
    private boolean activa;

    public herramientas() {}

    public herramientas(int id_herramienta, String nombre, String tipo,
                        String codigo_interno, String estado,
                        String ubicacion, boolean activa) {
        this.id_herramienta = id_herramienta;
        this.nombre = nombre;
        this.tipo = tipo;
        this.codigo_interno = codigo_interno;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.activa = activa;
    }

    public int getId_herramienta() {
        return id_herramienta;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return id_herramienta + " - " + nombre + " - " + estado;
    }
}