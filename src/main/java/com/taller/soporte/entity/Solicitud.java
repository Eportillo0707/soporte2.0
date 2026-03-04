package com.taller.soporte.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    private LocalDateTime fechaCreacion;

    // Relación ManyToOne con Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Solicitud() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public Solicitud(String descripcion, Cliente cliente) {
        this.descripcion = descripcion;
        this.cliente = cliente;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public EstadoSolicitud getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Cliente getCliente() { return cliente; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}