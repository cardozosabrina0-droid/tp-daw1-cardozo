
package com.cardozo.canchas.service;

import com.cardozo.canchas.entity.Cancha;
import com.cardozo.canchas.repository.CanchaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CanchaService {

    private final CanchaRepository repository;

    public CanchaService(CanchaRepository repository) {
        this.repository = repository;
    }

    public List<Cancha> listar() {
        return repository.findAll();
    }

    public Cancha buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cancha no encontrada"));
    }

    public Cancha crear(Cancha cancha) {
        cancha.setId(null);
        return repository.save(cancha);
    }

    public Cancha actualizar(Long id, Cancha datos) {
        Cancha cancha = buscarPorId(id);

        cancha.setNombre(datos.getNombre());
        cancha.setDeporte(datos.getDeporte());
        cancha.setPrecioHora(datos.getPrecioHora());

        return repository.save(cancha);
    }

    public void eliminar(Long id) {
        Cancha cancha = buscarPorId(id);
        repository.delete(cancha);
    }
}
