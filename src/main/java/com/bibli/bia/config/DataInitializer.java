/*package com.bibli.bia.config;

import com.bibli.bia.Model.LibroFisicoModel;
import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.Model.ReservaModel.EstadoReserva;
import com.bibli.bia.Model.Usuario;
import com.bibli.bia.repository.LibroFisicoRepository;
import com.bibli.bia.repository.ReservaRepository;
import com.bibli.bia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    // ===================== PARÁMETROS =====================
    private static final long SEMILLA = 42L;
    private static final int TOTAL_USUARIOS = 30;            // antes 100
    private static final int TOTAL_EJEMPLARES = 200;         // antes 360
    private static final int TOTAL_RESERVAS_OBJETIVO = 2400; // antes "~2.400" por suerte de Poisson
    private static final int DIAS_HISTORIAL = 730;
    private static final double DEMANDA_BASE_DIARIA = 3.5;
    private static final int MIN_RESERVAS_POR_USUARIO = 20;  // antes 3
    private static final int MIN_COPIAS_POR_TITULO = 3;
    private static final String PASSWORD_INICIAL = "Lectio2026*";

    private static final double[] ESTACIONAL = {
            0.75, 1.25, 1.30, 1.15, 1.10, 0.85, 0.70, 1.25, 1.30, 1.15, 1.05, 0.60
    };
    private static final double[] FACTOR_SEMANAL = {1.1, 1.2, 1.2, 1.1, 1.0, 0.6, 0.2};
    private static final Map<String, Double> PESO_CATEGORIA = Map.of(
            "Novelas", 0.26, "Fantasía", 0.22, "Ciencia", 0.16,
            "Historia", 0.14, "Filosofía", 0.12, "Arte", 0.10);
    private static final double[] POPULARIDAD = {1.0, 0.75, 0.55, 0.40, 0.30};
    private static final Set<String> RECREATIVAS = Set.of("Novelas", "Fantasía");

    private static final String[] NOMBRES = {
            "Juan","María","Carlos","Laura","Andrés","Camila","Santiago","Valentina","Daniel",
            "Sofía","Luis","Isabella","Jorge","Mariana","Felipe","Daniela","Sebastián","Natalia",
            "Mateo","Paula","Alejandro","Juliana","Miguel","Andrea","David"};
    private static final String[] APELLIDOS = {
            "García","Rodríguez","Martínez","López","González","Pérez","Sánchez","Ramírez","Torres",
            "Díaz","Vargas","Castro","Rojas","Moreno","Herrera","Medina","Ortega","Cárdenas",
            "Mendoza","Jiménez"};

    private record LibroSeed(String titulo, String autor, String categoria, String descripcion) {}
    private record UsuarioCarga(Usuario usuario, String nombreCompleto) {}

    private static final List<LibroSeed> LIBROS = List.of(
            new LibroSeed("Cien años de soledad","Gabriel García Márquez","Novelas","Saga de la familia Buendía en Macondo."),
            new LibroSeed("1984","George Orwell","Novelas","Distopía sobre un Estado totalitario."),
            new LibroSeed("Crimen y castigo","Fiódor Dostoievski","Novelas","Un estudiante y el peso de su conciencia."),
            new LibroSeed("Don Quijote de la Mancha","Miguel de Cervantes","Novelas","Aventuras de un hidalgo que enloquece leyendo."),
            new LibroSeed("El principito","Antoine de Saint-Exupéry","Novelas","Un piloto y un niño que viaja entre planetas."),
            new LibroSeed("Breve historia del tiempo","Stephen Hawking","Ciencia","Del Big Bang a los agujeros negros."),
            new LibroSeed("Cosmos","Carl Sagan","Ciencia","Recorrido por el universo y nuestro lugar en él."),
            new LibroSeed("El origen de las especies","Charles Darwin","Ciencia","La evolución por selección natural."),
            new LibroSeed("El gen egoísta","Richard Dawkins","Ciencia","La evolución desde el gen."),
            new LibroSeed("Una breve historia de casi todo","Bill Bryson","Ciencia","Viaje por los descubrimientos modernos."),
            new LibroSeed("Sapiens: de animales a dioses","Yuval Noah Harari","Historia","Breve historia de la humanidad."),
            new LibroSeed("Armas, gérmenes y acero","Jared Diamond","Historia","Por qué unas sociedades dominaron a otras."),
            new LibroSeed("Las venas abiertas de América Latina","Eduardo Galeano","Historia","Cinco siglos de explotación."),
            new LibroSeed("Los cañones de agosto","Barbara W. Tuchman","Historia","El primer mes de la Primera Guerra Mundial."),
            new LibroSeed("SPQR: una historia de la antigua Roma","Mary Beard","Historia","Mil años de historia romana."),
            new LibroSeed("Historia del arte","E. H. Gombrich","Arte","Recorrido por el arte desde las cavernas."),
            new LibroSeed("Modos de ver","John Berger","Arte","Ensayos sobre cómo miramos las imágenes."),
            new LibroSeed("Cartas a Theo","Vincent van Gogh","Arte","Correspondencia del pintor con su hermano."),
            new LibroSeed("Leonardo da Vinci","Walter Isaacson","Arte","Biografía del genio renacentista."),
            new LibroSeed("El diario de Frida Kahlo","Frida Kahlo","Arte","Dibujos y pensamientos de la pintora."),
            new LibroSeed("El Hobbit","J. R. R. Tolkien","Fantasía","Bilbo Bolsón y la compañía de enanos."),
            new LibroSeed("La Comunidad del Anillo","J. R. R. Tolkien","Fantasía","Comienza el viaje para destruir el Anillo Único."),
            new LibroSeed("Harry Potter y la piedra filosofal","J. K. Rowling","Fantasía","Un niño descubre que es mago."),
            new LibroSeed("Juego de tronos","George R. R. Martin","Fantasía","Casas nobles se disputan el Trono de Hierro."),
            new LibroSeed("El nombre del viento","Patrick Rothfuss","Fantasía","Kvothe cuenta la historia de su vida."),
            new LibroSeed("Así habló Zaratustra","Friedrich Nietzsche","Filosofía","El superhombre y la muerte de Dios."),
            new LibroSeed("Meditaciones","Marco Aurelio","Filosofía","Reflexiones estoicas del emperador romano."),
            new LibroSeed("La República","Platón","Filosofía","Diálogo sobre la justicia y la ciudad ideal."),
            new LibroSeed("El mundo de Sofía","Jostein Gaarder","Filosofía","Novela sobre la historia de la filosofía."),
            new LibroSeed("El existencialismo es un humanismo","Jean-Paul Sartre","Filosofía","Conferencia sobre libertad y responsabilidad.")
    );

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private LibroFisicoRepository libroFisicoRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${CARGA_INICIAL:true}")
    private boolean cargaInicial;

    @Override
    public void run(String... args) {
        crearAdmin();
        if (!cargaInicial) {
            System.out.println("ℹ️ CARGA_INICIAL=false: no se carga el catálogo inicial.");
            return;
        }
        if (libroFisicoRepository.count() > 0 || reservaRepository.count() > 0) {
            System.out.println("⚠️ Ya existen libros físicos o reservas: no se carga el catálogo inicial.");
            return;
        }

        Random rnd = new Random(SEMILLA);
        List<UsuarioCarga> usuarios = crearUsuarios();
        List<LibroFisicoModel> libros = crearLibros();
        List<ReservaModel> reservas = generarReservas(usuarios, libros, rnd);

        // Post-proceso: garantiza stock ≥ 0 y reservado == count(ACTIVA)
        ajustarInventario(libros, reservas, rnd);

        reservaRepository.saveAll(reservas);       // después de ajustar estados
        imprimirResumen(usuarios, libros, reservas);
    }

    // ===================== ADMIN =====================
    private void crearAdmin() {
        if (usuarioRepository.findByUsername("admin").isPresent()) {
            System.out.println("⚠️ El usuario admin ya existe.");
            return;
        }
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(Set.of("ADMIN"));
        admin.setActivo(true);
        usuarioRepository.save(admin);
        System.out.println("=================================");
        System.out.println("✅ ADMIN CREADO — admin / admin123");
        System.out.println("=================================");
    }

    // ===================== USUARIOS (30) =====================
    private List<UsuarioCarga> crearUsuarios() {
        String hash = passwordEncoder.encode(PASSWORD_INICIAL);
        int combinaciones = NOMBRES.length * APELLIDOS.length;
        List<UsuarioCarga> lista = new ArrayList<>();

        for (int i = 0; i < TOTAL_USUARIOS; i++) {
            int c = (i * 37) % combinaciones;
            String nombre   = NOMBRES[c % NOMBRES.length];
            String apellido1 = APELLIDOS[c / NOMBRES.length];
            String apellido2 = APELLIDOS[(c / NOMBRES.length + 7 + i % 13) % APELLIDOS.length];
            String username  = sinTildes(nombre) + "." + sinTildes(apellido1);

            Usuario usuario = usuarioRepository.findByUsername(username).orElseGet(() -> {
                Usuario u = new Usuario();
                u.setUsername(username);
                u.setPassword(hash);
                u.setRoles(Set.of("USER"));
                u.setActivo(true);
                return usuarioRepository.save(u);
            });
            lista.add(new UsuarioCarga(usuario, nombre + " " + apellido1 + " " + apellido2));
        }
        return lista;
    }

    // ===================== LIBROS FÍSICOS (200 ejemplares) =====================
    private List<LibroFisicoModel> crearLibros() {
        LocalDateTime alta = LocalDate.now().minusDays(DIAS_HISTORIAL).atStartOfDay();
        int[] copias = calcularCopias(LIBROS);
        List<LibroFisicoModel> libros = new ArrayList<>();
        for (int i = 0; i < LIBROS.size(); i++) {
            LibroSeed s = LIBROS.get(i);
            LibroFisicoModel libro = new LibroFisicoModel(
                    s.titulo(), s.autor(), s.descripcion(), s.categoria(), copias[i], 0);
            libro.setFechaCreacion(alta);
            libros.add(libro);
        }
        return libroFisicoRepository.saveAll(libros);
    }

    /*Reparte TOTAL_EJEMPLARES entre los 30 títulos según popularidad. Mínimo 3 cada uno.
    private int[] calcularCopias(List<LibroSeed> semillas) {
        int n = semillas.size();
        int[] copias = new int[n];
        Arrays.fill(copias, MIN_COPIAS_POR_TITULO);
        int sobrante = TOTAL_EJEMPLARES - n * MIN_COPIAS_POR_TITULO;

        Map<String, Integer> pos = new HashMap<>();
        double[] peso = new double[n];
        double sumaPeso = 0;
        for (int i = 0; i < n; i++) {
            int p = pos.merge(semillas.get(i).categoria(), 1, Integer::sum) - 1;
            peso[i] = POPULARIDAD[p];
            sumaPeso += peso[i];
        }

        int[] extra = new int[n];
        int repartido = 0;
        for (int i = 0; i < n; i++) {
            extra[i] = (int) Math.floor(sobrante * peso[i] / sumaPeso);
            repartido += extra[i];
        }
        // Repartir el residuo a los más populares
        Integer[] orden = new Integer[n];
        for (int i = 0; i < n; i++) orden[i] = i;
        Arrays.sort(orden, (a, b) -> Double.compare(peso[b], peso[a]));
        for (int i = 0; i < sobrante - repartido; i++) extra[orden[i]]++;

        for (int i = 0; i < n; i++) copias[i] += extra[i];
        return copias;
    }

    /* Cuota de demanda total por libro (suma 1).
    private double[] calcularCuotas(List<LibroFisicoModel> libros) {
        double[] cuota = new double[libros.size()];
        Map<String, Integer> posicion = new HashMap<>();
        Map<String, Double> sumaPopularidad = new HashMap<>();
        for (int i = 0; i < libros.size(); i++) {
            String cat = libros.get(i).getCategoria();
            int pos = posicion.merge(cat, 1, Integer::sum) - 1;
            cuota[i] = POPULARIDAD[pos];
            sumaPopularidad.merge(cat, POPULARIDAD[pos], Double::sum);
        }
        double total = 0;
        for (int i = 0; i < libros.size(); i++) {
            String cat = libros.get(i).getCategoria();
            cuota[i] = PESO_CATEGORIA.get(cat) * cuota[i] / sumaPopularidad.get(cat);
            total += cuota[i];
        }
        for (int i = 0; i < cuota.length; i++) cuota[i] /= total;
        return cuota;
    }

   /* // ===================== RESERVAS =====================
    private List<ReservaModel> generarReservas(List<UsuarioCarga> usuarios,
                                               List<LibroFisicoModel> libros, Random rnd) {
        record Pendiente(LibroFisicoModel libro, LocalDate dia) {}

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(DIAS_HISTORIAL - 1L);
        double[] cuota = calcularCuotas(libros);

        // 1) Demanda diaria
        List<Pendiente> pendientes = new ArrayList<>();
        for (int d = 0; d < DIAS_HISTORIAL; d++) {
            LocalDate dia = inicio.plusDays(d);
            double tendencia = 0.8 + 0.4 * d / (DIAS_HISTORIAL - 1);
            double base = DEMANDA_BASE_DIARIA * tendencia * FACTOR_SEMANAL[dia.getDayOfWeek().getValue() - 1];
            for (int i = 0; i < libros.size(); i++) {
                LibroFisicoModel libro = libros.get(i);
                double lambda = base * cuota[i] * factorEstacional(libro.getCategoria(), dia.getMonthValue());
                int n = poisson(lambda, rnd);
                for (int k = 0; k < n; k++) pendientes.add(new Pendiente(libro, dia));
            }
        }

        // 2) Truncar exactamente a TOTAL_RESERVAS_OBJETIVO
        Collections.shuffle(pendientes, rnd);
        if (pendientes.size() > TOTAL_RESERVAS_OBJETIVO) {
            pendientes = new ArrayList<>(pendientes.subList(0, TOTAL_RESERVAS_OBJETIVO));
        }

        // 3) Asignar usuario (garantizadas en ronda + resto por peso)
        double[] acumulado = pesosUsuarios(usuarios.size(), rnd);
        int garantizadas = usuarios.size() * MIN_RESERVAS_POR_USUARIO;
        Set<String> usadas = new HashSet<>();
        List<ReservaModel> reservas = new ArrayList<>();

        for (int k = 0; k < pendientes.size(); k++) {
            Pendiente p = pendientes.get(k);
            UsuarioCarga elegido = null;
            for (int intento = 0; intento < 8 && elegido == null; intento++) {
                UsuarioCarga candidato = (k < garantizadas && intento == 0)
                        ? usuarios.get(k % usuarios.size())
                        : usuarios.get(elegirIndice(acumulado, rnd));
                String clave = candidato.usuario().getId() + "|" + p.libro().getId() + "|" + p.dia();
                if (usadas.add(clave)) elegido = candidato;
            }
            if (elegido != null) reservas.add(construirReserva(elegido, p.libro(), p.dia(), hoy, rnd));
        }
        return reservas;
    }

    private ReservaModel construirReserva(UsuarioCarga uc, LibroFisicoModel libro,
                                          LocalDate dia, LocalDate hoy, Random rnd) {
        LocalDate fechaElegida = dia.plusDays(rnd.nextInt(4));
        String correo = uc.usuario().getUsername() + "@example.com";
        ReservaModel r = new ReservaModel(uc.usuario(), uc.nombreCompleto(), correo, libro, fechaElegida);
        r.setFechaCreacion(LocalDateTime.of(dia, LocalTime.of(8 + rnd.nextInt(12), rnd.nextInt(60))));

        long edad = ChronoUnit.DAYS.between(fechaElegida, hoy);
        if (rnd.nextDouble() < 0.08) {
            r.cancelar();
        } else if (edad >= 21 || (edad >= 8 && rnd.nextBoolean())) {
            LocalDate devolucion = fechaElegida.plusDays(7 + rnd.nextInt(15));
            r.marcarDevuelta(devolucion.isAfter(hoy) ? hoy : devolucion);
        } else {
            r.setEstado(EstadoReserva.ACTIVA);
        }
        return r;
    }
    /*-----------------------------------
    private void ajustarInventario(List<LibroFisicoModel> libros,
                                   List<ReservaModel> reservas, Random rnd) {
        LocalDate hoy = LocalDate.now();

        // Capacidad total por libro (stock + reservado ya fueron sembrados como copias)
        Map<String, Integer> capacidad = new HashMap<>();
        for (LibroFisicoModel libro : libros) {
            capacidad.put(libro.getId(), libro.getStock() + libro.getReservado());
        }

        // ACTIVA por libro
        Map<String, List<ReservaModel>> activasPorLibro = new HashMap<>();
        for (ReservaModel r : reservas) {
            if (r.getEstado() == EstadoReserva.ACTIVA) {
                activasPorLibro.computeIfAbsent(r.getLibroFisico().getId(), k -> new ArrayList<>()).add(r);
            }
        }

        // Degradar exceso a DEVUELTA (más antiguas primero)
        for (LibroFisicoModel libro : libros) {
            int cap = capacidad.get(libro.getId());
            List<ReservaModel> activas = activasPorLibro.getOrDefault(libro.getId(), List.of());
            if (activas.size() > cap) {
                List<ReservaModel> ordenadas = activas.stream()
                        .sorted(Comparator.comparing(ReservaModel::getFechaCreacion))
                        .toList();
                int exceso = activas.size() - cap;
                for (int i = 0; i < exceso; i++) {
                    ReservaModel r = ordenadas.get(i);
                    LocalDate creacion = r.getFechaCreacion().toLocalDate();
                    LocalDate devolucion = creacion.plusDays(7 + rnd.nextInt(15));
                    if (devolucion.isAfter(hoy)) devolucion = hoy;
                    r.marcarDevuelta(devolucion);
                }
            }
        }

        // Recalcular y guardar
        Map<String, Long> activasFinal = reservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.ACTIVA)
                .collect(Collectors.groupingBy(r -> r.getLibroFisico().getId(), Collectors.counting()));

        for (LibroFisicoModel libro : libros) {
            int total = capacidad.get(libro.getId());
            int act = activasFinal.getOrDefault(libro.getId(), 0L).intValue();
            libro.setReservado(act);
            libro.setStock(total - act);
        }
        libroFisicoRepository.saveAll(libros);
    }

    // ===================== UTILIDADES =====================
    private static double factorEstacional(String categoria, int mes) {
        double s = ESTACIONAL[mes - 1];
        return RECREATIVAS.contains(categoria) ? 1 + 0.4 * (1 - s) : s;
    }

    private static int poisson(double lambda, Random rnd) {
        double limite = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        do { k++; p *= rnd.nextDouble(); } while (p > limite);
        return k - 1;
    }

    private static double[] pesosUsuarios(int n, Random rnd) {
        double[] acumulado = new double[n];
        double suma = 0;
        for (int i = 0; i < n; i++) {
            suma += 0.3 + Math.pow(rnd.nextDouble(), 2) * 3;
            acumulado[i] = suma;
        }
        return acumulado;
    }

    private static int elegirIndice(double[] acumulado, Random rnd) {
        double x = rnd.nextDouble() * acumulado[acumulado.length - 1];
        for (int i = 0; i < acumulado.length; i++) if (x <= acumulado[i]) return i;
        return acumulado.length - 1;
    }

    private static String sinTildes(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }

    private void imprimirResumen(List<UsuarioCarga> usuarios, List<LibroFisicoModel> libros,
                                 List<ReservaModel> reservas) {
        Map<EstadoReserva, Long> porEstado = reservas.stream()
                .collect(Collectors.groupingBy(ReservaModel::getEstado, Collectors.counting()));
        int totalEjemplares = libros.stream().mapToInt(l -> l.getStock() + l.getReservado()).sum();

        int librosOk = 0, librosError = 0;
        for (LibroFisicoModel l : libros) {
            long activas = reservas.stream()
                    .filter(r -> r.getEstado() == EstadoReserva.ACTIVA
                            && r.getLibroFisico().getId().equals(l.getId()))
                    .count();
            if (l.getStock() >= 0 && l.getReservado() == activas) librosOk++; else librosError++;
        }

        System.out.println("=================================");
        System.out.println("✅ CARGA INICIAL COMPLETADA");
        System.out.println("Usuarios: " + usuarios.size());
        System.out.println("Libros (títulos): " + libros.size() + " — Ejemplares totales: " + totalEjemplares);
        System.out.println("Reservas: " + reservas.size() + " " + porEstado);
        System.out.println("Validación stock/reservas: " + librosOk + " OK, " + librosError + " con error");
        System.out.println("=================================");
    }
/**}*/