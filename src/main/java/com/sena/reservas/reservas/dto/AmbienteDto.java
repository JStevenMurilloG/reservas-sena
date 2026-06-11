package com.sena.reservas.reservas.dto;

import com.sena.reservas.reservas.enums.TipoAmbiente;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AmbienteDto {

    @NotBlank(message = "El nombre del ambiente es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de ambiente es obligatorio")
    private TipoAmbiente tipo;

    @Min(value = 1, message = "La capacidad minima es 1 persona")
    private int capacidad;

    private boolean activo;
}
