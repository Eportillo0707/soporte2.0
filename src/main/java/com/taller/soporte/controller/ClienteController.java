package com.taller.soporte.controller;

import com.taller.soporte.entity.Cliente;
import com.taller.soporte.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Listar clientes
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }
}