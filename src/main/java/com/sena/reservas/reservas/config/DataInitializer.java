package com.sena.reservas.reservas.config;

import com.sena.reservas.reservas.entities.Ambiente;
import com.sena.reservas.reservas.entities.Reserva;
import com.sena.reservas.reservas.enums.TipoAmbiente;
import com.sena.reservas.reservas.enums.TipoEstado;
import com.sena.reservas.reservas.repositories.AmbienteRepository;
import com.sena.reservas.reservas.repositories.ReservaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    public DataInitializer(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) {
        if (ambienteRepository.count() > 0) {
            return;
        }

        Ambiente salaLluvia = new Ambiente();
        salaLluvia.setNombre("Sala Lluvia");
        salaLluvia.setTipo(TipoAmbiente.SALA);
        salaLluvia.setCapacidad(30);
        salaLluvia.setActivo(true);
        ambienteRepository.save(salaLluvia);

        Ambiente laboratorioSol = new Ambiente();
        laboratorioSol.setNombre("Laboratorio Sol");
        laboratorioSol.setTipo(TipoAmbiente.LABORATORIO);
        laboratorioSol.setCapacidad(20);
        laboratorioSol.setActivo(true);
        ambienteRepository.save(laboratorioSol);

        Ambiente auditorioCentral = new Ambiente();
        auditorioCentral.setNombre("Auditorio Central");
        auditorioCentral.setTipo(TipoAmbiente.AUDITORIO);
        auditorioCentral.setCapacidad(100);
        auditorioCentral.setActivo(true);
        ambienteRepository.save(auditorioCentral);

        Ambiente salaNube = new Ambiente();
        salaNube.setNombre("Sala Nube");
        salaNube.setTipo(TipoAmbiente.SALA);
        salaNube.setCapacidad(15);
        salaNube.setActivo(true);
        ambienteRepository.save(salaNube);

        Ambiente laboratorioInactivo = new Ambiente();
        laboratorioInactivo.setNombre("Laboratorio Obsoleto");
        laboratorioInactivo.setTipo(TipoAmbiente.LABORATORIO);
        laboratorioInactivo.setCapacidad(25);
        laboratorioInactivo.setActivo(false);
        ambienteRepository.save(laboratorioInactivo);

        LocalDateTime manana = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime pasado = LocalDateTime.now().minusDays(7).withHour(8).withMinute(0).withSecond(0).withNano(0);

        Reserva r1 = new Reserva();
        r1.setAmbiente(salaLluvia);
        r1.setNombreInstructor("Juan Perez");
        r1.setFechaHoraInicio(manana);
        r1.setFechaHoraFin(manana.plusHours(2));
        r1.setNumAprendices(25);
        r1.setEstado(TipoEstado.ACTIVO);
        reservaRepository.save(r1);

        Reserva r2 = new Reserva();
        r2.setAmbiente(laboratorioSol);
        r2.setNombreInstructor("Maria Gomez");
        r2.setFechaHoraInicio(manana.withHour(14));
        r2.setFechaHoraFin(manana.withHour(16));
        r2.setNumAprendices(15);
        r2.setEstado(TipoEstado.ACTIVO);
        reservaRepository.save(r2);

        Reserva r3 = new Reserva();
        r3.setAmbiente(salaLluvia);
        r3.setNombreInstructor("Pedro Lopez");
        r3.setFechaHoraInicio(manana.withHour(10));
        r3.setFechaHoraFin(manana.withHour(11));
        r3.setNumAprendices(10);
        r3.setEstado(TipoEstado.ACTIVO);
        reservaRepository.save(r3);

        Reserva r4 = new Reserva();
        r4.setAmbiente(auditorioCentral);
        r4.setNombreInstructor("Juan Perez");
        r4.setFechaHoraInicio(manana.plusDays(1).withHour(9));
        r4.setFechaHoraFin(manana.plusDays(1).withHour(12));
        r4.setNumAprendices(50);
        r4.setEstado(TipoEstado.ACTIVO);
        reservaRepository.save(r4);

        Reserva r5 = new Reserva();
        r5.setAmbiente(salaLluvia);
        r5.setNombreInstructor("Maria Gomez");
        r5.setFechaHoraInicio(pasado);
        r5.setFechaHoraFin(pasado.plusHours(2));
        r5.setNumAprendices(20);
        r5.setEstado(TipoEstado.FINALIZADO);
        reservaRepository.save(r5);
    }
}
