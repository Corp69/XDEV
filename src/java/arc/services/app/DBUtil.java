
package arc.services.app;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author ELIZANDRO
 */
public class DBUtil {

    private static DataSource datasource;

    /**
     * 
     * @param empresa obtenemos del archivo context del tomcat las bases de datos a conectarnos en este caso los clientes.
     * @return datasource retornamos la conexion al cliente
     */
    public static DataSource getDatasource(String empresa) {
        try {
            Context context = new InitialContext();
            Object lookup = context.lookup("java:/comp/env/jdbc/pg_" + empresa.toLowerCase());
            if (lookup != null) {
                datasource = (DataSource) lookup;
            }
        } catch (NamingException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datasource;
    }

}