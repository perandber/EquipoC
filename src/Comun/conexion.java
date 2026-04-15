package Comun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** @author Perceval
 * Conexion a la base de datos externa */
public class conexion {

    //Base de datos
    /**[Tipo de base de datos] :// [direccion ip del servidor] : [puerto] / [nombre de base de datos]*/
    final private static String dir = "jdbc:mariadb://172.18.50.57:3306/intermodular";
    /**nombre de usario a usar*/
    final private static String user = "admin";
    /**contraseña de usario a usar*/
    final private static String pwd = "Perceval1";  
    
	public static Connection Conectar() {
		Connection conexion = null;
		
		//Iniciar la conexion
		try {
			//juntar datos de la conexion, usuario y contraseña
			conexion = (Connection) DriverManager.getConnection(dir, user, pwd);
		} catch (SQLException e) {
            System.out.println("Error al intentar iniciar conexion a la base de datos");			
		}
		return conexion;
	}
}
