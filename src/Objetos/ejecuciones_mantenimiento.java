package Objetos;

import java.util.Date;
import java.sql.Time;

/*
 * Clase que representa una ejecución de mantenimiento.
 * Cuando un técnico lleva a cabo una orden de mantenimiento, se registra
 * una ejecución con los datos de esa intervención: en qué fecha se hizo,
 * a qué hora empezó y terminó, quién fue el técnico responsable,
 * qué observaciones se anotaron y cuál fue el resultado final.
 *
 * Cada ejecución está asociada a una orden de mantenimiento concreta (id_orden).
 */
public class ejecuciones_mantenimiento {

    private int id_ejecucion;       // Identificador único de esta ejecución
    private int id_orden;           // Orden de mantenimiento a la que pertenece esta ejecución
    private Date fecha_ejecucion;   // Fecha en que se realizó el mantenimiento
    private Time hora_inicio;       // Hora a la que empezó la intervención
    private Time hora_fin;          // Hora a la que terminó la intervención
    private int id_tecnico;         // Técnico que realizó el trabajo
    private String observaciones;   // Notas o incidencias anotadas durante la ejecución
    private String resultado;       // Resultado final: completado, fallido, pendiente, etc.

    // Constructor vacío, necesario para crear objetos sin datos iniciales
    public ejecuciones_mantenimiento() {}

    // Constructor completo: crea una ejecución con todos sus datos de una vez
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

    // Representación en texto de la ejecución para mostrar por consola
    @Override
    public String toString() {
        return id_ejecucion + " - orden: " + id_orden + " - " + resultado;
    }
}