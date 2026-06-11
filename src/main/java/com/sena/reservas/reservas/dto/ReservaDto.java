package com.sena.reservas.reservas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDto {

    @NotNull(message = "El ID del ambiente es obligatorio")
    private Long ambienteId;

    @NotBlank(message = "El nombre del instructor es obligatorio")
    private String nombreInstructor;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es obligatoria")
    private LocalDateTime fechaHoraFin;

    @Min(value = 1, message = "Debe haber al menos 1 aprendiz")
    private int numAprendices;
}
