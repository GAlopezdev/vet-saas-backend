package com.veterinaria.controller;

import com.veterinaria.dto.request.LoginRequest;
import com.veterinaria.dto.request.RegistroClienteRequest;
import com.veterinaria.dto.request.RegistroEmpresaRequest;
import com.veterinaria.dto.request.RegistroVeterinarioRequest;
import com.veterinaria.dto.response.AuthResponse;
import com.veterinaria.dto.response.RegistroResponse;
import com.veterinaria.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register-cliente")
    public ResponseEntity<RegistroResponse> registrarCliente(@Valid @RequestBody RegistroClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarCliente(request));
    }

    @PostMapping("/register-veterinario")
    public ResponseEntity<RegistroResponse> registrarVeterinario(@Valid @RequestBody RegistroVeterinarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarVeterinario(request));
    }

    @PostMapping("/register-empresa")
    public ResponseEntity<RegistroResponse> registrarEmpresa(@Valid @RequestBody RegistroEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarEmpresa(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verificarCuenta(@RequestParam String token) {
        authService.verificarCuenta(token);
        return ResponseEntity.ok("Cuenta activada exitosamente. Ya puedes iniciar sesión.");
    }
}
