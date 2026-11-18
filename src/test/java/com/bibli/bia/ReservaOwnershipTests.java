package com.bibli.bia;

import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.Model.Usuario;
import com.bibli.bia.repository.ReservaRepository;
import com.bibli.bia.service.LibroFisicoService;
import com.bibli.bia.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaOwnershipTests {
    @Test
    void cancellationRejectsAnotherReadersReservationBeforeChangingStock() {
        var repository = mock(ReservaRepository.class);
        var inventory = mock(LibroFisicoService.class);
        var service = new ReservaService();
        ReflectionTestUtils.setField(service, "reservaRepository", repository);
        ReflectionTestUtils.setField(service, "libroFisicoService", inventory);
        var owner = new Usuario();
        owner.setUsername("owner");
        var reservation = new ReservaModel();
        reservation.setUsuario(owner);
        when(repository.findByIdForUpdate("reservation")).thenReturn(Optional.of(reservation));

        assertThrows(AccessDeniedException.class, () -> service.cancelarReserva("reservation", "other", false));
        assertEquals(ReservaModel.EstadoReserva.ACTIVA, reservation.getEstado());
        verify(repository, never()).save(any());
        verifyNoInteractions(inventory);
    }

    @Test
    void ownerCanCancelTheirOwnReservation() {
        var repository = mock(ReservaRepository.class);
        var service = new ReservaService();
        ReflectionTestUtils.setField(service, "reservaRepository", repository);
        var owner = new Usuario();
        owner.setUsername("owner");
        var reservation = new ReservaModel();
        reservation.setUsuario(owner);
        when(repository.findByIdForUpdate("reservation")).thenReturn(Optional.of(reservation));
        service.cancelarReserva("reservation", "owner", false);
        assertEquals(ReservaModel.EstadoReserva.CANCELADA, reservation.getEstado());
        verify(repository).save(reservation);
    }
}
