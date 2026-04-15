package Objetos;

public class archivos_adjuntos {

    private int id_archivo;
    private String nombre_archivo;
    private String tipo_referencia;
    private String ruta_archivo;
    private int id_usuario_subida;

    public archivos_adjuntos() {}

    public archivos_adjuntos(int id_archivo, String nombre_archivo,
                             String tipo_referencia, String ruta_archivo,
                             int id_usuario_subida) {
        this.id_archivo = id_archivo;
        this.nombre_archivo = nombre_archivo;
        this.tipo_referencia = tipo_referencia;
        this.ruta_archivo = ruta_archivo;
        this.id_usuario_subida = id_usuario_subida;
    }

    @Override
    public String toString() {
        return id_archivo + " - " + nombre_archivo + " - " + tipo_referencia;
    }
}