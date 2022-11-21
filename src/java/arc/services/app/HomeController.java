package arc.services.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ELIZANDRO
 */
@Controller
public class HomeController {

    /**
     *
     * @param model mostramos el JSP para declarar que es un webservices y
     * consumir servicios.
     * @return hacemos un redireccionamiento a login.
     */
    @RequestMapping("/login")
    public String home(Model model) {
        return "login/index";
    }
}
