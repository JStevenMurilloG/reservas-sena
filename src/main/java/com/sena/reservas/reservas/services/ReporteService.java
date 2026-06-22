package com.sena.reservas.reservas.services;

import com.sena.reservas.reservas.dto.OcupacionDto;
import com.sena.reservas.reservas.entities.Ambiente;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.enums.TipoEstado;
import com.sena.reservas.reservas.repositories.AmbienteRepository;
import com.sena.reservas.reservas.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    public ReporteService(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<OcupacionDto> calcularOcupacion(LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(LocalTime.MAX);
        long horasLaborables = 16;

        List<Ambiente> ambientes = ambienteRepository.findAll();
        List<OcupacionDto> resultado = new ArrayList<>();

        for (Ambiente ambiente : ambientes) {
            List<Reserva> reservas = reservaRepository.findReservasActivasPorAmbienteYFecha(
                    ambiente.getId(), inicioDia, finDia, TipoEstado.ACTIVO);

            long minutosReservados = 0;
            for (Reserva reserva : reservas) {
                long minutos = ChronoUnit.MINUTES.between(reserva.getFechaHoraInicio(), reserva.getFechaHoraFin());
                minutosReservados = minutosReservados + minutos;
            }

            long horasReservadas = minutosReservados / 60;
            double porcentaje = (double) minutosReservados / (horasLaborables * 60) * 100;
            double porcentajeRedondeado = Math.round(porcentaje * 100.0) / 100.0;

            resultado.add(new OcupacionDto(ambiente.getNombre(), horasReservadas, porcentajeRedondeado));
        }

        return resultado;
    }
}
