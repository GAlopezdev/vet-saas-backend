package com.veterinaria.config;

import com.veterinaria.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    // Inyectamos la lista desde el YAML
    @Value("${app.security.cors.allowed-origins:}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Rutas Públicas (Auth y Documentación)
                        .requestMatchers("/api/v1/auth/**", "/api/v1/consultas/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/error", "/").permitAll()

                        .requestMatchers("/api/payments/**").permitAll() // Webhook de Mercado Pago debe ser público
                        .requestMatchers("/api/orders/**").hasAnyRole("CLIENTE", "EMPRESA", "VETERINARIO")
                        // Admin
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Productos
                        .requestMatchers(HttpMethod.GET,"/api/v1/productos/**").permitAll() // Ver productos suele ser público
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasAnyRole("CLIENTE", "EMPRESA", "VETERINARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAnyRole("CLIENTE","EMPRESA", "VETERINARIO")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasAnyRole("CLIENTE","EMPRESA", "VETERINARIO")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:4200",
                "https://nelly-unrestorative-ronald.ngrok-free.dev"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "ngrok-skip-browser-warning" // Importante para probar con navegador y Ngrok
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}