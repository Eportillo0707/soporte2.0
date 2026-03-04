package com.taller.soporte.repository;

import com.taller.soporte.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Buscar solicitudes por cliente
    List<Solicitud> findByClienteId(Long clienteId);

}