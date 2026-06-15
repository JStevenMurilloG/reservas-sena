package com.sena.reservas.reservas.services;

import com.sena.reservas.reservas.dto.ReservaDto;
import com.sena.reservas.reservas.entities.Ambiente;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.enums.TipoEstado;
import com.sena.reservas.reservas.exception.RecursoNoEncontradoException;
import com.sena.reservas.reservas.exception.ReglaNegocioException;
import com.sena.reservas.reservas.repositories.AmbienteRepository;
import com.sena.reservas.reservas.repositories.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AmbienteRepository ambienteRepository;

    public ReservaService(ReservaRepository reservaRepository, AmbienteRepository ambienteRepository) {
        this.reservaRepository = reservaRepository;
        this.ambienteRepository = ambienteRepository;
    }

    public List<Reserva> obtenerTodos() {
        return this.reservaRepository.findAll();
    }

    public Optional<Reserva> porId(Long id) {
        return this.reservaRepository.findById(id);
    }

    public Reserva crear(ReservaDto dto) {
        Optional<Ambiente> optionalAmbiente = ambienteRepository.findById(dto.getAmbienteId());
        if (optionalAmbiente.isEmpty()) {
            throw new RecursoNoEncontradoException("Ambiente no encontrado");
        }
        Ambiente ambiente = optionalAmbiente.get();

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = dto.getFechaHoraInicio();
        LocalDateTime fin = dto.getFechaHoraFin();

        if (inicio.isBefore(ahora)) {
            throw new ReglaNegocioException("No se puede reservar en fechas pasadas", HttpStatus.BAD_REQUEST);
        }

        if (!ambiente.isActivo()) {
            throw new ReglaNegocioException("No se puede reservar un ambiente inactivo", HttpStatus.BAD_REQUEST);
        }

        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();
        long duracionMinutos = Duration.between(inicio, fin).toMinutes();

        if (horaInicio.isBefore(LocalTime.of(6, 0)) || horaFin.isAfter(LocalTime.of(22, 0))) {
            throw new ReglaNegocioException("Las reservas deben estar entre las 6:00 y las 22:00", HttpStatus.BAD_REQUEST);
        }

        if (duracionMinutos < 60 || duracionMinutos > 240) {
            throw new ReglaNegocioException("La reserva debe durar entre 1 y 4 horas", HttpStatus.BAD_REQUEST);
        }

        if (dto.getNumAprendices() > ambiente.getCapacidad()) {
            throw new ReglaNegocioException(
                    "El numero de aprendices (" + dto.getNumAprendices() + ") supera la capacidad del ambiente ("
                            + ambiente.getCapacidad() + ")",
                    HttpStatus.BAD_REQUEST);
        }

        List<Reserva> solapamientos = reservaRepository.buscarSolapamientos(
                dto.getAmbienteId(), inicio, fin, TipoEstado.ACTIVO);
        if (!solapamientos.isEmpty()) {
            throw new ReglaNegocioException(
                    "El ambiente ya tiene una reserva activa en ese horario", HttpStatus.CONFLICT);
        }

        long reservasHoy = reservaRepository.contarActivasPorInstructorYFecha(
                dto.getNombreInstructor(), inicio.toLocalDate(), TipoEstado.ACTIVO);
        if (reservasHoy >= 3) {
            throw new ReglaNegocioException(
                    "El instructor ya tiene 3 reservas activas en este dia", HttpStatus.BAD_REQUEST);
        }

        Reserva reserva = new Reserva();
        reserva.setAmbiente(ambiente);
        reserva.setNombreInstructor(dto.getNombreInstructor());
        reserva.setFechaHoraInicio(inicio);
        reserva.setFechaHoraFin(fin);
        reserva.setNumAprendices(dto.getNumAprendices());
        reserva.setEstado(TipoEstado.ACTIVO);

        return reservaRepository.save(reserva);
    }

    public Reserva cancelar(Long id) {
        Optional<Reserva> optionalReserva = reservaRepository.findById(id);
        if (optionalReserva.isEmpty()) {
            throw new RecursoNoEncontradoException("Reserva no encontrada");
        }
        Reserva reserva = optionalReserva.get();

        if (LocalDateTime.now().plusHours(2).isAfter(reserva.getFechaHoraInicio())) {
            throw new ReglaNegocioException(
                    "Deben faltar al menos 2 horas para cancelar la reserva", HttpStatus.BAD_REQUEST);
        }

        reserva.setEstado(TipoEstado.CANCELADO);
        return reservaRepository.save(reserva);
    }

    public Reserva actualizar(Reserva reserva) {
        Optional<Reserva> reservaFound = this.porId(reserva.getId());
        if (reservaFound.isEmpty()) {
            return null;
        }
        return this.reservaRepository.save(reserva);
    }

    public Reserva eliminar(Long id) {
        Optional<Reserva> reservaFound = this.porId(id);
        if (reservaFound.isEmpty()) {
            return null;
        }
        this.reservaRepository.delete(reservaFound.get());
        return reservaFound.get();
    }
}
