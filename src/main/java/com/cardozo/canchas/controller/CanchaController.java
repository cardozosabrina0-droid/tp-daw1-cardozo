package com.cardozo.canchas.controller;

import com.cardozo.canchas.entity.Cancha;
import com.cardozo.canchas.entity.Reserva;
import com.cardozo.canchas.service.CanchaService;
import com.cardozo.canchas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/canchas")
public class CanchaController {

    private final CanchaService service;
    private final ReservaService reservaService;

    public CanchaController(CanchaService service,
                            ReservaService reservaService) {
        this.service = service;
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Cancha> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Cancha buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Cancha> crear(
            @Valid @RequestBody Cancha cancha) {

        Cancha creada = service.crear(cancha);

        return ResponseEntity
                .created(URI.create("/api/canchas/" + creada.getId()))
                .body(creada);
    }

    @PutMapping("/{id}")
    public Cancha actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Cancha cancha) {

        return service.actualizar(id, cancha);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservas")
    public List<Reserva> reservasPorFecha(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate fecha) {

        List<Reserva> reservas = reservaService.reservasPorCancha(id);

        if (fecha == null) {
            return reservas;
        }

        return reservas.stream()
                .filter(r -> r.getInicio()
                        .toLocalDate().equals(fecha))
                .toList();
    }
}

