package com.sena.reservas.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OcupacionDto {

    private String nombreAmbiente;

    private long horasReservadas;

    private double porcentajeOcupacion;
}
