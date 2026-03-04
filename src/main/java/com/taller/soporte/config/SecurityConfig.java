package com.taller.soporte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Públicas
                        .requestMatchers("/login.html", "/registro.html").permitAll()
                        .requestMatchers("/auth/registro").permitAll()
                        .requestMatchers("/auth/login").permitAll()   // (claridad)

                        //  Estáticos
                        .requestMatchers(
                                "/**/*.css", "/**/*.js",
                                "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.svg",
                                "/favicon.ico"
                        ).permitAll()

                        //  Páginas por rol
                        .requestMatchers("/gestion-solicitudes.html").hasRole("ADMIN")
                        .requestMatchers("/solicitudes.html", "/mis-solicitudes.html").hasRole("USUARIO")

                        // ENDPOINTS USUARIO
                        .requestMatchers("/solicitudes/mia", "/solicitudes/mias").hasRole("USUARIO")

                        // ENDPOINTS ADMIN
                        .requestMatchers(HttpMethod.GET, "/solicitudes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/solicitudes/cliente/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/solicitudes/*/estado").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/solicitudes/*").hasRole("ADMIN")


                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/index.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/login.html?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}