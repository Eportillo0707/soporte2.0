package com.taller.soporte.controller;

import com.taller.soporte.entity.EstadoSolicitud;
import com.taller.soporte.entity.Solicitud;
import com.taller.soporte.entity.Usuario;
import com.taller.soporte.repository.UsuarioRepository;
import com.taller.soporte.service.SolicitudService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final UsuarioRepository usuarioRepository;

    public SolicitudController(SolicitudService solicitudService,
                               UsuarioRepository usuarioRepository) {
        this.solicitudService = solicitudService;
        this.usuarioRepository = usuarioRepository;
    }

    // USUARIO: Crear solicitud para el cliente del usuario logueado
    @PostMapping("/mia")
    public Solicitud crearSolicitudMia(Authentication authentication,
                                       @RequestParam String descripcion) {

        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long clienteId = usuario.getCliente().getId();

        return solicitudService.crearSolicitud(clienteId, descripcion);
    }

    // USUARIO: Listar solo mis solicitudes
    @GetMapping("/mias")
    public List<Solicitud> listarMisSolicitudes(Authentication authentication) {

        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long clienteId = usuario.getCliente().getId();

        return solicitudService.listarPorCliente(clienteId);
    }

    //  ADMIN: Listar todas
    @GetMapping
    public List<Solicitud> listarSolicitudes() {
        return solicitudService.listarSolicitudes();
    }

    // ADMIN: Listar por cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Solicitud> listarPorCliente(@PathVariable Long clienteId) {
        return solicitudService.listarPorCliente(clienteId);
    }

    //  ADMIN: Cambiar estado
    @PutMapping("/{id}/estado")
    public Solicitud cambiarEstado(@PathVariable Long id,
                                   @RequestParam EstadoSolicitud estado) {
        return solicitudService.cambiarEstado(id, estado);
    }

    // ADMIN: Eliminar solicitud
    @DeleteMapping("/{id}")
    public void eliminarSolicitud(@PathVariable Long id) {
        solicitudService.eliminarSolicitud(id);
    }
}