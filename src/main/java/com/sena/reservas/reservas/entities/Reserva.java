package com.sena.reservas.reservas.entities;

import com.sena.reservas.reservas.enums.TipoEstado;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity(name = "reservas")
@Data
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ambiente_id")
    private Ambiente ambiente;

    private String nombreInstructor;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private int numAprendices;

    @Enumerated(EnumType.STRING)
    private TipoEstado estado;
}
