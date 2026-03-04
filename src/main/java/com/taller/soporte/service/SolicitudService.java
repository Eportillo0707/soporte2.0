package com.taller.soporte.service;

import com.taller.soporte.entity.Cliente;
import com.taller.soporte.entity.Solicitud;
import com.taller.soporte.repository.ClienteRepository;
import com.taller.soporte.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudService(SolicitudRepository solicitudRepository,
                            ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
    }

    public Solicitud crearSolicitud(Long clienteId, String descripcion) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        //  ya pone estado=PENDIENTE y fechaCreacion automáticamente
        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion(descripcion);
        solicitud.setCliente(cliente);

        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> listarPorCliente(Long clienteId) {
        return solicitudRepository.findByClienteId(clienteId);
    }

    public Solicitud cambiarEstado(Long solicitudId, com.taller.soporte.entity.EstadoSolicitud nuevoEstado) {

        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }

    public void eliminarSolicitud(Long id) {
        if (!solicitudRepository.existsById(id)) {
            throw new RuntimeException("Solicitud no encontrada");
        }
        solicitudRepository.deleteById(id);
    }
}