package arc.services.api.auth;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author ELIZANDRO
 */
@RestController
@RequestMapping("/api/auth")
public class CtrMenu {

    /**
     * Se devueve el listado de los menues a los que tiene permiso el empleado
     * con base en su Numero de Credencial
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/menu", method = RequestMethod.GET, produces = "application/json")
    public String menu(HttpServletRequest request) {
        String NumeroCredencial = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        xook.sia.basedatos.conexion _DBconexion = new xook.sia.basedatos.conexion();
        try {
            _DBconexion.setConexion(arc.services.app.DBUtil.getDatasource(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getConnection());
            String Respuesta = _DBconexion.strSP("select json_menu(" + NumeroCredencial + ", 0)");
            return Respuesta;
        } catch (SQLException ex) {
            Logger.getLogger(CtrMenu.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                _DBconexion.Activar(false);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CtrMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/menuatajo", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<JSONObject> menuAtajo(HttpServletRequest request) {
        String NumeroCredencial = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        xook.sia.basedatos.conexion _DBconexion = new xook.sia.basedatos.conexion();
        try {
            _DBconexion.setConexion(arc.services.app.DBUtil.getDatasource(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getConnection());
            String Respuesta = _DBconexion.strSP("select json_menu_atajos(" + NumeroCredencial + ")");
            JSONObject entity = new JSONObject();
            entity.put("numero", 1);
            entity.put("titulo", "Informacion de Menu");
            entity.put("mensaje", "Informacion Correcta");
            entity.put("respuesta", (Respuesta == null ? "" : new JSONParser().parse(Respuesta)));
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (SQLException | ParseException ex) {
            JSONObject entity = new JSONObject();
            entity.put("numero", -1);
            entity.put("titulo", "Listado de Estatus");
            entity.put("mensaje", "Informacion Correcta");
            entity.put("respuesta", "");
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } finally {
            try {
                _DBconexion.Activar(false);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CtrMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
