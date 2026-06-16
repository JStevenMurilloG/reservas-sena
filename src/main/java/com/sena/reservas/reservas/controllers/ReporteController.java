package com.sena.reservas.reservas.controllers;

import com.sena.reservas.reservas.dto.OcupacionDto;
import com.sena.reservas.reservas.entities.Ambiente;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.enums.TipoEstado;
import com.sena.reservas.reservas.repositories.AmbienteRepository;
import com.sena.reservas.reservas.repositories.ReservaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    public ReporteController(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/ocupacion")
    public List<OcupacionDto> getOcupacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(LocalTime.MAX);

        List<Ambiente> ambientes = ambienteRepository.findAll();
        long horasLaborables = 16;

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

            OcupacionDto ocupacion = new OcupacionDto(ambiente.getNombre(), horasReservadas, porcentajeRedondeado);
            resultado.add(ocupacion);
        }

        return resultado;
    }
}
