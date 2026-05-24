
/*package com.bibli.bia.config;

import com.bibli.bia.Model.*;
import com.bibli.bia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroFisicoRepository libroFisicoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private Resenarepository resenaRepository;

    @Autowired
    private RespuestaDashboardRepository respuestaDashboardRepository;

    @Autowired
    private ProgresoLecturaRepository progresoLecturaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        System.out.println("🚀 Iniciando carga de datos de ejemplo...");

        // ==================== 1. USUARIOS (30) ====================
        List<Usuario> usuarios = new ArrayList<>();
        String[] nombres = {"admin", "user", "davidpadillam", "maria_gomez", "juan_perez",
                "carla_ruiz", "andres_mejia", "lucia_fernandez", "pedro_ramirez",
                "sofia_castro", "diego_herrera", "valentina_ortiz", "carlos_mora",
                "natalia_restrepo", "felipe_jimenez", "daniela_vasquez", "oscar_lopez",
                "paula_sanchez", "ricardo_martinez", "camila_rodriguez",
                // NUEVOS USUARIOS (10 más - INACTIVOS)
                "ana_garcia", "luis_torres", "elena_mendoza", "javier_reyes",
                "patricia_galindo", "roberto_navarro", "laura_castillo",
                "fernando_herrera", "isabella_romero", "miguel_angel"};

        String[] contrasenas = {"admin123", "user123", "david123", "maria123", "juan123",
                "carla123", "andres123", "lucia123", "pedro123", "sofia123",
                "diego123", "vale123", "carlos123", "nata123", "felipe123",
                "daniela123", "oscar123", "paula123", "ricardo123", "camila123",
                // Contraseñas para nuevos usuarios
                "ana123", "luis123", "elena123", "javier123", "patricia123",
                "roberto123", "laura123", "fernando123", "isabella123", "miguel123"};

// Crear usuarios con estado activo/inactivo
        for (int i = 0; i < 30; i++) {
            Optional<Usuario> existente = usuarioRepository.findByUsername(nombres[i]);
            if (existente.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsername(nombres[i]);
                usuario.setPassword(passwordEncoder.encode(contrasenas[i]));

                // Configurar roles
                if (i == 0) {
                    usuario.setRoles(Set.of("ADMIN"));
                } else {
                    usuario.setRoles(Set.of("USER"));
                }

                // Configurar estado activo/inactivo
                // Los primeros 20 usuarios son ACTIVOS, los últimos 10 son INACTIVOS
                if (i < 20) {
                    usuario.setActivo(true);  // Usuarios activos
                } else {
                    usuario.setActivo(false); // Usuarios inactivos
                }

                usuarios.add(usuarioRepository.save(usuario));
                System.out.println("✅ Usuario creado: " + nombres[i] + " (Activo: " + usuario.getActivo() + ")");
            } else {
                usuarios.add(existente.get());
                System.out.println("⚠️ Usuario ya existe: " + nombres[i] + " (Activo: " + existente.get().getActivo() + ")");
            }
        }

        // ==================== 2. LIBROS VIRTUALES (20) - SIN DUPLICADOS ====================
        String[] titulosVirtuales = {
                "Cien Años de Soledad", "El Amor en los Tiempos del Cólera", "Crónica de una Muerte Anunciada",
                "El Principito", "1984", "Rebelión en la Granja", "Un Mundo Feliz", "Fahrenheit 451",
                "El Hobbit", "El Señor de los Anillos", "Harry Potter y la Piedra Filosofal",
                "Don Quijote de la Mancha", "La Odisea", "La Ilíada", "Orgullo y Prejuicio",
                "Cumbres Borrascosas", "Jane Eyre", "Moby Dick", "La Metamorfosis", "El Extranjero"
        };

        String[] autoresVirtuales = {
                "Gabriel García Márquez", "Gabriel García Márquez", "Gabriel García Márquez",
                "Antoine de Saint-Exupéry", "George Orwell", "George Orwell", "Aldous Huxley", "Ray Bradbury",
                "J.R.R. Tolkien", "J.R.R. Tolkien", "J.K. Rowling",
                "Miguel de Cervantes", "Homero", "Homero", "Jane Austen",
                "Emily Brontë", "Charlotte Brontë", "Herman Melville", "Franz Kafka", "Albert Camus"
        };

        String[] categoriasVirtuales = {
                "Novelas", "Novelas", "Novelas", "Fantasía", "Ciencia", "Ciencia", "Ciencia", "Ciencia",
                "Fantasía", "Fantasía", "Fantasía", "Novelas", "Historia", "Historia", "Novelas",
                "Novelas", "Novelas", "Novelas", "Filosofía", "Filosofía"
        };

        String[] descripcionesVirtuales = {
                "La historia de la familia Buendía en Macondo", "Una historia de amor eterno", "Un crimen anunciado",
                "Un clásico de la literatura infantil", "Una distopía sobre el control total", "Una sátira política",
                "Una sociedad perfecta y sus sacrificios", "Una sociedad que quema libros",
                "La aventura de Bilbo Bolsón", "La épica lucha por el anillo", "La magia de Hogwarts",
                "El ingenioso hidalgo de la Mancha", "Las aventuras de Ulises", "La guerra de Troya",
                "El orgullo y los prejuicios del amor", "La pasión y la venganza", "La independencia femenina",
                "La obsesión por la ballena blanca", "La transformación de Gregorio Samsa", "El absurdo de la vida"
        };

        String[] urlsVirtuales = {
                "https://www.gutenberg.org/ebooks/1", "https://www.gutenberg.org/ebooks/2", "https://www.gutenberg.org/ebooks/3",
                "https://www.gutenberg.org/ebooks/4", "https://www.gutenberg.org/ebooks/5", "https://www.gutenberg.org/ebooks/6",
                "https://www.gutenberg.org/ebooks/7", "https://www.gutenberg.org/ebooks/8", "https://www.gutenberg.org/ebooks/9",
                "https://www.gutenberg.org/ebooks/10", "https://www.gutenberg.org/ebooks/11", "https://www.gutenberg.org/ebooks/12",
                "https://www.gutenberg.org/ebooks/13", "https://www.gutenberg.org/ebooks/14", "https://www.gutenberg.org/ebooks/15",
                "https://www.gutenberg.org/ebooks/16", "https://www.gutenberg.org/ebooks/17", "https://www.gutenberg.org/ebooks/18",
                "https://www.gutenberg.org/ebooks/19", "https://www.gutenberg.org/ebooks/20"
        };

        List<LibroModel> librosVirtuales = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<LibroModel> existentes = libroRepository.findByTituloContainingIgnoreCase(titulosVirtuales[i]);
            if (existentes.isEmpty()) {
                LibroModel libro = new LibroModel();
                libro.setTitulo(titulosVirtuales[i]);
                libro.setAutor(autoresVirtuales[i]);
                libro.setCategoria(categoriasVirtuales[i]);
                libro.setDescripcion(descripcionesVirtuales[i]);
                libro.setUrl(urlsVirtuales[i]);
                librosVirtuales.add(libroRepository.save(libro));
                System.out.println("✅ Libro virtual creado: " + titulosVirtuales[i]);
            } else {
                librosVirtuales.add(existentes.get(0));
                System.out.println("⚠️ Libro virtual ya existe: " + titulosVirtuales[i]);
            }
        }

        // ==================== 3. LIBROS FÍSICOS (20) ====================
        String[] titulosFisicos = {
                "El Laberinto de la Soledad", "La Fiesta del Chivo", "La Ciudad y los Perros",
                "Rayuela", "El Túnel", "Sobre Héroes y Tumbas", "El Aleph", "Ficciones",
                "La Vorágine", "María", "Aura", "Pedro Páramo", "El Llano en Llamas",
                "Los Pasos Perdidos", "La Casa de los Espíritus", "Eva Luna", "De Amor y de Sombra",
                "El Beso de la Mujer Araña", "La Tregua", "El Coronel No Tiene Quien Le Escriba"
        };

        String[] autoresFisicos = {
                "Octavio Paz", "Mario Vargas Llosa", "Mario Vargas Llosa",
                "Julio Cortázar", "Ernesto Sábato", "Ernesto Sábato", "Jorge Luis Borges", "Jorge Luis Borges",
                "José Eustasio Rivera", "Jorge Isaacs", "Carlos Fuentes", "Juan Rulfo", "Juan Rulfo",
                "Alejo Carpentier", "Isabel Allende", "Isabel Allende", "Isabel Allende",
                "Manuel Puig", "Mario Benedetti", "Gabriel García Márquez"
        };

        String[] categoriasFisicas = {
                "Filosofía", "Novelas", "Novelas", "Novelas", "Novelas", "Novelas", "Filosofía", "Filosofía",
                "Novelas", "Novelas", "Fantasía", "Novelas", "Novelas", "Novelas", "Novelas", "Novelas",
                "Novelas", "Novelas", "Novelas", "Novelas"
        };

        String[] descripcionesFisicas = {
                "Reflexión sobre la identidad mexicana", "Dictadura en República Dominicana", "Vida en un colegio militar",
                "Una novela experimental", "Obsesión y crimen", "La historia de los Olmos", "Cuentos fantásticos", "Laberintos literarios",
                "La selva amazónica", "Amor en el Valle del Cauca", "Historia de fantasmas", "La obra maestra de Rulfo", "Cuentos de la Revolución Mexicana",
                "La búsqueda del origen", "La familia Trueba", "Una narradora nómada", "Dictadura y amor",
                "Dos presos en una celda", "La rutina del empleado", "La espera infinita de un coronel"
        };

        List<LibroFisicoModel> librosFisicos = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            // Verificar si ya existe para no duplicar
            List<LibroFisicoModel> existentes = libroFisicoRepository.findByTituloContainingIgnoreCase(titulosFisicos[i]);
            if (existentes.isEmpty()) {
                LibroFisicoModel libroFisico = new LibroFisicoModel();
                libroFisico.setTitulo(titulosFisicos[i]);
                libroFisico.setAutor(autoresFisicos[i]);
                libroFisico.setCategoria(categoriasFisicas[i]);
                libroFisico.setDescripcion(descripcionesFisicas[i]);
                libroFisico.setStock(10 + (i % 15));
                libroFisico.setReservado(0);
                librosFisicos.add(libroFisicoRepository.save(libroFisico));
                System.out.println("✅ Libro físico creado: " + titulosFisicos[i]);
            } else {
                librosFisicos.add(existentes.get(0));
                System.out.println("⚠️ Libro físico ya existe: " + titulosFisicos[i]);
            }
        }

        // ==================== 4. RESERVAS NUEVAS (2025 y 2026) ====================
        // Combinar todos los libros disponibles (virtuales + físicos)
        List<String> todosLosLibros = new ArrayList<>();
        List<String> todasLasCategorias = new ArrayList<>();

        for (LibroModel libro : librosVirtuales) {
            todosLosLibros.add(libro.getTitulo());
            todasLasCategorias.add(libro.getCategoria());
        }

        for (LibroFisicoModel libro : librosFisicos) {
            todosLosLibros.add(libro.getTitulo());
            todasLasCategorias.add(libro.getCategoria());
        }

        // Crear 30 reservas nuevas (2025 y 2026)
        System.out.println("📚 Creando reservas para 2025 y 2026...");

        // Fechas para reservas: 15 en 2025 y 15 en 2026
        LocalDate[] fechasReservas = new LocalDate[30];
        for (int i = 0; i < 15; i++) {
            // Reservas en 2025 (enero a diciembre)
            fechasReservas[i] = LocalDate.of(2025, 1 + (i % 12), 1 + (i % 28));
        }
        for (int i = 15; i < 30; i++) {
            // Reservas en 2026 (enero a mayo - lo que llevamos)
            fechasReservas[i] = LocalDate.of(2026, 1 + ((i - 15) % 5), 1 + ((i - 15) % 28));
        }

        for (int i = 0; i < 30; i++) {
            int libroIndex = i % todosLosLibros.size();
            ReservaModel reserva = new ReservaModel(
                    usuarios.get(i % usuarios.size()),
                    usuarios.get(i % usuarios.size()).getUsername(),
                    usuarios.get(i % usuarios.size()).getUsername() + "@email.com",
                    todasLasCategorias.get(libroIndex),
                    todosLosLibros.get(libroIndex),
                    fechasReservas[i]
            );
            reservaRepository.save(reserva);
            System.out.println("✅ Reserva creada: " + todosLosLibros.get(libroIndex) + " - Fecha: " + fechasReservas[i]);
        }

        // ==================== 5. MULTAS (una pagada y una no pagada por usuario nuevo) ====================
        // ==================== 5. MULTAS (una pagada y una no pagada por usuario nuevo) ====================
        System.out.println("📚 Creando multas para usuarios...");

// Para los primeros 20 usuarios (antiguos), crear multas aleatorias
        for (int i = 0; i < 20; i++) {
            int libroIndex = i % todosLosLibros.size();
            int mesReserva = 1 + (i % 12);  // Entre 1 y 12
            int mesDevolucion = 1 + ((i + 1) % 12);  // Entre 1 y 12

            MultaModel multa = new MultaModel(
                    usuarios.get(i),
                    usuarios.get(i).getUsername(),
                    todosLosLibros.get(libroIndex),
                    LocalDate.of(2024, mesReserva, 1 + (i % 28)),
                    LocalDate.of(2024, mesDevolucion, 5 + (i % 28)),
                    5 + (i % 15),
                    5000.0 + (i * 1000)
            );
            multaRepository.save(multa);
            System.out.println("✅ Multa creada para usuario antiguo: " + usuarios.get(i).getUsername());
        }

// Para los 10 usuarios nuevos (20-29): cada uno tiene una multa pagada y una no pagada
        for (int i = 20; i < 30; i++) {
            int libroIndex1 = (i * 2) % todosLosLibros.size();
            int libroIndex2 = (i * 2 + 1) % todosLosLibros.size();

            // Multa NO PAGADA (fecha fin posterior a hoy)
            MultaModel multaNoPagada = new MultaModel(
                    usuarios.get(i),
                    usuarios.get(i).getUsername(),
                    todosLosLibros.get(libroIndex1),
                    LocalDate.of(2025, 10 + ((i - 20) % 3), 1 + ((i - 20) % 28)),
                    LocalDate.of(2026, 1 + ((i - 20) % 5), 15 + ((i - 20) % 28)),
                    10 + ((i - 20) % 20),
                    10000.0 + ((i - 20) * 500)
            );
            multaRepository.save(multaNoPagada);

            // Multa PAGADA (fecha fin anterior a hoy)
            MultaModel multaPagada = new MultaModel(
                    usuarios.get(i),
                    usuarios.get(i).getUsername(),
                    todosLosLibros.get(libroIndex2),
                    LocalDate.of(2025, 1 + ((i - 20) % 6), 1 + ((i - 20) % 28)),
                    LocalDate.of(2025, 3 + ((i - 20) % 6), 10 + ((i - 20) % 28)),
                    5 + ((i - 20) % 10),
                    5000.0 + ((i - 20) * 300)
            );
            multaRepository.save(multaPagada);

            System.out.println("✅ Multas creadas para usuario nuevo: " + usuarios.get(i).getUsername() +
                    " (1 pagada + 1 no pagada)");
        }
        // ==================== 6. RESEÑAS (30 reseñas) ====================
        String[] comentarios = {
                "Excelente libro, lo recomiendo", "Una obra maestra", "Me encantó la historia",
                "No me gustó mucho", "Interesante perspectiva", "De mis favoritos",
                "Lo leí en una semana", "Atrapante desde el inicio", "Un clásico imperdible",
                "Muy bien escrito", "Reflexivo y profundo", "Divertido y entretenido",
                "Cambió mi forma de pensar", "Lo volvería a leer", "Un poco extenso pero vale la pena",
                "Personajes memorables", "Final sorprendente", "Historia conmovedora",
                "Muy educativo", "Inspirador", "No pude soltarlo", "Historia única",
                "Me dejó pensando", "Increíble narrativa", "Lo recomendaría a todos",
                "Una joya literaria", "Superó mis expectativas", "Para leer varias veces",
                "Emocionante de principio a fin", "Un verdadero clásico"
        };

        for (int i = 0; i < 30; i++) {
            ResenaModel resena = new ResenaModel(
                    usuarios.get(i % usuarios.size()),
                    usuarios.get(i % usuarios.size()).getUsername(),
                    comentarios[i % comentarios.length]
            );
            resenaRepository.save(resena);
            System.out.println("✅ Reseña creada por: " + usuarios.get(i % usuarios.size()).getUsername());
        }

        // ==================== 7. RESPUESTAS DASHBOARD (30) ====================
        Integer[] edades = {18, 22, 25, 30, 28, 35, 40, 19, 24, 27, 32, 29, 26, 31, 33, 34, 36, 38, 41, 45,
                23, 37, 42, 21, 44, 39, 20, 43, 46, 47}; // Edades para los 10 nuevos

        String[] generos = {"Masculino", "Femenino", "Masculino", "Femenino", "Masculino", "Femenino", "Masculino",
                "Femenino", "Masculino", "Femenino", "Masculino", "Femenino", "Masculino", "Femenino",
                "Masculino", "Femenino", "Masculino", "Femenino", "Masculino", "Femenino",
                "Masculino", "Femenino", "Masculino", "Femenino", "Masculino", "Femenino",
                "Masculino", "Femenino", "Masculino", "Femenino"};

        String[] frecuencias = {"Diaria", "Semanal", "Mensual", "Ocasional", "Diaria", "Semanal", "Mensual",
                "Ocasional", "Diaria", "Semanal", "Mensual", "Ocasional", "Diaria", "Semanal",
                "Mensual", "Ocasional", "Diaria", "Semanal", "Mensual", "Ocasional",
                "Diaria", "Semanal", "Mensual", "Ocasional", "Diaria", "Semanal",
                "Mensual", "Ocasional", "Diaria", "Semanal"};

        for (int i = 0; i < 30; i++) {
            RespuestaDashboard respuesta = new RespuestaDashboard();
            respuesta.setUsuario(usuarios.get(i % usuarios.size()));
            respuesta.setEdad(edades[i]);
            respuesta.setGenero(generos[i]);
            respuesta.setFrecuencia(frecuencias[i]);
            respuesta.setCategoriaFavorita(todasLasCategorias.get(i % todasLasCategorias.size()));
            respuesta.setFormato(i % 2 == 0 ? "Virtual" : "Físico");
            respuesta.setUso(i % 3 == 0 ? "Sí" : "No");
            respuesta.setLibrosMes(1 + (i % 10));
            respuesta.setCalificacion(3 + (i % 8));
            respuesta.setFechaRegistro(LocalDateTime.now().minusDays(i));
            respuestaDashboardRepository.save(respuesta);
            System.out.println("✅ Respuesta Dashboard creada para: " + usuarios.get(i % usuarios.size()).getUsername());
        }

        // ==================== 8. PROGRESO LECTURA (30) ====================
        for (int i = 0; i < 30 && i < usuarios.size(); i++) {
            Optional<ProgresoLectura> existente = progresoLecturaRepository.findByUsuario(usuarios.get(i));
            if (existente.isEmpty()) {
                ProgresoLectura progreso = new ProgresoLectura(usuarios.get(i));

                Map<String, Set<Integer>> capitulosMap = new HashMap<>();
                Set<Integer> capitulosLeidos = new HashSet<>();
                for (int j = 1; j <= (i % 8) + 1; j++) {
                    capitulosLeidos.add(j);
                }
                capitulosMap.put(todosLosLibros.get(i % todosLosLibros.size()), capitulosLeidos);
                progreso.setCapitulosPorLibro(capitulosMap);

                Set<String> completados = new HashSet<>();
                if ((i % 6) == 4) {
                    completados.add(todosLosLibros.get(i % todosLosLibros.size()));
                    progreso.setTotalLibrosLeidos(i % 10);
                    progreso.setPuntos(i * 50);
                }
                progreso.setLibrosCompletados(completados);
                progreso.setUltimaActualizacion(LocalDateTime.now());

                progresoLecturaRepository.save(progreso);
                System.out.println("✅ Progreso Lectura creado para: " + usuarios.get(i).getUsername());
            } else {
                System.out.println("⚠️ Progreso Lectura ya existe para: " + usuarios.get(i).getUsername());
            }
        }

        System.out.println(" TODOS los datos de ejemplo han sido cargados exitosamente!");
        System.out.println(" Resumen final:");
        System.out.println("   - Usuarios: " + usuarioRepository.count() + " (20 activos + 10 inactivos)");
        System.out.println("   - Libros Virtuales: " + libroRepository.count());
        System.out.println("   - Libros Físicos: " + libroFisicoRepository.count());
        System.out.println("   - Reservas: " + reservaRepository.count() + " (15 en 2025 + 15 en 2026)");
        System.out.println("   - Multas: " + multaRepository.count() + " (20 antiguos + 10 nuevos con 2 multas c/u)");
        System.out.println("   - Reseñas: " + resenaRepository.count());
        System.out.println("   - Respuestas Dashboard: " + respuestaDashboardRepository.count());
        System.out.println("   - Progreso Lectura: " + progresoLecturaRepository.count());
    }
}*/