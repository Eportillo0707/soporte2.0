package com.taller.soporte.config;

import com.taller.soporte.entity.Rol;
import com.taller.soporte.entity.Usuario;
import com.taller.soporte.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder) {

        return args -> {

            // Si no existe "admin", lo crea
            if (usuarioRepository.findByUsername("admin").isEmpty()) {

                Usuario admin = new Usuario();
                admin.setUsername("admin");

                // ✅ Se guarda encriptada (BCrypt)
                admin.setPassword(passwordEncoder.encode("admin123"));

                admin.setRol(Rol.ADMIN);

                // Nota: tu ADMIN no necesita cliente, así que lo dejamos null
                admin.setCliente(null);

                usuarioRepository.save(admin);

                System.out.println("✅ Usuario ADMIN creado automáticamente");
                System.out.println("Usuario: admin");
                System.out.println("Password: admin123");
            }
        };
    }
}