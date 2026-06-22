package com.sena.reservas.reservas.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservaResponseDto {

    private Long id;

    private String nombreAmbiente;

    private String nombreInstructor;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private int numAprendices;

    private String estado;
}
