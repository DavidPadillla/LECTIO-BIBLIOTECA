package com.bibli.bia.config;

import com.bibli.bia.Model.LibroOptimizador;
import com.bibli.bia.repository.LibroOptimizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoaderOptimizador implements CommandLineRunner {

    @Autowired
    private LibroOptimizadorRepository libroRepositoryOptimizador;

    @Override
    public void run(String... args) {
        if (libroRepositoryOptimizador.count() == 0) {
            List<LibroOptimizador> libros = Arrays.asList(
                    // 1
                    new LibroOptimizador("Cien años de soledad", "Gabriel García Márquez", "Realismo mágico", 471, 12.5, 9, 4.8, true),
                    // 2
                    new LibroOptimizador("Don Quijote de la Mancha", "Miguel de Cervantes", "Clásico", 863, 23.0, 10, 4.9, true),
                    // 3
                    new LibroOptimizador("1984", "George Orwell", "Distopía", 328, 8.7, 9, 4.7, true),
                    // 4
                    new LibroOptimizador("Orgullo y prejuicio", "Jane Austen", "Romance", 432, 11.5, 8, 4.6, true),
                    // 5
                    new LibroOptimizador("El Gran Gatsby", "F. Scott Fitzgerald", "Clásico", 180, 4.8, 8, 4.4, true),
                    // 6
                    new LibroOptimizador("Matar a un ruiseñor", "Harper Lee", "Drama", 336, 8.9, 9, 4.8, true),
                    // 7
                    new LibroOptimizador("Crimen y castigo", "Fiódor Dostoievski", "Novela psicológica", 672, 18.0, 8, 4.7, true),
                    // 8
                    new LibroOptimizador("La odisea", "Homero", "Épica", 416, 11.0, 9, 4.5, true),
                    // 9
                    new LibroOptimizador("Ulises", "James Joyce", "Vanguardista", 736, 19.6, 7, 4.1, true),
                    // 10
                    new LibroOptimizador("La metamorfosis", "Franz Kafka", "Absurdo", 144, 3.8, 8, 4.3, true),
                    // 11
                    new LibroOptimizador("Moby Dick", "Herman Melville", "Aventura", 720, 19.2, 7, 4.2, true),
                    // 12
                    new LibroOptimizador("En busca del tiempo perdido", "Marcel Proust", "Moderno", 2400, 64.0, 6, 4.4, true),
                    // 13
                    new LibroOptimizador("La divina comedia", "Dante Alighieri", "Poema épico", 576, 15.3, 8, 4.5, true),
                    // 14
                    new LibroOptimizador("El principito", "Antoine de Saint-Exupéry", "Fábula", 96, 2.5, 10, 4.9, true),
                    // 15
                    new LibroOptimizador("Hamlet", "William Shakespeare", "Teatro", 160, 4.2, 9, 4.7, true),
                    // 16
                    new LibroOptimizador("Rayuela", "Julio Cortázar", "Experimental", 576, 15.3, 7, 4.3, true),
                    // 17
                    new LibroOptimizador("La sombra del viento", "Carlos Ruiz Zafón", "Misterio", 576, 15.3, 9, 4.6, true),
                    // 18
                    new LibroOptimizador("Harry Potter y la piedra filosofal", "J.K. Rowling", "Fantasía", 309, 8.2, 10, 4.8, true),
                    // 19
                    new LibroOptimizador("El hobbit", "J.R.R. Tolkien", "Fantasía", 310, 8.2, 9, 4.7, true),
                    // 20
                    new LibroOptimizador("El señor de los anillos", "J.R.R. Tolkien", "Épica fantástica", 1178, 31.4, 10, 4.9, true),
                    // 21
                    new LibroOptimizador("Juego de tronos", "George R.R. Martin", "Fantasía oscura", 800, 21.3, 9, 4.5, true),
                    // 22
                    new LibroOptimizador("Los juegos del hambre", "Suzanne Collins", "Distopía", 374, 10.0, 8, 4.3, true),
                    // 23
                    new LibroOptimizador("Divergente", "Veronica Roth", "Distopía", 487, 13.0, 7, 4.0, true),
                    // 24
                    new LibroOptimizador("La chica del tren", "Paula Hawkins", "Suspenso", 496, 13.2, 8, 4.1, true),
                    // 25
                    new LibroOptimizador("Perdida", "Gillian Flynn", "Thriller", 464, 12.4, 8, 4.2, true),
                    // 26
                    new LibroOptimizador("El código Da Vinci", "Dan Brown", "Misterio", 656, 17.5, 9, 4.0, true),
                    // 27
                    new LibroOptimizador("Ángeles y demonios", "Dan Brown", "Suspenso", 616, 16.4, 8, 4.0, true),
                    // 28
                    new LibroOptimizador("La verdad sobre el caso Harry Quebert", "Joël Dicker", "Novela negra", 672, 17.9, 7, 4.1, true),
                    // 29
                    new LibroOptimizador("El psicoanalista", "John Katzenbach", "Thriller", 528, 14.1, 8, 4.2, true),
                    // 30
                    new LibroOptimizador("La chica que soñaba con una cerilla y un bidón de gasolina", "Stieg Larsson", "Policíaco", 672, 17.9, 7, 4.0, true),
                    // 31
                    new LibroOptimizador("Los hombres que no amaban a las mujeres", "Stieg Larsson", "Suspenso", 672, 17.9, 8, 4.3, true),
                    // 32
                    new LibroOptimizador("El nombre de la rosa", "Umberto Eco", "Histórico", 592, 15.8, 8, 4.5, true),
                    // 33
                    new LibroOptimizador("Ficciones", "Jorge Luis Borges", "Cuento", 176, 4.7, 9, 4.8, true),
                    // 34
                    new LibroOptimizador("La casa de los espíritus", "Isabel Allende", "Realismo mágico", 496, 13.2, 8, 4.5, true),
                    // 35
                    new LibroOptimizador("Como agua para chocolate", "Laura Esquivel", "Romance", 208, 5.5, 7, 4.2, true),
                    // 36
                    new LibroOptimizador("Pedro Páramo", "Juan Rulfo", "Realismo mágico", 128, 3.4, 9, 4.6, true),
                    // 37
                    new LibroOptimizador("La tregua", "Mario Benedetti", "Novela íntima", 192, 5.1, 8, 4.4, true),
                    // 38
                    new LibroOptimizador("El amor en los tiempos del cólera", "Gabriel García Márquez", "Romance", 368, 9.8, 8, 4.4, true),
                    // 39
                    new LibroOptimizador("Los detectives salvajes", "Roberto Bolaño", "Moderno", 624, 16.6, 7, 4.3, true),
                    // 40
                    new LibroOptimizador("2666", "Roberto Bolaño", "Monumental", 1120, 29.8, 7, 4.4, true),
                    // 41
                    new LibroOptimizador("Ensayo sobre la ceguera", "José Saramago", "Distopía", 320, 8.5, 8, 4.5, true),
                    // 42
                    new LibroOptimizador("El alquimista", "Paulo Coelho", "Autoayuda", 208, 5.5, 9, 4.2, true),
                    // 43
                    new LibroOptimizador("Once minutos", "Paulo Coelho", "Drama", 240, 6.4, 7, 3.9, true),
                    // 44
                    new LibroOptimizador("El monje que vendió su Ferrari", "Robin Sharma", "Autoayuda", 224, 6.0, 8, 4.1, true),
                    // 45
                    new LibroOptimizador("Padre rico, padre pobre", "Robert Kiyosaki", "Finanzas", 336, 8.9, 9, 4.2, true),
                    // 46
                    new LibroOptimizador("Piense y hágase rico", "Napoleon Hill", "Autoayuda", 288, 7.7, 9, 4.3, true),
                    // 47
                    new LibroOptimizador("Los 7 hábitos de la gente altamente efectiva", "Stephen Covey", "Autoayuda", 384, 10.2, 9, 4.5, true),
                    // 48
                    new LibroOptimizador("El poder del ahora", "Eckhart Tolle", "Espiritual", 224, 6.0, 8, 4.4, true),
                    // 49
                    new LibroOptimizador("Sapiens", "Yuval Noah Harari", "Historia", 496, 13.2, 9, 4.6, true),
                    // 50
                    new LibroOptimizador("Homo Deus", "Yuval Noah Harari", "Futurismo", 480, 12.8, 8, 4.5, true),
                    // 51
                    new LibroOptimizador("21 lecciones para el siglo XXI", "Yuval Noah Harari", "Ensayo", 352, 9.4, 8, 4.4, true),
                    // 52
                    new LibroOptimizador("Breve historia del tiempo", "Stephen Hawking", "Ciencia", 256, 6.8, 9, 4.7, true),
                    // 53
                    new LibroOptimizador("El universo en una cáscara de nuez", "Stephen Hawking", "Divulgación", 224, 6.0, 8, 4.5, true),
                    // 54
                    new LibroOptimizador("La teoría del todo", "Stephen Hawking", "Física", 176, 4.7, 7, 4.3, true),
                    // 55
                    new LibroOptimizador("Cosmos", "Carl Sagan", "Ciencia", 384, 10.2, 9, 4.8, true),
                    // 56
                    new LibroOptimizador("El gen egoísta", "Richard Dawkins", "Biología", 352, 9.4, 8, 4.6, true),
                    // 57
                    new LibroOptimizador("El espejismo de Dios", "Richard Dawkins", "Filosofía", 464, 12.4, 7, 4.2, true),
                    // 58
                    new LibroOptimizador("El fin de la infancia", "Arthur C. Clarke", "Ciencia ficción", 256, 6.8, 8, 4.3, true),
                    // 59
                    new LibroOptimizador("Cita con Rama", "Arthur C. Clarke", "CF", 320, 8.5, 8, 4.4, true),
                    // 60
                    new LibroOptimizador("Fundación", "Isaac Asimov", "CF", 244, 6.5, 9, 4.6, true),
                    // 61
                    new LibroOptimizador("Yo, robot", "Isaac Asimov", "CF", 256, 6.8, 8, 4.5, true),
                    // 62
                    new LibroOptimizador("El fin de la eternidad", "Isaac Asimov", "CF", 224, 6.0, 8, 4.4, true),
                    // 63
                    new LibroOptimizador("Dune", "Frank Herbert", "CF", 688, 18.3, 10, 4.8, true),
                    // 64
                    new LibroOptimizador("El problema de los tres cuerpos", "Liu Cixin", "CF dura", 400, 10.6, 8, 4.5, true),
                    // 65
                    new LibroOptimizador("El bosque oscuro", "Liu Cixin", "CF", 512, 13.6, 8, 4.6, true),
                    // 66
                    new LibroOptimizador("Neuromante", "William Gibson", "Ciberpunk", 320, 8.5, 8, 4.3, true),
                    // 67
                    new LibroOptimizador("Sueñan los androides con ovejas eléctricas?", "Philip K. Dick", "CF", 240, 6.4, 8, 4.4, true),
                    // 68
                    new LibroOptimizador("Ubik", "Philip K. Dick", "CF", 256, 6.8, 7, 4.2, true),
                    // 69
                    new LibroOptimizador("Crónicas marcianas", "Ray Bradbury", "CF poética", 256, 6.8, 8, 4.5, true),
                    // 70
                    new LibroOptimizador("Fahrenheit 451", "Ray Bradbury", "Distopía", 192, 5.1, 9, 4.6, true),
                    // 71
                    new LibroOptimizador("Un mundo feliz", "Aldous Huxley", "Distopía", 288, 7.7, 9, 4.5, true),
                    // 72
                    new LibroOptimizador("Nosotros", "Yevgueni Zamiatin", "Distopía", 256, 6.8, 7, 4.2, true),
                    // 73
                    new LibroOptimizador("La naranja mecánica", "Anthony Burgess", "Distopía", 208, 5.5, 8, 4.3, true),
                    // 74
                    new LibroOptimizador("El cuento de la criada", "Margaret Atwood", "Distopía", 352, 9.4, 9, 4.5, true),
                    // 75
                    new LibroOptimizador("La carretera", "Cormac McCarthy", "Postapocalíptico", 304, 8.1, 8, 4.4, true),
                    // 76
                    new LibroOptimizador("Guerra mundial Z", "Max Brooks", "Zombies", 352, 9.4, 7, 4.0, true),
                    // 77
                    new LibroOptimizador("Yo soy leyenda", "Richard Matheson", "Vampiros", 160, 4.3, 8, 4.2, true),
                    // 78
                    new LibroOptimizador("Drácula", "Bram Stoker", "Terror", 512, 13.6, 9, 4.5, true),
                    // 79
                    new LibroOptimizador("Frankenstein", "Mary Shelley", "Terror gótico", 288, 7.7, 9, 4.6, true),
                    // 80
                    new LibroOptimizador("El resplandor", "Stephen King", "Terror", 688, 18.3, 9, 4.6, true),
                    // 81
                    new LibroOptimizador("It", "Stephen King", "Terror", 1376, 36.7, 9, 4.5, true),
                    // 82
                    new LibroOptimizador("Carrie", "Stephen King", "Terror", 272, 7.2, 8, 4.2, true),
                    // 83
                    new LibroOptimizador("Misterio", "Peter Straub", "Terror", 672, 17.9, 7, 3.9, true),
                    // 84
                    new LibroOptimizador("El exorcista", "William Peter Blatty", "Terror", 400, 10.6, 8, 4.3, true),
                    // 85
                    new LibroOptimizador("Los pilares de la Tierra", "Ken Follett", "Histórico", 1024, 27.3, 9, 4.7, true),
                    // 86
                    new LibroOptimizador("Un mundo sin fin", "Ken Follett", "Histórico", 1024, 27.3, 8, 4.5, true),
                    // 87
                    new LibroOptimizador("La catedral del mar", "Ildefonso Falcones", "Histórico", 688, 18.3, 8, 4.4, true),
                    // 88
                    new LibroOptimizador("Los reyes malditos", "Maurice Druon", "Histórico", 640, 17.0, 7, 4.3, true),
                    // 89
                    new LibroOptimizador("El médico", "Noah Gordon", "Histórico", 720, 19.2, 8, 4.5, true),
                    // 90
                    new LibroOptimizador("Sinuhé, el egipcio", "Mika Waltari", "Histórico", 608, 16.2, 8, 4.4, true),
                    // 91
                    new LibroOptimizador("Yo, Claudio", "Robert Graves", "Histórico", 576, 15.3, 8, 4.5, true),
                    // 92
                    new LibroOptimizador("Claudio el dios y su esposa Mesalina", "Robert Graves", "Histórico", 496, 13.2, 7, 4.3, true),
                    // 93
                    new LibroOptimizador("Memorias de Adriano", "Marguerite Yourcenar", "Histórico", 336, 8.9, 9, 4.7, true),
                    // 94
                    new LibroOptimizador("La caída de los gigantes", "Ken Follett", "Histórico", 1008, 26.9, 8, 4.4, true),
                    // 95
                    new LibroOptimizador("El invierno del mundo", "Ken Follett", "Histórico", 944, 25.2, 8, 4.4, true),
                    // 96
                    new LibroOptimizador("El umbral de la eternidad", "Ken Follett", "Histórico", 1072, 28.6, 8, 4.3, true),
                    // 97
                    new LibroOptimizador("Los miserables", "Víctor Hugo", "Clásico", 1488, 39.7, 9, 4.8, true),
                    // 98
                    new LibroOptimizador("El conde de Montecristo", "Alexandre Dumas", "Aventura", 1312, 35.0, 9, 4.8, true),
                    // 99
                    new LibroOptimizador("Los tres mosqueteros", "Alexandre Dumas", "Aventura", 768, 20.5, 8, 4.5, true),
                    // 100
                    new LibroOptimizador("La isla del tesoro", "Robert Louis Stevenson", "Aventura", 320, 8.5, 9, 4.6, true),
                    // 101
                    new LibroOptimizador("Las aventuras de Tom Sawyer", "Mark Twain", "Aventura", 288, 7.7, 8, 4.4, true),
                    // 102
                    new LibroOptimizador("Las aventuras de Huckleberry Finn", "Mark Twain", "Aventura", 368, 9.8, 8, 4.5, true),
                    // 103
                    new LibroOptimizador("Veinte mil leguas de viaje submarino", "Julio Verne", "CF", 480, 12.8, 9, 4.6, true),
                    // 104
                    new LibroOptimizador("La vuelta al mundo en 80 días", "Julio Verne", "Aventura", 288, 7.7, 9, 4.5, true),
                    // 105
                    new LibroOptimizador("Viaje al centro de la Tierra", "Julio Verne", "CF", 352, 9.4, 8, 4.4, true),
                    // 106
                    new LibroOptimizador("La máquina del tiempo", "H.G. Wells", "CF", 144, 3.8, 8, 4.4, true),
                    // 107
                    new LibroOptimizador("El hombre invisible", "H.G. Wells", "CF", 208, 5.5, 8, 4.3, true),
                    // 108
                    new LibroOptimizador("La guerra de los mundos", "H.G. Wells", "CF", 256, 6.8, 9, 4.5, true),
                    // 109
                    new LibroOptimizador("Robinson Crusoe", "Daniel Defoe", "Aventura", 352, 9.4, 9, 4.4, true),
                    // 110
                    new LibroOptimizador("Los viajes de Gulliver", "Jonathan Swift", "Sátira", 336, 8.9, 8, 4.3, true),
                    // 111
                    new LibroOptimizador("Cándido", "Voltaire", "Filosófico", 144, 3.8, 8, 4.4, true),
                    // 112
                    new LibroOptimizador("El extranjero", "Albert Camus", "Existencialista", 160, 4.3, 9, 4.5, true),
                    // 113
                    new LibroOptimizador("La peste", "Albert Camus", "Novela filosófica", 320, 8.5, 8, 4.5, true),
                    // 114
                    new LibroOptimizador("La náusea", "Jean-Paul Sartre", "Filosofía", 256, 6.8, 7, 4.2, true),
                    // 115
                    new LibroOptimizador("El mito de Sísifo", "Albert Camus", "Ensayo", 176, 4.7, 8, 4.4, true),
                    // 116
                    new LibroOptimizador("Así habló Zaratustra", "Friedrich Nietzsche", "Filosofía", 384, 10.2, 8, 4.3, true),
                    // 117
                    new LibroOptimizador("El arte de la guerra", "Sun Tzu", "Estrategia", 112, 3.0, 9, 4.6, true),
                    // 118
                    new LibroOptimizador("El príncipe", "Nicolás Maquiavelo", "Política", 160, 4.3, 9, 4.5, true),
                    // 119
                    new LibroOptimizador("La república", "Platón", "Filosofía", 416, 11.1, 9, 4.6, true),
                    // 120
                    new LibroOptimizador("Ética a Nicómaco", "Aristóteles", "Filosofía", 336, 8.9, 8, 4.4, true),
                    // 121
                    new LibroOptimizador("Meditaciones", "Marco Aurelio", "Filosofía estoica", 176, 4.7, 9, 4.7, true),
                    // 122
                    new LibroOptimizador("Cartas a Lucilio", "Séneca", "Filosofía", 320, 8.5, 8, 4.5, true),
                    // 123
                    new LibroOptimizador("La sociedad del cansancio", "Byung-Chul Han", "Filosofía", 128, 3.4, 8, 4.3, true),
                    // 124
                    new LibroOptimizador("La resistencia", "Ernesto Sábato", "Ensayo", 176, 4.7, 7, 4.2, true),
                    // 125
                    new LibroOptimizador("Sobre la brevedad de la vida", "Séneca", "Filosofía", 112, 3.0, 8, 4.5, true),
                    // 126
                    new LibroOptimizador("El hombre en busca de sentido", "Viktor Frankl", "Psicología", 176, 4.7, 9, 4.8, true),
                    // 127
                    new LibroOptimizador("El poder de los hábitos", "Charles Duhigg", "Autoayuda", 400, 10.6, 8, 4.4, true),
                    // 128
                    new LibroOptimizador("Hábitos atómicos", "James Clear", "Autoayuda", 320, 8.5, 9, 4.7, true),
                    // 129
                    new LibroOptimizador("Mindset", "Carol Dweck", "Psicología", 288, 7.7, 8, 4.5, true),
                    // 130
                    new LibroOptimizador("Grit", "Angela Duckworth", "Psicología", 336, 8.9, 8, 4.4, true),
                    // 131
                    new LibroOptimizador("El sutil arte de que te importe un carajo", "Mark Manson", "Autoayuda", 224, 6.0, 9, 4.3, true),
                    // 132
                    new LibroOptimizador("Todo está jodido", "Mark Manson", "Autoayuda", 256, 6.8, 8, 4.2, true),
                    // 133
                    new LibroOptimizador("El monje y el filósofo", "Jean-François Revel", "Diálogo", 336, 8.9, 7, 4.3, true),
                    // 134
                    new LibroOptimizador("Siddhartha", "Hermann Hesse", "Novela espiritual", 176, 4.7, 9, 4.6, true),
                    // 135
                    new LibroOptimizador("El lobo estepario", "Hermann Hesse", "Novela", 256, 6.8, 8, 4.4, true),
                    // 136
                    new LibroOptimizador("Demian", "Hermann Hesse", "Bildungsroman", 176, 4.7, 8, 4.4, true),
                    // 137
                    new LibroOptimizador("Bajo la rueda", "Hermann Hesse", "Novela", 208, 5.5, 7, 4.2, true),
                    // 138
                    new LibroOptimizador("Narciso y Goldmundo", "Hermann Hesse", "Novela", 336, 8.9, 8, 4.5, true),
                    // 139
                    new LibroOptimizador("El juego de los abalorios", "Hermann Hesse", "Utopía", 592, 15.8, 7, 4.4, true),
                    // 140
                    new LibroOptimizador("La insoportable levedad del ser", "Milan Kundera", "Novela", 384, 10.2, 8, 4.4, true),
                    // 141
                    new LibroOptimizador("La broma", "Milan Kundera", "Novela", 320, 8.5, 7, 4.2, true),
                    // 142
                    new LibroOptimizador("El libro de los abrazos", "Eduardo Galeano", "Poético", 288, 7.7, 8, 4.5, true),
                    // 143
                    new LibroOptimizador("Las venas abiertas de América Latina", "Eduardo Galeano", "Ensayo", 384, 10.2, 9, 4.4, true),
                    // 144
                    new LibroOptimizador("Memoria del fuego", "Eduardo Galeano", "Historia", 640, 17.0, 8, 4.5, true),
                    // 145
                    new LibroOptimizador("El laberinto de la soledad", "Octavio Paz", "Ensayo", 256, 6.8, 8, 4.6, true),
                    // 146
                    new LibroOptimizador("La región más transparente", "Carlos Fuentes", "Novela", 512, 13.6, 7, 4.3, true),
                    // 147
                    new LibroOptimizador("Aura", "Carlos Fuentes", "Novela corta", 144, 3.8, 8, 4.4, true),
                    // 148
                    new LibroOptimizador("La muerte de Artemio Cruz", "Carlos Fuentes", "Novela", 320, 8.5, 8, 4.4, true),
                    // 149
                    new LibroOptimizador("Los pasos perdidos", "Alejo Carpentier", "Realismo mágico", 384, 10.2, 7, 4.3, true),
                    // 150
                    new LibroOptimizador("El reino de este mundo", "Alejo Carpentier", "Novela", 176, 4.7, 7, 4.2, true),
                    // 151
                    new LibroOptimizador("La fiesta del Chivo", "Mario Vargas Llosa", "Novela política", 592, 15.8, 8, 4.5, true),
                    // 152
                    new LibroOptimizador("Conversación en La Catedral", "Mario Vargas Llosa", "Novela", 672, 17.9, 8, 4.5, true),
                    // 153
                    new LibroOptimizador("La ciudad y los perros", "Mario Vargas Llosa", "Novela", 512, 13.6, 9, 4.6, true),
                    // 154
                    new LibroOptimizador("Pantaleón y las visitadoras", "Mario Vargas Llosa", "Novela", 304, 8.1, 8, 4.3, true),
                    // 155
                    new LibroOptimizador("La tía Julia y el escribidor", "Mario Vargas Llosa", "Novela", 448, 11.9, 8, 4.4, true),
                    // 156
                    new LibroOptimizador("El otoño del patriarca", "Gabriel García Márquez", "Novela", 288, 7.7, 8, 4.4, true),
                    // 157
                    new LibroOptimizador("Crónica de una muerte anunciada", "Gabriel García Márquez", "Novela", 160, 4.3, 9, 4.5, true),
                    // 158
                    new LibroOptimizador("Del amor y otros demonios", "Gabriel García Márquez", "Novela", 176, 4.7, 8, 4.3, true),
                    // 159
                    new LibroOptimizador("Vivir para contarla", "Gabriel García Márquez", "Autobiografía", 592, 15.8, 8, 4.6, true),
                    // 160
                    new LibroOptimizador("Noticia de un secuestro", "Gabriel García Márquez", "Crónica", 336, 8.9, 7, 4.3, true),
                    // 161
                    new LibroOptimizador("La columna de hierro", "Taylor Caldwell", "Histórico", 704, 18.8, 7, 4.1, true),
                    // 162
                    new LibroOptimizador("Azteca", "Gary Jennings", "Histórico", 768, 20.5, 8, 4.4, true),
                    // 163
                    new LibroOptimizador("El viajero", "Gary Jennings", "Histórico", 864, 23.0, 8, 4.3, true),
                    // 164
                    new LibroOptimizador("Shogun", "James Clavell", "Histórico", 1152, 30.7, 9, 4.7, true),
                    // 165
                    new LibroOptimizador("El clan del oso cavernario", "Jean M. Auel", "Prehistoria", 640, 17.0, 8, 4.3, true),
                    // 166
                    new LibroOptimizador("El valle de los caballos", "Jean M. Auel", "Prehistoria", 624, 16.6, 8, 4.3, true),
                    // 167
                    new LibroOptimizador("Los cazadores de mamuts", "Jean M. Auel", "Prehistoria", 688, 18.3, 7, 4.2, true),
                    // 168
                    new LibroOptimizador("El honor de la tribu", "Jean M. Auel", "Prehistoria", 624, 16.6, 7, 4.2, true),
                    // 169
                    new LibroOptimizador("El país de las cuevas pintadas", "Jean M. Auel", "Prehistoria", 768, 20.5, 7, 4.1, true),
                    // 170
                    new LibroOptimizador("La Tierra de las cuevas pintadas", "Jean M. Auel", "Prehistoria", 720, 19.2, 7, 4.1, true),
                    // 171
                    new LibroOptimizador("El círculo de los mentirosos", "Jean-Christophe Grangé", "Thriller", 544, 14.5, 7, 4.0, true),
                    // 172
                    new LibroOptimizador("Los miserables (cómic)", "Varios", "Gráfica", 144, 3.8, 6, 3.8, true),
                    // 173
                    new LibroOptimizador("Maus", "Art Spiegelman", "Novela gráfica", 296, 7.9, 9, 4.7, true),
                    // 174
                    new LibroOptimizador("Persépolis", "Marjane Satrapi", "Novela gráfica", 344, 9.2, 8, 4.5, true),
                    // 175
                    new LibroOptimizador("Watchmen", "Alan Moore", "Cómic", 416, 11.1, 9, 4.6, true),
                    // 176
                    new LibroOptimizador("V de Vendetta", "Alan Moore", "Cómic", 288, 7.7, 8, 4.5, true),
                    // 177
                    new LibroOptimizador("El contrato social", "Jean-Jacques Rousseau", "Filosofía", 208, 5.5, 8, 4.3, true),
                    // 178
                    new LibroOptimizador("El origen de las especies", "Charles Darwin", "Ciencia", 576, 15.3, 9, 4.6, true),
                    // 179
                    new LibroOptimizador("El capital", "Karl Marx", "Economía", 1152, 30.7, 8, 4.2, true),
                    // 180
                    new LibroOptimizador("La interpretación de los sueños", "Sigmund Freud", "Psicología", 704, 18.8, 8, 4.3, true),
                    // 181
                    new LibroOptimizador("El malestar en la cultura", "Sigmund Freud", "Psicología", 160, 4.3, 8, 4.4, true),
                    // 182
                    new LibroOptimizador("Psicopatología de la vida cotidiana", "Sigmund Freud", "Psicología", 384, 10.2, 7, 4.2, true),
                    // 183
                    new LibroOptimizador("Introducción al psicoanálisis", "Sigmund Freud", "Psicología", 512, 13.6, 8, 4.3, true),
                    // 184
                    new LibroOptimizador("El yo y el ello", "Sigmund Freud", "Psicología", 128, 3.4, 7, 4.2, true),
                    // 185
                    new LibroOptimizador("Más allá del principio del placer", "Sigmund Freud", "Psicología", 112, 3.0, 7, 4.1, true),
                    // 186
                    new LibroOptimizador("Totem y tabú", "Sigmund Freud", "Antropología", 208, 5.5, 7, 4.2, true),
                    // 187
                    new LibroOptimizador("El futuro de una ilusión", "Sigmund Freud", "Filosofía", 112, 3.0, 7, 4.1, true),
                    // 188
                    new LibroOptimizador("Duelo y melancolía", "Sigmund Freud", "Psicología", 80, 2.1, 7, 4.2, true),
                    // 189
                    new LibroOptimizador("Inhibición, síntoma y angustia", "Sigmund Freud", "Psicología", 128, 3.4, 7, 4.1, true),
                    // 190
                    new LibroOptimizador("El hombre del lobo", "Sigmund Freud", "Caso clínico", 144, 3.8, 6, 4.0, true),
                    // 191
                    new LibroOptimizador("El chiste y su relación con lo inconsciente", "Sigmund Freud", "Psicología", 320, 8.5, 7, 4.2, true),
                    // 192
                    new LibroOptimizador("Tres ensayos sobre teoría sexual", "Sigmund Freud", "Psicología", 176, 4.7, 7, 4.3, true),
                    // 193
                    new LibroOptimizador("La última pregunta", "Isaac Asimov", "Relato", 32, 0.8, 9, 4.8, true),
                    // 194
                    new LibroOptimizador("La biblioteca de Babel", "Jorge Luis Borges", "Cuento", 48, 1.2, 9, 4.9, true),
                    // 195
                    new LibroOptimizador("El aleph", "Jorge Luis Borges", "Cuento", 176, 4.7, 9, 4.8, true),
                    // 196
                    new LibroOptimizador("El Sur", "Jorge Luis Borges", "Cuento", 32, 0.8, 8, 4.7, true),
                    // 197
                    new LibroOptimizador("Funes el memorioso", "Jorge Luis Borges", "Cuento", 32, 0.8, 8, 4.7, true),
                    // 198
                    new LibroOptimizador("La muerte y la brújula", "Jorge Luis Borges", "Cuento", 48, 1.2, 8, 4.6, true),
                    // 199
                    new LibroOptimizador("El jardín de senderos que se bifurcan", "Jorge Luis Borges", "Cuento", 48, 1.2, 9, 4.8, true),
                    // 200
                    new LibroOptimizador("El oro de los tigres", "Jorge Luis Borges", "Poesía", 176, 4.7, 7, 4.5, true)
            );

            libroRepositoryOptimizador.saveAll(libros);
            System.out.println("✔ Se han cargado " + libros.size() + " libros reales en la tabla 'libros_optimizador'.");
        }
    }
}