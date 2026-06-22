package com.sena.reservas.reservas.controllers;

import com.sena.reservas.reservas.dto.AmbienteDto;
import com.sena.reservas.reservas.entities.Ambiente;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.exception.RecursoNoEncontradoException;
import com.sena.reservas.reservas.services.AmbienteService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    private AmbienteService service;

    public AmbienteController(AmbienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ambiente> get() {
        return this.service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambiente> getById(@PathVariable("id") Long id) {
        Optional<Ambiente> optionalAmbiente = this.service.porId(id);
        if (optionalAmbiente.isEmpty()) {
            throw new RecursoNoEncontradoException("Ambiente no encontrado");
        }
        Ambiente ambiente = optionalAmbiente.get();
        return ResponseEntity.ok(ambiente);
    }

    @PostMapping
    public Ambiente create(@Valid @RequestBody AmbienteDto body) {
        Ambiente ambiente = new Ambiente();
        ambiente.setNombre(body.getNombre());
        ambiente.setTipo(body.getTipo());
        ambiente.setCapacidad(body.getCapacidad());
        ambiente.setActivo(body.isActivo());
        return this.service.crear(ambiente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ambiente> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody AmbienteDto body) {

        Ambiente ambiente = new Ambiente();
        ambiente.setId(id);
        ambiente.setNombre(body.getNombre());
        ambiente.setTipo(body.getTipo());
        ambiente.setCapacidad(body.getCapacidad());
        ambiente.setActivo(body.isActivo());

        Ambiente ambienteUpdated = this.service.actualizar(ambiente);
        if (ambienteUpdated == null) {
            throw new RecursoNoEncontradoException("Ambiente no encontrado");
        }
        return ResponseEntity.ok(ambienteUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ambiente> delete(@PathVariable("id") Long id) {
        Ambiente ambienteDeleted = this.service.eliminar(id);
        if (ambienteDeleted == null) {
            throw new RecursoNoEncontradoException("Ambiente no encontrado");
        }
        return ResponseEntity.ok(ambienteDeleted);
    }

    @GetMapping("/{id}/reservas")
    public List<Reserva> getReservasPorFecha(
            @PathVariable("id") Long id,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return this.service.obtenerReservasPorFecha(id, fecha);
    }

    @GetMapping("/disponibles")
    public List<Ambiente> getDisponibles(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return this.service.obtenerDisponibles(inicio, fin);
    }
}
