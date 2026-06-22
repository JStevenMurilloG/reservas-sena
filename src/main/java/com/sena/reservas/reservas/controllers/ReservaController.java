package com.sena.reservas.reservas.controllers;

import com.sena.reservas.reservas.dto.ReservaDto;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.exception.RecursoNoEncontradoException;
import com.sena.reservas.reservas.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reserva> get() {
        return this.service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getById(@PathVariable("id") Long id) {
        Optional<Reserva> optionalReserva = this.service.porId(id);
        if (optionalReserva.isEmpty()) {
            throw new RecursoNoEncontradoException("Reserva no encontrada");
        }
        Reserva reserva = optionalReserva.get();
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public Reserva create(@Valid @RequestBody ReservaDto body) {
        return this.service.crear(body);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable("id") Long id) {
        Reserva reserva = this.service.cancelar(id);
        return ResponseEntity.ok(reserva);
    }
}
