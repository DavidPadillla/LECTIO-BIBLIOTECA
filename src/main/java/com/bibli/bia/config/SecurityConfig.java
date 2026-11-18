package com.bibli.bia.config;

import com.bibli.bia.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.ASYNC).permitAll()
                        .requestMatchers(
                                "/",
                                "/intro",
                                "/api/index",
                                "/api/register",
                                "/api/login",
                                "/estilos/**",
                                "/js/**",
                                "/img/**",
                                "/login",
                                "/api/librosVirtuales",
                                "/api/muestras/**",
                                "/api/terminos", "/api/privacidad"
                        ).permitAll()

                        .requestMatchers(
                                "/api/admin/**", "/api/dashboard", "/api/usuariosAdmin",
                                "/api/addBook", "/api/agregarLibro", "/api/addLibroF", "/api/agregarLibroF",
                                "/api/reservaAdmin", "/api/devolucion", "/api/procesar-devolucion",
                                "/api/multasAdmin", "/api/multas/pagar/**", "/api/resenasAdmin",
                                "/api/modeloPredictivo", "/api/noAdminConfig"
                        ).hasRole("ADMIN")
                        .requestMatchers("/api/logiado/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/api/login")
                        .loginProcessingUrl("/api/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/api/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessUrl("/api/index")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
        ;

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest saved = requestCache.getRequest(request, response);
            if (saved != null) {
                String path = java.net.URI.create(saved.getRedirectUrl()).getPath();
                String context = request.getContextPath();
                String localPath = path.startsWith(context) ? path.substring(context.length()) : path;
                if (localPath.matches("/api/lecturas/(hadas|grimm|poe|aventura|conocimiento|universo|civilizaciones)")) {
                    requestCache.removeRequest(request, response);
                    response.sendRedirect(context + localPath);
                    return;
                }
            }
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/api/admin");
            } else {
                response.sendRedirect("/api/logiado");
            }
        };
    }
}

