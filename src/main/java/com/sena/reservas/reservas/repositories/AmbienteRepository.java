package com.sena.reservas.reservas.repositories;

import com.sena.reservas.reservas.entities.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

    List<Ambiente> findByActivoTrue();
}
