package com.bibli.bia;

import com.bibli.bia.controller.PublicController;
import com.bibli.bia.controller.WebController;
import com.bibli.bia.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebUiRegressionTests {
    @Test
    void controllerViewsExistWithExactCaseForLinuxDeployments() throws Exception {
        var templates = Path.of("src/main/resources/templates");
        var names = Files.list(templates);
        try (names) {
            var available = names.map(path -> path.getFileName().toString()).toList();
            for (var controller : new String[]{"WebController", "PublicController"}) {
                var source = Files.readString(Path.of("src/main/java/com/bibli/bia/controller/" + controller + ".java"));
                var matches = Pattern.compile("return \"([\\w-]+)\";").matcher(source);
                while (matches.find()) {
                    assertTrue(available.contains(matches.group(1) + ".html"), "Missing template: " + matches.group(1));
                }
            }
        }
    }

    @Test
    void whitespaceUsernameIsRejectedBeforePersistence() {
        var controller = new WebController();
        var repository = mock(UsuarioRepository.class);
        ReflectionTestUtils.setField(controller, "usuarioRepository", repository);
        var attributes = new RedirectAttributesModelMap();
        assertEquals("redirect:/api/register", controller.procesarRegistro("   ", "abcdef", true, attributes));
        assertTrue(attributes.containsAttribute("error"));
        verifyNoInteractions(repository);
    }

    @Test
    void legacyLoginAndCartLinksResolveToWorkingScreens() {
        assertEquals("login", new PublicController().login());
        assertEquals("redirect:/api/reservaLibro", new WebController().carrito());
    }
}
