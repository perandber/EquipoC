package Objetos;

public class gamas_mantenimiento {

    private int id_gama;
    private String nombre;
    private String tipo_mantenimiento;
    private String tipo_gama;
    private String descripcion;

    public gamas_mantenimiento() {}

    public gamas_mantenimiento(int id_gama, String nombre,
                               String tipo_mantenimiento,
                               String tipo_gama,
                               String descripcion) {
        this.id_gama = id_gama;
        this.nombre = nombre;
        this.tipo_mantenimiento = tipo_mantenimiento;
        this.tipo_gama = tipo_gama;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return id_gama + " - " + nombre + " - " + tipo_mantenimiento;
    }
}