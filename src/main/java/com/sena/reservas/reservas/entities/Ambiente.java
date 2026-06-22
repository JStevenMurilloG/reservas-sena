package com.sena.reservas.reservas.entities;

import com.sena.reservas.reservas.enums.TipoAmbiente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ambientes")
@Data
@NoArgsConstructor
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoAmbiente tipo;

    private int capacidad;

    private boolean activo;
}