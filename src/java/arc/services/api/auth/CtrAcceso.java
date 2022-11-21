package arc.services.api.auth;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class CtrAcceso {
    //**************************************************************************
    // DECLARAMOS LAS VARIABLES FINALES.
    //**************************************************************************
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer";
    private final String SECRET = "ArcariusKey";

    //**************************************************************************
    // AGREGAR METODOS DE LOGIN SI SON NESESARIOS EN ESTE APARTADO.
    //**************************************************************************
    // @method login() => insertar metodo de logiin y ejecutar el jsonwebtoken de ser nesesario 
    
        @ResponseBody
    @RequestMapping(value = "/acceso", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<org.jose4j.json.internal.json_simple.JSONObject> acceso(@RequestBody MdlUsuario usuario, HttpServletResponse response) {
        xook.sia.basedatos.conexion _DBconexion = new xook.sia.basedatos.conexion();
        try {
            _DBconexion.setConexion(arc.services.app.DBUtil.getDatasource(usuario.Database).getConnection());
            java.sql.ResultSet rs = _DBconexion.exeQuery("select rh_empleado.id,"
                    + " rh_empleado.nombre, rh_empleado.paterno, rh_empleado.materno,"
                    + " rh_puesto.descripcion puesto, rh_departamento.descripcion departamento,"
                    + " conf_empresa.nombre || case when conf_empresa.prueba then ' [Test]' else ' [Production]' end as empresa,"
                    + " case when _sp_soy_admin(rh_empleado.id) then 1 else 0 end perfil, rh_empleado.correo,"
                    + " coalesce(rh_empleado.id_rpt_tablero_detalle, -1) id_rpt_tablero_detalle"
                    + " from rh_empleado"
                    + " inner join conf_empresa on conf_empresa.id = rh_empleado.id_conf_empresa"
                    + " inner join rh_puesto on rh_puesto.id = rh_empleado.id_rh_puesto"
                    + " inner join rh_departamento on rh_departamento.id = rh_empleado.id_rh_departamento"
                    + " where rh_empleado.usuario = '" + usuario.getUser() + "'"
                    + " and rh_empleado.password = '" + usuario.getPassword() + "'"
            );
            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                getJWTToken(usuario);
                Gson gson = new Gson();
                org.jose4j.json.internal.json_simple.parser.JSONParser parser = new org.jose4j.json.internal.json_simple.parser.JSONParser();
                org.jose4j.json.internal.json_simple.JSONObject entity = (org.jose4j.json.internal.json_simple.JSONObject) parser.parse(gson.toJson(usuario));
                return new ResponseEntity<>(entity, HttpStatus.OK);
            } else {
                org.jose4j.json.internal.json_simple.JSONObject entity = new org.jose4j.json.internal.json_simple.JSONObject();
                entity.put("numero", -1);
                entity.put("titulo", "Autentificacion Arcarius");
                entity.put("mensaje", "Tus credenciales no son correctas");
                return new ResponseEntity<>(entity, HttpStatus.OK);
            }
        } catch (SQLException ex) {
            org.jose4j.json.internal.json_simple.JSONObject entity = new org.jose4j.json.internal.json_simple.JSONObject();
            entity.put("numero", -1);
            entity.put("titulo", "Problemas al guardar actividad");
            entity.put("mensaje", "Problema: " + ex.toString());
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (org.jose4j.json.internal.json_simple.parser.ParseException ex) {
            Logger.getLogger(CtrAcceso.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                _DBconexion.Activar(false);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CtrAcceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    
    //**************************************************************************
    // pasamos: un modelo para generar un JsonWebToken pasmos un modelo
    // en base al modelo agregamos las columnas que nesesitamos, el usuario a logerarse.
    //**************************************************************************
    private void getJWTToken(MdlUsuario usuario) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setId(String.valueOf(usuario.getId()))
                .setSubject(usuario.getDatabase())
                .setHeaderParam("Numero", usuario.getId())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .claim("NumeroUsuario", usuario.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
        usuario.setToken("Bearer " + token);
    }

}
