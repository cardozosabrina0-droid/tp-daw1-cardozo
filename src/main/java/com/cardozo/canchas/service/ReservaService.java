package com.cardozo.canchas.service;

import com.cardozo.canchas.dto.ReservaRequest;
import com.cardozo.canchas.entity.*;
import com.cardozo.canchas.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservas;
    private final CanchaRepository canchas;
    private final ClienteRepository clientes;

    public ReservaService(
            ReservaRepository reservas,
            CanchaRepository canchas,
            ClienteRepository clientes) {
        this.reservas = reservas;
        this.canchas = canchas;
        this.clientes = clientes;
    }

    public List<Reserva> listar() {
        return reservas.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no encontrada"));
    }

    @Transactional
    public Reserva crear(ReservaRequest datos) {

        Cancha cancha = canchas.findById(datos.canchaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cancha no encontrada"));

        Cliente cliente = clientes.findById(datos.clienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        validarDisponibilidad(
                cancha.getId(), datos.inicio(), datos.horas());

        Reserva reserva = new Reserva();
        reserva.setCancha(cancha);
        reserva.setCliente(cliente);
        reserva.setInicio(datos.inicio());
        reserva.setHoras(datos.horas());
        reserva.setEstado(EstadoReserva.PENDIENTE);

        reserva.setPrecioTotal(
                cancha.getPrecioHora()
                        .multiply(BigDecimal.valueOf(datos.horas())));

        return reservas.save(reserva);
    }

    private void validarDisponibilidad(
            Long canchaId, LocalDateTime inicio, int horas) {

        LocalDateTime fin = inicio.plusHours(horas);

        List<Reserva> existentes =
                reservas.findByCanchaIdAndEstadoNotAndInicioLessThan(
                        canchaId, EstadoReserva.CANCELADA, fin);

        for (Reserva existente : existentes) {
            LocalDateTime finExistente =
                    existente.getInicio().plusHours(existente.getHoras());

            if (finExistente.isAfter(inicio)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "La cancha ya tiene una reserva en ese horario");
            }
        }
    }

    @Transactional
    public Reserva cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva reserva = buscarPorId(id);
        EstadoReserva actual = reserva.getEstado();

        boolean permitido = switch (actual) {
            case PENDIENTE ->
                    nuevoEstado == EstadoReserva.CONFIRMADA ||
                            nuevoEstado == EstadoReserva.CANCELADA;
            case CONFIRMADA ->
                    nuevoEstado == EstadoReserva.FINALIZADA ||
                            nuevoEstado == EstadoReserva.CANCELADA;
            case FINALIZADA, CANCELADA -> false;
        };

        if (!permitido) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transición de estado no permitida");
        }

        reserva.setEstado(nuevoEstado);
        return reservas.save(reserva);
    }

    public List<Reserva> reservasPorCliente(Long clienteId) {
        if (!clientes.existsById(clienteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        return reservas.findByClienteId(clienteId);
    }

    public List<Reserva> reservasPorCancha(Long canchaId) {
        if (!canchas.existsById(canchaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cancha no encontrada");
        }
        return reservas.findByCanchaId(canchaId);
    }
}
