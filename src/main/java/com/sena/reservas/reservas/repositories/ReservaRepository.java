package com.sena.reservas.reservas.repositories;

import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.enums.TipoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM reservas r WHERE r.ambiente.id = :ambienteId AND r.estado = :estado AND r.fechaHoraInicio < :fechaHoraFin AND r.fechaHoraFin > :fechaHoraInicio")
    List<Reserva> buscarSolapamientos(
            @Param("ambienteId") Long ambienteId,
            @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
            @Param("fechaHoraFin") LocalDateTime fechaHoraFin,
            @Param("estado") TipoEstado estado);

    @Query("SELECT COUNT(r) FROM reservas r WHERE r.nombreInstructor = :nombreInstructor AND r.estado = :estado AND CAST(r.fechaHoraInicio AS LocalDate) = :fecha")
    long contarActivasPorInstructorYFecha(
            @Param("nombreInstructor") String nombreInstructor,
            @Param("fecha") LocalDate fecha,
            @Param("estado") TipoEstado estado);

    @Query("SELECT r FROM reservas r WHERE r.ambiente.id = :ambienteId AND r.estado = :estado AND r.fechaHoraInicio >= :inicio AND r.fechaHoraInicio < :fin ORDER BY r.fechaHoraInicio")
    List<Reserva> findReservasActivasPorAmbienteYFecha(
            @Param("ambienteId") Long ambienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("estado") TipoEstado estado);

    @Query("SELECT r FROM reservas r WHERE r.estado = :estado AND r.fechaHoraInicio < :fechaHoraFin AND r.fechaHoraFin > :fechaHoraInicio")
    List<Reserva> findReservasActivasEnRango(
            @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
            @Param("fechaHoraFin") LocalDateTime fechaHoraFin,
            @Param("estado") TipoEstado estado);
}
