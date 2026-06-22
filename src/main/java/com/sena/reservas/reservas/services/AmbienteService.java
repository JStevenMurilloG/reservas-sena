package com.sena.reservas.reservas.services;

import com.sena.reservas.reservas.entities.Ambiente;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.enums.TipoEstado;
import com.sena.reservas.reservas.exception.RecursoNoEncontradoException;
import com.sena.reservas.reservas.repositories.AmbienteRepository;
import com.sena.reservas.reservas.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AmbienteService {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    public AmbienteService(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Ambiente> obtenerTodos() {
        return this.ambienteRepository.findAll();
    }

    public List<Ambiente> obtenerActivos() {
        return this.ambienteRepository.findByActivoTrue();
    }

    public Optional<Ambiente> porId(Long id) {
        return this.ambienteRepository.findById(id);
    }

    public Ambiente crear(Ambiente ambiente) {
        return this.ambienteRepository.save(ambiente);
    }

    public Ambiente actualizar(Ambiente ambiente) {
        Optional<Ambiente> ambienteFound = this.porId(ambiente.getId());
        if (ambienteFound.isEmpty()) {
            return null;
        }
        return this.ambienteRepository.save(ambiente);
    }

    public Ambiente eliminar(Long id) {
        Optional<Ambiente> ambienteFound = this.porId(id);
        if (ambienteFound.isEmpty()) {
            return null;
        }
        this.ambienteRepository.delete(ambienteFound.get());
        return ambienteFound.get();
    }

    public List<Reserva> obtenerReservasPorFecha(Long ambienteId, LocalDate fecha) {
        Optional<Ambiente> optionalAmbiente = ambienteRepository.findById(ambienteId);
        if (optionalAmbiente.isEmpty()) {
            throw new RecursoNoEncontradoException("Ambiente no encontrado");
        }

        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);

        return reservaRepository.findReservasActivasPorAmbienteYFecha(
                ambienteId, inicio, fin, TipoEstado.ACTIVO);
    }

    public List<Ambiente> obtenerDisponibles(LocalDateTime inicio, LocalDateTime fin) {
        List<Ambiente> activos = ambienteRepository.findByActivoTrue();

        List<Reserva> reservasEnRango = reservaRepository.findReservasActivasEnRango(
                inicio, fin, TipoEstado.ACTIVO);

        Set<Long> idsOcupados = new HashSet<>();
        for (Reserva reserva : reservasEnRango) {
            idsOcupados.add(reserva.getAmbiente().getId());
        }

        List<Ambiente> disponibles = new ArrayList<>();
        for (Ambiente ambiente : activos) {
            boolean estaOcupado = idsOcupados.contains(ambiente.getId());
            if (!estaOcupado) {
                disponibles.add(ambiente);
            }
        }

        return disponibles;
    }
}
