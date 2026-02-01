package com.veterinaria.service;

import com.veterinaria.dto.request.LoginRequest;
import com.veterinaria.dto.request.RegistroClienteRequest;
import com.veterinaria.dto.request.RegistroEmpresaRequest;
import com.veterinaria.dto.request.RegistroVeterinarioRequest;
import com.veterinaria.dto.response.AuthResponse;
import com.veterinaria.dto.response.RegistroResponse;

public interface AuthService {

    RegistroResponse registrarCliente(RegistroClienteRequest request);

    RegistroResponse registrarEmpresa(RegistroEmpresaRequest request);

    RegistroResponse registrarVeterinario(RegistroVeterinarioRequest request);

    AuthResponse login(LoginRequest request);

    void verificarCuenta(String token);
}
