package com.bibli.bia.controller;

import com.bibli.bia.Model.*;
import com.bibli.bia.repository.LibroRepository;
import com.bibli.bia.repository.ReservaRepository;
import com.bibli.bia.repository.RespuestaDashboardRepository;
import com.bibli.bia.repository.UsuarioRepository;
import com.bibli.bia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class WebController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private RespuestaDashboardRepository respuestaRepository;

    @Autowired
    private LibroFisicoService libroFisicoService;

    @Autowired
    private RespuestaDashboardService respuestaService;

    @Autowired
    private UsuarioRepository usuariorepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private MultaService multaService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private EmailService emailService;

    // ==================== RUTAS BÁSICAS ====================

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/intro";
    }

    @GetMapping("/intro")
    public String introPage() {
        return "intro";
    }

    @GetMapping("/index")
    public String homePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isUser = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

            if (isAdmin) {
                return "redirect:/api/admin";
            } else if (isUser) {
                return "redirect:/api/logiado";
            }
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Usuario o contraseña incorrectos");
        if (logout != null) model.addAttribute("message", "Sesión cerrada correctamente");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/librosVirtuales")
    public String librosVirtualess() {
        return "librosVirtuales";
    }

    @PostMapping("/register")
    public String procesarRegistro(@RequestParam String username,
                                   @RequestParam String password,
                                   RedirectAttributes redirectAttributes) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addAttribute("error", "El nombre de usuario ya está en uso");
            return "redirect:/api/register";
        }
        if (password.length() < 6) {
            redirectAttributes.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "redirect:/api/register";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRoles(Set.of("USER"));
        usuarioRepository.save(nuevoUsuario);

        redirectAttributes.addAttribute("success", true);
        return "redirect:/api/login";
    }

    // ==================== ÁREA USUARIO ====================

    @GetMapping("/logiado")
    public String logiado(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "logiado";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }

    @GetMapping("/libroVirtualesLog")
    public String listarLibros(Model model) {
        List<LibroModel> libros = libroService.obtenerTodosLosLibros();
        System.out.println("📚 TOTAL LIBROS EN BD: " + libros.size());

        model.addAttribute("libros", libros);
        model.addAttribute("librosNovela", libroService.obtenerLibrosPorCategoria("Novelas"));
        model.addAttribute("librosCiencia", libroService.obtenerLibrosPorCategoria("Ciencia"));
        model.addAttribute("librosHistoria", libroService.obtenerLibrosPorCategoria("Historia"));
        model.addAttribute("librosArte", libroService.obtenerLibrosPorCategoria("Arte"));
        model.addAttribute("librosFantasia", libroService.obtenerLibrosPorCategoria("Fantasía"));
        model.addAttribute("librosFilosofia", libroService.obtenerLibrosPorCategoria("Filosofía"));
        return "libroVirtualesLog";
    }

    @GetMapping("/reservaLibro")
    public String reservaLibroUsuario(@RequestParam(value = "categoria", required = false) String categoria, Model model) {
        model.addAttribute("categorias", List.of("Novelas", "Ciencia", "Historia", "Arte", "Fantasía", "Filosofía"));
        if (categoria != null) {
            model.addAttribute("librosPorCategoria", libroFisicoService.obtenerLibrosFisicosPorCategoria(categoria));
            model.addAttribute("categoriaSeleccionada", categoria);
        }
        return "reservaLibro";
    }

    @PostMapping("/guardarResena")
    public String guardarResena(@RequestParam String comentario,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para dejar una reseña");
                return "redirect:/api/login";
            }

            Usuario usuario = usuarioRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            ResenaModel resena = new ResenaModel(usuario, usuario.getUsername(), comentario);
            resenaService.guardarResena(resena);
            redirectAttributes.addFlashAttribute("mensaje", "¡Gracias por tu reseña!");
            return "redirect:/api/logiado";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar la reseña");
            return "redirect:/api/logiado";
        }
    }

    @PostMapping("/guardarRespuestaDashboard")
    public String guardarRespuestaDashboard(@ModelAttribute RespuestaDashboard respuesta, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElse(null);
            respuesta.setUsuario(usuario);
        }
        respuestaService.guardarRespuesta(respuesta);
        return "redirect:/api/logiado";
    }

    // ==================== ÁREA ADMINISTRACIÓN ====================

    @GetMapping("/admin")
    public String adminPage(Model model) {
        try {
            long totalUsuarios = usuarioService.contarUsuarios();
            long totalReservas = reservaService.contarReservas();
            long totalMultas = multaService.contarMultas();
            long totalResenas = resenaService.contarResenas();
            long totalLibros = libroService.contarLibros();
            long totalLibrosFisicos = libroFisicoService.contarLibrosFisicos();

            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("totalReservas", totalReservas);
            model.addAttribute("totalMultas", totalMultas);
            model.addAttribute("totalResenas", totalResenas);
            model.addAttribute("totalLibros", totalLibros + totalLibrosFisicos);

        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas del admin: " + e.getMessage());
            e.printStackTrace();
        }
        return "admin";
    }

    // ========== DASHBOARD CON ESTADÍSTICAS ==========

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            long totalUsuarios = usuarioService.obtenerTodosLosUsuarios().size();
            long totalLibros = libroService.obtenerTodosLosLibros().size();
            long totalLibrosFisicos = libroFisicoService.obtenerTodosLosLibrosFisicos().size();
            long totalReservas = reservaService.obtenerTodasReservas().size();
            long totalMultas = multaService.obtenerTodasMultas().size();
            long multasPendientes = multaService.obtenerTodasMultas().stream()
                    .filter(m -> !m.isPagada())
                    .count();
            long totalResenas = resenaService.obtenerTodasLasResenas().size();

            double totalMultasPendientes = multaService.obtenerTodasMultas().stream()
                    .filter(m -> !m.isPagada())
                    .mapToDouble(MultaModel::getValorMulta)
                    .sum();

            Map<String, Integer> estadisticasCategorias = reservaService.obtenerEstadisticasCategorias();

            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("totalLibros", totalLibros + totalLibrosFisicos);
            model.addAttribute("totalReservas", totalReservas);
            model.addAttribute("totalMultas", totalMultas);
            model.addAttribute("multasPendientes", multasPendientes);
            model.addAttribute("totalMultasPendientes", totalMultasPendientes);
            model.addAttribute("totalResenas", totalResenas);
            model.addAttribute("estadisticasCategorias", estadisticasCategorias);

            System.out.println("✅ Dashboard cargado con éxito");
            return "dashboard";

        } catch (Exception e) {
            System.err.println("❌ Error al cargar dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el dashboard");
            return "dashboard";
        }
    }

    // ========== GESTIÓN DE USUARIOS ==========

    @GetMapping("/usuariosAdmin")
    public String usuariosAdmin(Model model) {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            System.out.println("✅ Total usuarios cargados: " + usuarios.size());
            model.addAttribute("usuarios", usuarios);
        } catch (Exception e) {
            System.err.println("❌ Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("usuarios", new ArrayList<>());
        }
        return "UsuariosAdmin";
    }

    // ========== GESTIÓN DE LIBROS ==========

    @GetMapping("/addBook")
    public String addBook() {
        return "addBook";
    }

    @PostMapping("/agregarLibro")
    @ResponseBody
    public ResponseEntity<?> agregarLibro(@RequestParam String titulo,
                                          @RequestParam String url,
                                          @RequestParam String autor,
                                          @RequestParam String descripcion,
                                          @RequestParam String categoria) {
        try {
            LibroModel libro = new LibroModel();
            libro.setTitulo(titulo);
            libro.setUrl(url);
            libro.setAutor(autor);
            libro.setDescripcion(descripcion);
            libro.setCategoria(categoria);
            libroService.guardarLibro(libro);
            return ResponseEntity.ok().body("Libro agregado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hubo un error al agregar el libro");
        }
    }

    @GetMapping("/addLibroF")
    public String addLibroF() {
        return "addLibroF";
    }

    @PostMapping("/agregarLibroF")
    @ResponseBody
    public ResponseEntity<?> agregarLibroF(@RequestBody LibroFisicoModel libroFisico) {
        try {
            libroFisicoService.guardarLibroFisico(libroFisico);
            return ResponseEntity.ok().body("Libro físico agregado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hubo un error al agregar el libro físico");
        }
    }

    @GetMapping("/cargarLibrosPorCategoria")
    @ResponseBody
    public List<LibroFisicoModel> cargarLibrosPorCategoria(@RequestParam String categoria) {
        return libroFisicoService.obtenerLibrosFisicosPorCategoria(categoria);
    }

    // ========== GESTIÓN DE RESERVAS ==========

    @GetMapping("/reservaAdmin")
    public String reservaAdmin(@RequestParam(value = "categoria", required = false) String categoria, Model model) {
        model.addAttribute("categorias", List.of("Novelas", "Ciencia", "Historia", "Arte", "Fantasía", "Filosofía"));
        if (categoria != null) {
            model.addAttribute("librosPorCategoria", libroFisicoService.obtenerLibrosFisicosPorCategoria(categoria));
            model.addAttribute("categoriaSeleccionada", categoria);
        }
        return "reservaAdmin";
    }

    @PostMapping("/guardarReservaUsuario")
    @ResponseBody
    public ResponseEntity<?> guardarReservaUsuario(@RequestParam String nombreCompleto,
                                                   @RequestParam String correo,
                                                   @RequestParam String categoria,
                                                   @RequestParam String libro,
                                                   @RequestParam String fecha,
                                                   Authentication authentication) {
        try {
            Usuario usuario = null;
            if (authentication != null && authentication.isAuthenticated()) {
                usuario = usuarioRepository.findByUsername(authentication.getName()).orElse(null);
            }

            if (usuario == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "error", "Debes iniciar sesión para realizar una reserva"
                ));
            }

            ReservaModel reserva = new ReservaModel(
                    usuario, nombreCompleto, correo, categoria, libro, LocalDate.parse(fecha)
            );
            reservaService.crearReserva(reserva);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = LocalDate.parse(fecha).format(formatter);

            emailService.enviarConfirmacionReservaHTML(
                    correo,
                    nombreCompleto,
                    libro,
                    fechaFormateada,
                    categoria
            );

            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "mensaje", "Reserva realizada con éxito."
            ));
        } catch (Exception e) {
            System.err.println("❌ Error al guardar reserva: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", "Error al realizar la reserva. Inténtalo nuevamente."
            ));
        }
    }

    // ========== GESTIÓN DE DEVOLUCIONES ==========

    @GetMapping("/devolucion")
    public String libroDevolucion(Model model) {
        List<ReservaModel> reservas = reservaService.obtenerTodasReservas();
        model.addAttribute("reservas", reservas);
        return "devolucion";
    }

    @PostMapping("/procesar-devolucion")
    public String procesarDevolucion(
            @RequestParam String reservaId,
            @RequestParam(required = false) Integer diasRetraso,
            @RequestParam(required = false) Double valorMulta,
            @RequestParam String fechaDevolucion,
            RedirectAttributes redirectAttributes) {

        ReservaModel reserva = reservaService.obtenerReservaPorId(reservaId);

        if (reserva == null) {
            redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
            return "redirect:/api/devolucion";
        }

        LocalDate fechaDev = LocalDate.parse(fechaDevolucion);

        if (diasRetraso != null && diasRetraso > 0 && valorMulta != null) {
            MultaModel multa = new MultaModel(
                    reserva.getUsuario(),
                    reserva.getNombreCompleto(),
                    reserva.getLibro(),
                    reserva.getFecha(),
                    fechaDev,
                    diasRetraso,
                    valorMulta
            );
            multaService.crearMulta(multa);

            redirectAttributes.addFlashAttribute("mensaje",
                    "Devolución registrada con multa de $" + valorMulta + " COP");
        } else {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Devolución registrada sin multa");
        }

        reservaService.eliminarReserva(reservaId);

        return "redirect:/api/devolucion";
    }

    // ========== GESTIÓN DE MULTAS ==========

    @GetMapping("/multasAdmin")
    public String multasAdmin(Model model) {
        try {
            List<MultaModel> multas = multaService.obtenerTodasMultas();
            System.out.println("✅ Total multas cargadas: " + multas.size());
            model.addAttribute("multas", multas);
        } catch (Exception e) {
            System.err.println("❌ Error al cargar multas: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("multas", new ArrayList<>());
        }
        return "multasAdmin";
    }

    @PostMapping("/multas/pagar/{id}")
    public String pagarMulta(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            MultaModel multa = multaService.marcarComoPagada(id);
            if (multa != null) {
                redirectAttributes.addFlashAttribute("mensaje", "Multa marcada como pagada");
            } else {
                redirectAttributes.addFlashAttribute("error", "Multa no encontrada");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pago");
        }
        return "redirect:/api/multasAdmin";
    }

    // ========== GESTIÓN DE RESEÑAS ==========

    @GetMapping("/resenasAdmin")
    public String resenasAdmin(Model model) {
        try {
            List<ResenaModel> resenas = resenaService.obtenerTodasLasResenas();
            System.out.println("✅ Total reseñas cargadas: " + resenas.size());
            model.addAttribute("resenas", resenas);
        } catch (Exception e) {
            System.err.println("❌ Error al cargar reseñas: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("resenas", new ArrayList<>());
        }
        return "resenasAdmin";
    }

    // ========== MODELO PREDICTIVO / ESTADÍSTICAS ==========

    @GetMapping("/modeloPredictivo")
    public String mostrarModeloPredictivo(Model model) {
        try {
            String categoriaMasReservada = reservaService.obtenerCategoriaMasReservada();
            Map<String, Integer> estadisticas = reservaService.obtenerEstadisticasCategorias();

            Map<String, Integer> estadisticasOrdenadas = estadisticas.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            model.addAttribute("categoriaMasReservada", categoriaMasReservada);
            model.addAttribute("estadisticas", estadisticasOrdenadas);

            return "modeloPredictivo";

        } catch (Exception e) {
            System.err.println("❌ Error al cargar estadísticas: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar estadísticas: " + e.getMessage());
            model.addAttribute("categoriaMasReservada", "N/A");
            model.addAttribute("estadisticas", new LinkedHashMap<>());

            return "modeloPredictivo";
        }
    }

    @GetMapping("/categoria-mas-reservada")
    @ResponseBody
    public ResponseEntity<?> obtenerCategoriaMasReservada() {
        try {
            String categoriaMasReservada = reservaService.obtenerCategoriaMasReservada();
            return ResponseEntity.ok().body(Map.of("categoriaMasReservada", categoriaMasReservada));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al obtener la categoría más reservada: " + e.getMessage()));
        }
    }

    @GetMapping("/estadisticas-categorias")
    @ResponseBody
    public ResponseEntity<?> obtenerEstadisticasCategorias() {
        try {
            Map<String, Integer> estadisticas = reservaService.obtenerEstadisticasCategorias();
            return ResponseEntity.ok().body(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al obtener estadísticas: " + e.getMessage()));
        }
    }

    // ========== OTRAS RUTAS ==========

    @GetMapping("/mensajeRegistro")
    public String mensajeRegistro() {
        return "mensajeRegistro";
    }

    @GetMapping("/mensajeReserva")
    public String mensajeReserva() {
        return "mensajeReserva";
    }

    @GetMapping("/mensajeReservaUsuario")
    public String mensajeReservaUsuario() {
        return "mensajeReservaUsuario";
    }

    @GetMapping("/confirmacionLibro")
    public String confirmacionLibro() {
        return "confirmacionLibro";
    }

    @GetMapping("/noAdminConfig")
    public String noAdminConfig() {
        return "noAdminConfig";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/pago")
    public String pagoPage() {
        return "pago";
    }

    @GetMapping("/conocerte")
    public String conocertePage() {
        return "conocerte";
    }

    @GetMapping("/libros")
    public String libros() {
        return "libros";
    }

    @GetMapping("/historia_pensamiento")
    public String historia_pensamiento() {
        return "historia_pensamiento";
    }

    @GetMapping("/desafio_principito")
    public String desafio_principito() {
        return "desafio_principito";
    }

    @GetMapping("/desafio_harrypotter")
    public String desafio_harrypotter() {
        return "desafio_harrypotter";
    }

    @GetMapping("/desafio_1984")
    public String desafio_1984() {
        return "desafio_1984";
    }

    @GetMapping("/librosPorCategoria/{categoria}")
    public String mostrarLibrosPorCategoria(@PathVariable String categoria, Model model) {
        List<LibroModel> libros = libroService.obtenerLibrosPorCategoria(categoria);
        model.addAttribute("libros", libros);
        model.addAttribute("categoria", categoria);
        return "librosPorCategoria";
    }

    @PostMapping("/reservar/{id}")
    public String reservarLibroFisico(@PathVariable String id, Model model) {
        try {
            boolean reservado = libroFisicoService.reservarLibroFisico(id);

            if (reservado) {
                model.addAttribute("mensajeExito", "Reserva exitosa! El stock se ha actualizado correctamente.");
            } else {
                model.addAttribute("mensajeError", "No hay stock disponible o el libro no existe.");
            }

        } catch (Exception e) {
            System.err.println("Error al reservar libro físico: " + e.getMessage());
            model.addAttribute("mensajeError", "Ocurrió un error al procesar la reserva.");
        }

        model.addAttribute("librosFisicos", libroFisicoService.obtenerTodosLosLibrosFisicos());
        return "reservaLibro";
    }

    @PostMapping("/cancelar-reserva/{id}")
    public String cancelarReservaLibroFisico(@PathVariable String id, Model model) {
        try {
            boolean cancelado = libroFisicoService.cancelarReservaLibroFisico(id);

            if (cancelado) {
                model.addAttribute("mensaje", "Reserva cancelada. El stock se ha restablecido.");
            } else {
                model.addAttribute("mensaje", "No hay reservas para cancelar o el libro no existe.");
            }
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al cancelar la reserva: " + e.getMessage());
        }

        model.addAttribute("librosFisicos", libroFisicoService.obtenerTodosLosLibrosFisicos());
        return "reservaLibro";
    }

    // ==================== OPTIMIZADOR MILP ====================

    @Autowired
    private OptimizadorService optimizadorService;                          // ✅ FIX: inyección correcta del bean

    @Autowired
    private com.bibli.bia.repository.LibroOptimizadorRepository libroOptimizadorRepository;

    @GetMapping("/optimizador")
    public String optimizadorFormulario(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("solicitud", new SolicitudOptimizacion());       // ✅ FIX: paquete correcto (com.bibli.bia.Model)
        model.addAttribute("totalLibros", libroOptimizadorRepository.findByDisponibleTrue().size());
        model.addAttribute("categorias", List.of(
                "Clásico", "Distopía", "Ciencia ficción", "Fantasía", "Thriller",
                "Romance", "Historia", "Ficción", "Autoayuda", "Tecnología",
                "Negocios", "Terror", "Poesía", "Filosofía", "Infantil",
                "Misterio", "Ciencia", "Fábula"
        ));
        return "formulario";
    }

    @PostMapping("/optimizador")
    public String optimizadorCalcular(
            @Valid @ModelAttribute("solicitud") SolicitudOptimizacion solicitud, // ✅ FIX: paquete correcto
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("totalLibros", libroOptimizadorRepository.findByDisponibleTrue().size());
            model.addAttribute("categorias", List.of(
                    "Clásico", "Distopía", "Ciencia ficción", "Fantasía", "Thriller",
                    "Romance", "Historia", "Ficción", "Autoayuda", "Tecnología",
                    "Negocios", "Terror", "Poesía", "Filosofía", "Infantil",
                    "Misterio", "Ciencia", "Fábula"
            ));
            return "formulario";
        }

        // ✅ FIX: se llama al bean inyectado, no como método estático de la clase equivocada
        ResultadoOptimizacion resultado = optimizadorService.optimizar(solicitud);
        model.addAttribute("resultado", resultado);
        model.addAttribute("solicitud", solicitud);
        return "resultado";
    }
}