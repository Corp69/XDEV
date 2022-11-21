package arc.services.api.auth;

/**
 *
 * @author ELIZANDRO
 *
 * <h6>MDL USUARIO </h6>
 *
 * <b>Usuario: esta clase creamos un modelo de los datos que estaremos
 * utilizando para mostrar la información en localStorange desde angular o ionic
 * es un ejemplo de posibles datos.
 * </b>
 *
 *
 */
public class MdlUsuario {

    int Id = -1;
    String User = "";
    String Password = "";
    String Token = "";
    String Nombre = "";
    String imagen;
    String Database = "";

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
    
    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDatabase() {
        return Database;
    }

    public void setDatabase(String Database) {
        this.Database = Database;
    }
}
