package arc.services.api.publico;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/publico/evaluaciones")
public class CtrPublicoEvaluaciones {

    /**
     * <h2>cuestionario</h2>
     * explicacion del metodo
     *
     * encriptamos a base 64 para ocultar el url que geramos para dar a conocer
     * pasamos un id con el cuestionario a realizar dentro del los params
     * pasamos cadena => este contione el id del empleado y la informacion asi
     * como todo el cuestionario prueba que se almacena en el metodo registro
     *
     * @param ProductoPrecio
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/lstricsa", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<JSONObject> lstricsa(
            HttpServletRequest request
    ) {
        xook.sia.basedatos.conexion _DBconexion = new xook.sia.basedatos.conexion();
        try {
            _DBconexion.setConexion(arc.services.app.DBUtil.getDatasource("PRUEBA").getConnection());
            String Respuesta = _DBconexion.strSP("select json_cliente_listado()");
            JSONObject entity = new JSONObject();
            entity.put("numero", 1);
            entity.put("titulo", "Activiudad: Listados");
            entity.put("mensaje", "Listado correcto");
            entity.put("respuesta", (Respuesta == null ? "" : new JSONParser().parse(Respuesta)));
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (SQLException | ParseException ex) {
            JSONObject entity = new JSONObject();
            entity.put("numero", -1);
            entity.put("titulo", "MMinventario: Producto");
            entity.put("mensaje", "Problema: " + ex.toString());
            entity.put("respuesta", "");
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } finally {
            try {
                _DBconexion.Activar(false);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CtrPublicoEvaluaciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
