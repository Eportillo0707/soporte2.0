package com.taller.soporte.repository;

import com.taller.soporte.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Metodo para buscar cliente por correo
    Optional<Cliente> findByCorreo(String correo);
}