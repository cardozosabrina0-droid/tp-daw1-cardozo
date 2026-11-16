package com.cardozo.canchas.controller;

import com.cardozo.canchas.dto.ReservaRequest;
import com.cardozo.canchas.entity.EstadoReserva;
import com.cardozo.canchas.entity.Reserva;
import com.cardozo.canchas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reserva> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Reserva buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(
            @Valid @RequestBody ReservaRequest datos) {

        Reserva creada = service.crear(datos);

        return ResponseEntity
                .created(URI.create("/api/reservas/" + creada.getId()))
                .body(creada);
    }

    @PatchMapping("/{id}/estado")
    public Reserva cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos) {

        String valor = datos.get("estado");

        if (valor == null || valor.isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "El estado es obligatorio");
        }

        EstadoReserva estado;
        try {
            estado = EstadoReserva.valueOf(valor);
        } catch (IllegalArgumentException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Estado inválido");
        }

        return service.cambiarEstado(id, estado);
    }
}
