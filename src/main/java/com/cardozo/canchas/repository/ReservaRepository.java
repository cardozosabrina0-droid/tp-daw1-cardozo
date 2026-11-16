package com.cardozo.canchas.repository;

import com.cardozo.canchas.entity.Reserva;
import com.cardozo.canchas.entity.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByCanchaIdAndEstadoNotAndInicioLessThan(
            Long canchaId,
            EstadoReserva estado,
            LocalDateTime fin
    );

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByCanchaId(Long canchaId);
}
