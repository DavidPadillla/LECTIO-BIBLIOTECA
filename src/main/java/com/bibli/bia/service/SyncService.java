package com.bibli.bia.service;

import com.bibli.bia.Model.*;
import com.bibli.bia.repository.*;
import com.bibli.bia.postgres.entity.*;
import com.bibli.bia.postgres.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SyncService {

    private static final int LIMITE_REGISTROS = 0;

    private static LocalDateTime ultimaSincronizacion = null;
    private static int totalSincronizados = 0;

    private final Map<String, String> cacheLibroId = new HashMap<>();

    @Autowired
    private LibroRepository libroMongoRepo;
    @Autowired
    private LibroFisicoRepository libroFisicoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private ReservaRepository reservaRepo;
    @Autowired
    private Resenarepository resenaRepo;
    @Autowired
    private ProgresoLecturaRepository progresoRepo;
    @Autowired
    private MultaRepository multaRepo;
    @Autowired
    private RespuestaDashboardRepository dashboardRepo;

    @Autowired
    private LibroPostgresRepository libroPostgresRepo;
    @Autowired
    private LibroFisicoPostgresRepository libroFisicoPostgresRepo;
    @Autowired
    private UsuarioPostgresRepository usuarioPostgresRepo;
    @Autowired
    private ReservaPostgresRepository reservaPostgresRepo;
    @Autowired
    private ResenaPostgresRepository resenaPostgresRepo;
    @Autowired
    private ProgresoLecturaPostgresRepository progresoPostgresRepo;
    @Autowired
    private MultaPostgresRepository multaPostgresRepo;
    @Autowired
    private RespuestaDashboardPostgresRepository dashboardPostgresRepo;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void sincronizarTodo() {
        System.out.println("Iniciando sincronizacion MongoDB → PostgreSQL...");
        System.out.println("Limitando a " + LIMITE_REGISTROS + " registros por tabla");

        sincronizarLibros();
        sincronizarLibrosFisicos();
        sincronizarUsuarios();
        sincronizarReservas();
        sincronizarResenasBatch();
        sincronizarProgresoLecturaBatch();
        sincronizarMultasBatch();
        sincronizarDashboardBatch();

        ultimaSincronizacion = LocalDateTime.now();

        System.out.println("Sincronizacion completada!");
        System.out.println("Total de registros sincronizados: " + totalSincronizados);
    }

    private void sincronizarLibros() {
        List<LibroPostgres> list = new ArrayList<>();
        int count = 0;

        for (LibroModel m : libroMongoRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = false;
            for (LibroPostgres l : libroPostgresRepo.findAll()) {
                if (l.getTitulo() != null && l.getTitulo().equals(m.getTitulo())) {
                    existe = true;
                    cacheLibroId.put(m.getTitulo(), l.getId());
                    break;
                }
            }

            if (!existe) {
                LibroPostgres p = new LibroPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setTitulo(m.getTitulo());
                p.setUrl(m.getUrl());
                p.setAutor(m.getAutor());
                p.setDescripcion(m.getDescripcion());
                p.setCategoria(m.getCategoria());
                list.add(p);
                cacheLibroId.put(m.getTitulo(), p.getId());
                count++;
            }
        }

        if (!list.isEmpty()) {
            libroPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Libros sincronizados: " + list.size());
    }

    private void sincronizarLibrosFisicos() {
        List<LibroFisicoPostgres> list = new ArrayList<>();
        int count = 0;

        for (LibroFisicoModel m : libroFisicoRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = false;
            for (LibroFisicoPostgres l : libroFisicoPostgresRepo.findAll()) {
                if (l.getTitulo() != null && l.getTitulo().equals(m.getTitulo())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                LibroFisicoPostgres p = new LibroFisicoPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setIdLibro(cacheLibroId.get(m.getTitulo()));
                p.setTitulo(m.getTitulo());
                p.setAutor(m.getAutor());
                p.setDescripcion(m.getDescripcion());
                p.setCategoria(m.getCategoria());
                p.setStock(m.getStock());
                p.setReservado(m.getReservado());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            libroFisicoPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Libros fisicos sincronizados: " + list.size());
    }

    private void sincronizarUsuarios() {
        List<UsuarioPostgres> list = new ArrayList<>();
        int count = 0;

        for (Usuario m : usuarioRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = false;
            for (UsuarioPostgres u : usuarioPostgresRepo.findAll()) {
                if (u.getUsername() != null && u.getUsername().equals(m.getUsername())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                UsuarioPostgres p = new UsuarioPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setUsername(m.getUsername());
                p.setPassword(m.getPassword());
                try {
                    p.setRoles(objectMapper.writeValueAsString(m.getRoles()));
                } catch (Exception e) {
                    p.setRoles("[]");
                }
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            usuarioPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Usuarios sincronizados: " + list.size());
    }

    private void sincronizarReservas() {
        List<ReservaPostgres> list = new ArrayList<>();
        int count = 0;

        for (ReservaModel m : reservaRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            String idLibro = cacheLibroId.get(m.getLibro());

            boolean existe = false;
            for (ReservaPostgres r : reservaPostgresRepo.findAll()) {
                if (r.getIdUsuario() != null && r.getIdUsuario().equals(m.getIdUsuario())
                        && r.getLibro() != null && r.getLibro().equals(m.getLibro())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ReservaPostgres p = new ReservaPostgres(
                        m.getIdUsuario(),
                        m.getNombreCompleto(),
                        m.getCorreo(),
                        m.getCategoria(),
                        m.getLibro(),
                        m.getFecha()
                );
                if (idLibro != null) {
                    p.setIdLibro(idLibro);
                }
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            reservaPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Reservas sincronizadas: " + list.size());
    }

    private void sincronizarResenasBatch() {
        List<ResenaPostgres> list = new ArrayList<>();
        int count = 0;

        for (ResenaModel m : resenaRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = false;
            for (ResenaPostgres r : resenaPostgresRepo.findAll()) {
                if (r.getNombre() != null && r.getNombre().equals(m.getNombre())
                        && r.getComentario() != null && r.getComentario().equals(m.getComentario())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ResenaPostgres p = new ResenaPostgres(m.getNombre(), m.getComentario());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            resenaPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Resenas sincronizadas: " + list.size());
    }

    private void sincronizarProgresoLecturaBatch() {
        List<ProgresoLecturaPostgres> list = new ArrayList<>();
        int count = 0;

        for (ProgresoLectura m : progresoRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = false;
            for (ProgresoLecturaPostgres p : progresoPostgresRepo.findAll()) {
                if (p.getUsername() != null && p.getUsername().equals(m.getUsername())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ProgresoLecturaPostgres p = new ProgresoLecturaPostgres(m.getUsername());
                p.setTotalLibrosLeidos(m.getTotalLibrosLeidos());
                p.setUltimaActualizacion(m.getUltimaActualizacion());
                p.setPuntos(m.getPuntos());
                try {
                    p.setLibrosCompletados(objectMapper.writeValueAsString(m.getLibrosCompletados()));
                    p.setCapitulosPorLibro(objectMapper.writeValueAsString(m.getCapitulosPorLibro()));
                } catch (Exception e) {
                    p.setLibrosCompletados("[]");
                    p.setCapitulosPorLibro("{}");
                }
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            progresoPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Progresos lectura sincronizados: " + list.size());
    }

    private void sincronizarMultasBatch() {
        List<MultaPostgres> list = new ArrayList<>();
        int count = 0;

        for (MultaModel m : multaRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            String idLibro = cacheLibroId.get(m.getLibro());

            boolean existe = false;
            for (MultaPostgres mt : multaPostgresRepo.findAll()) {
                if (mt.getIdUsuario() != null && mt.getIdUsuario().equals(m.getIdUsuario())
                        && mt.getLibro() != null && mt.getLibro().equals(m.getLibro())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                MultaPostgres p = new MultaPostgres(
                        m.getIdUsuario(),
                        m.getNombreUsuario(),
                        m.getLibro(),
                        m.getFechaReserva(),
                        m.getFechaDevolucion(),
                        m.getDiasRetraso(),
                        m.getValorMulta()
                );
                if (idLibro != null) {
                    p.setIdLibro(idLibro);
                    p.setLibroTitulo(m.getLibro());
                }
                p.setPagada(m.isPagada());
                p.setFechaPago(m.getFechaPago());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            multaPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Multas sincronizadas: " + list.size());
    }

    private void sincronizarDashboardBatch() {
        List<RespuestaDashboardPostgres> list = new ArrayList<>();
        int count = 0;

        for (RespuestaDashboard m : dashboardRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = false;
            for (RespuestaDashboardPostgres d : dashboardPostgresRepo.findAll()) {
                if (d.getGenero() != null && d.getGenero().equals(m.getGenero())
                        && d.getEdad() != null && d.getEdad().equals(m.getEdad())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                RespuestaDashboardPostgres p = new RespuestaDashboardPostgres(
                        m.getEdad(),
                        m.getGenero(),
                        m.getEducacion(),
                        m.getFrecuencia(),
                        m.getCategoriaFavorita(),
                        m.getFormato(),
                        m.getUso(),
                        m.getLibrosMes(),
                        m.getCalificacion(),
                        m.getRecomendacion(),
                        m.getDispositivos(),
                        m.getUltimoLibro(),
                        m.getMejoras(),
                        m.getRecomendaciones(),
                        m.getClubes(),
                        m.getCompras(),
                        m.getAutoresFavoritos(),
                        m.getBoletines()
                );
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            dashboardPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("Dashboard sincronizado: " + list.size());
    }

    // ==================== MÉTODOS PÚBLICOS PARA SINCRONIZACIÓN EN TIEMPO REAL ====================

    public void sincronizarLibro(LibroModel libro) {
        try {
            boolean existe = false;
            for (LibroPostgres l : libroPostgresRepo.findAll()) {
                if (l.getTitulo() != null && l.getTitulo().equals(libro.getTitulo())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                LibroPostgres p = new LibroPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setTitulo(libro.getTitulo());
                p.setUrl(libro.getUrl());
                p.setAutor(libro.getAutor());
                p.setDescripcion(libro.getDescripcion());
                p.setCategoria(libro.getCategoria());
                libroPostgresRepo.save(p);
                System.out.println("✅ Libro sincronizado a Neon: " + libro.getTitulo());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando libro: " + e.getMessage());
        }
    }

    public void sincronizarUsuario(Usuario usuario) {
        try {
            boolean existe = false;
            for (UsuarioPostgres u : usuarioPostgresRepo.findAll()) {
                if (u.getUsername() != null && u.getUsername().equals(usuario.getUsername())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                UsuarioPostgres p = new UsuarioPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setUsername(usuario.getUsername());
                p.setPassword(usuario.getPassword());
                try {
                    p.setRoles(objectMapper.writeValueAsString(usuario.getRoles()));
                } catch (Exception e) {
                    p.setRoles("[]");
                }
                usuarioPostgresRepo.save(p);
                System.out.println("✅ Usuario sincronizado a Neon: " + usuario.getUsername());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando usuario: " + e.getMessage());
        }
    }

    public void sincronizarResena(ResenaModel resena) {
        try {
            boolean existe = false;
            for (ResenaPostgres r : resenaPostgresRepo.findAll()) {
                if (r.getNombre() != null && r.getNombre().equals(resena.getNombre())
                        && r.getComentario() != null && r.getComentario().equals(resena.getComentario())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ResenaPostgres p = new ResenaPostgres(resena.getNombre(), resena.getComentario());
                resenaPostgresRepo.save(p);
                System.out.println("✅ Reseña sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando reseña: " + e.getMessage());
        }
    }

    public void sincronizarReserva(ReservaModel reserva) {
        try {
            String idLibro = cacheLibroId.get(reserva.getLibro());

            boolean existe = false;
            for (ReservaPostgres r : reservaPostgresRepo.findAll()) {
                if (r.getIdUsuario() != null && r.getIdUsuario().equals(reserva.getIdUsuario())
                        && r.getLibro() != null && r.getLibro().equals(reserva.getLibro())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ReservaPostgres p = new ReservaPostgres(
                        reserva.getIdUsuario(),
                        reserva.getNombreCompleto(),
                        reserva.getCorreo(),
                        reserva.getCategoria(),
                        reserva.getLibro(),
                        reserva.getFecha()
                );
                if (idLibro != null) {
                    p.setIdLibro(idLibro);
                }
                reservaPostgresRepo.save(p);
                System.out.println("✅ Reserva sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando reserva: " + e.getMessage());
        }
    }

    public void sincronizarMulta(MultaModel multa) {
        try {
            String idLibro = cacheLibroId.get(multa.getLibro());

            boolean existe = false;
            for (MultaPostgres mt : multaPostgresRepo.findAll()) {
                if (mt.getIdUsuario() != null && mt.getIdUsuario().equals(multa.getIdUsuario())
                        && mt.getLibro() != null && mt.getLibro().equals(multa.getLibro())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                MultaPostgres p = new MultaPostgres(
                        multa.getIdUsuario(),
                        multa.getNombreUsuario(),
                        multa.getLibro(),
                        multa.getFechaReserva(),
                        multa.getFechaDevolucion(),
                        multa.getDiasRetraso(),
                        multa.getValorMulta()
                );
                if (idLibro != null) {
                    p.setIdLibro(idLibro);
                    p.setLibroTitulo(multa.getLibro());
                }
                p.setPagada(multa.isPagada());
                p.setFechaPago(multa.getFechaPago());
                multaPostgresRepo.save(p);
                System.out.println("✅ Multa sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando multa: " + e.getMessage());
        }
    }

    public void sincronizarProgresoLectura(ProgresoLectura progreso) {
        try {
            boolean existe = false;
            for (ProgresoLecturaPostgres p : progresoPostgresRepo.findAll()) {
                if (p.getUsername() != null && p.getUsername().equals(progreso.getUsername())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ProgresoLecturaPostgres p = new ProgresoLecturaPostgres(progreso.getUsername());
                p.setTotalLibrosLeidos(progreso.getTotalLibrosLeidos());
                p.setUltimaActualizacion(progreso.getUltimaActualizacion());
                p.setPuntos(progreso.getPuntos());
                try {
                    p.setLibrosCompletados(objectMapper.writeValueAsString(progreso.getLibrosCompletados()));
                    p.setCapitulosPorLibro(objectMapper.writeValueAsString(progreso.getCapitulosPorLibro()));
                } catch (Exception e) {
                    p.setLibrosCompletados("[]");
                    p.setCapitulosPorLibro("{}");
                }
                progresoPostgresRepo.save(p);
                System.out.println("✅ Progreso lectura sincronizado a Neon: " + progreso.getUsername());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando progreso lectura: " + e.getMessage());
        }
    }

    public void sincronizarRespuestaDashboard(RespuestaDashboard respuesta) {
        try {
            boolean existe = false;
            for (RespuestaDashboardPostgres d : dashboardPostgresRepo.findAll()) {
                if (d.getGenero() != null && d.getGenero().equals(respuesta.getGenero())
                        && d.getEdad() != null && d.getEdad().equals(respuesta.getEdad())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                RespuestaDashboardPostgres p = new RespuestaDashboardPostgres(
                        respuesta.getEdad(),
                        respuesta.getGenero(),
                        respuesta.getEducacion(),
                        respuesta.getFrecuencia(),
                        respuesta.getCategoriaFavorita(),
                        respuesta.getFormato(),
                        respuesta.getUso(),
                        respuesta.getLibrosMes(),
                        respuesta.getCalificacion(),
                        respuesta.getRecomendacion(),
                        respuesta.getDispositivos(),
                        respuesta.getUltimoLibro(),
                        respuesta.getMejoras(),
                        respuesta.getRecomendaciones(),
                        respuesta.getClubes(),
                        respuesta.getCompras(),
                        respuesta.getAutoresFavoritos(),
                        respuesta.getBoletines()
                );
                dashboardPostgresRepo.save(p);
                System.out.println("✅ Respuesta dashboard sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando respuesta dashboard: " + e.getMessage());
        }
    }

    public void sincronizarLibroFisico(LibroFisicoModel libroFisico) {
        try {
            boolean existe = false;
            for (LibroFisicoPostgres l : libroFisicoPostgresRepo.findAll()) {
                if (l.getTitulo() != null && l.getTitulo().equals(libroFisico.getTitulo())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                LibroFisicoPostgres p = new LibroFisicoPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setIdLibro(cacheLibroId.get(libroFisico.getTitulo()));
                p.setTitulo(libroFisico.getTitulo());
                p.setAutor(libroFisico.getAutor());
                p.setDescripcion(libroFisico.getDescripcion());
                p.setCategoria(libroFisico.getCategoria());
                p.setStock(libroFisico.getStock());
                p.setReservado(libroFisico.getReservado());
                libroFisicoPostgresRepo.save(p);
                System.out.println("✅ Libro físico sincronizado a Neon: " + libroFisico.getTitulo());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando libro físico: " + e.getMessage());
        }
    }

    public static LocalDateTime getUltimaSincronizacion() {
        return ultimaSincronizacion;
    }

    public static int getTotalSincronizados() {
        return totalSincronizados;
    }
}