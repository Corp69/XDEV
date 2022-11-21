package arc.services.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author ELIZANDRO
 */
@Configuration
@EnableWebSecurity
public class SpringSeguridades extends WebSecurityConfigurerAdapter {

    /**
     * <h6>Nombre:Elizandro Fecha:03/Junio/2022</h6>
     *
     * Observaciones: esta clase SpringSeguridades tiene como finalidad ejecutar
     * algunos metodos sin tokken
     *
     *
     * @param http ejecutamos una instrucción de tipo http para ejecutar los
     * metodos
     *
     * @throws Exception Arrojamos los posibles errores de los metodos
     * normalmente son de tipo http 405 o 403 el cual debemos tener encenta si
     * son GET POST ETC.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login", "/resources/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/acceso").permitAll()
                //==========================================================
                // Metodos Publicos.
                //==========================================================
                .antMatchers(HttpMethod.GET, "/api/publico/evaluaciones/lstricsa").permitAll()
                //==========================================================
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home.html", true);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
