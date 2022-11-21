
package arc.services.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author ELIZANDRO
 */
public class SpringWebInicializador implements WebApplicationInitializer {

    /**
     *
     * @param contenedor
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext contenedor) throws ServletException {
        AnnotationConfigWebApplicationContext contexto = new AnnotationConfigWebApplicationContext();
        contexto.register(SpringConfiguracion.class);
        contenedor.addListener(new ContextLoaderListener(contexto));
        AnnotationConfigWebApplicationContext servletConfig = new AnnotationConfigWebApplicationContext();
        ServletRegistration.Dynamic servlet = contenedor.addServlet("dispatcher", new DispatcherServlet(servletConfig));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        contenedor.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain")).addMappingForUrlPatterns(null, false, "/*");
    }
}