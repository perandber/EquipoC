package Objetos;

import java.util.Date;
import java.sql.Time;

public class ejecuciones_mantenimiento {

    private int id_ejecucion;
    private int id_orden;
    private Date fecha_ejecucion;
    private Time hora_inicio;
    private Time hora_fin;
    private int id_tecnico;
    private String observaciones;
    private String resultado;

    public ejecuciones_mantenimiento() {}

    public ejecuciones_mantenimiento(int id_ejecucion, int id_orden, Date fecha_ejecucion,
                                     Time hora_inicio, Time hora_fin,
                                     int id_tecnico, String observaciones,
                                     String resultado) {
        this.id_ejecucion = id_ejecucion;
        this.id_orden = id_orden;
        this.fecha_ejecucion = fecha_ejecucion;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.id_tecnico = id_tecnico;
        this.observaciones = observaciones;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return id_ejecucion + " - orden: " + id_orden + " - " + resultado;
    }
}