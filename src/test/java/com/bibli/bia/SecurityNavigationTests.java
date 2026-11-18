package com.bibli.bia;

import com.bibli.bia.config.SecurityConfig;
import com.bibli.bia.service.CustomUserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SecurityNavigationTests {
    private AnnotationConfigWebApplicationContext context;
    private MockMvc mvc;

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean CustomUserDetailsService users() { return mock(CustomUserDetailsService.class); }
    }

    @BeforeEach
    void setup() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(WebConfig.class, SecurityConfig.class);
        context.refresh();
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(context.getBean(FilterChainProxy.class)).build();
    }

    @AfterEach
    void close() { context.close(); }

    @Test
    void unauthenticatedNavigationUsesExistingLoginRoute() throws Exception {
        mvc.perform(get("/api/reservaLibro"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/api/login"));
    }

    @Test
    void readerCannotAccessAdministrativeScreensByDirectUrl() throws Exception {
        var session = new MockHttpSession();
        var authentication = new UsernamePasswordAuthenticationToken("reader", null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        session.setAttribute("SPRING_SECURITY_CONTEXT", new SecurityContextImpl(authentication));
        for (var route : List.of("dashboard", "usuariosAdmin", "addBook", "addLibroF",
                "reservaAdmin", "devolucion", "multasAdmin", "resenasAdmin", "modeloPredictivo")) {
            mvc.perform(get("/api/" + route).session(session)).andExpect(status().isForbidden());
        }
    }
}
