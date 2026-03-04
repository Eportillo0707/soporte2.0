package com.taller.soporte.controller;

import com.taller.soporte.entity.Cliente;
import com.taller.soporte.entity.Rol;
import com.taller.soporte.entity.Usuario;
import com.taller.soporte.repository.ClienteRepository;
import com.taller.soporte.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(ClienteRepository clienteRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registro")
    public String registrar(@RequestBody RegistroRequest request) {

        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (clienteRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

       
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());

        Cliente clienteGuardado = clienteRepository.save(cliente);

        // Crear usuario vinculado
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Rol.USUARIO);
        usuario.setCliente(clienteGuardado);

        usuarioRepository.save(usuario);

        return "Cuenta creada correctamente";
    }

    //  para que el frontend sepa rol y oculte menús
    @GetMapping("/me")
    public Usuario me(Authentication authentication) {
        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setPassword(null); 
        return usuario;
    }
}