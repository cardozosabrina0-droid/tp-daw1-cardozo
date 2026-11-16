package com.cardozo.canchas.controller;

import com.cardozo.canchas.entity.Cliente;
import com.cardozo.canchas.entity.Reserva;
import com.cardozo.canchas.service.ClienteService;
import com.cardozo.canchas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;
    private final ReservaService reservaService;

    public ClienteController(ClienteService service,
                             ReservaService reservaService) {
        this.service = service;
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(
            @Valid @RequestBody Cliente cliente) {

        Cliente creado = service.crear(cliente);

        return ResponseEntity
                .created(URI.create("/api/clientes/" + creado.getId()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Cliente cliente) {

        return service.actualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservas")
    public List<Reserva> reservasPorCliente(@PathVariable Long id) {
        return reservaService.reservasPorCliente(id);
    }
}
