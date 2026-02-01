package com.veterinaria.service.impl;

import com.veterinaria.dto.request.LoginRequest;
import com.veterinaria.dto.request.RegistroClienteRequest;
import com.veterinaria.dto.request.RegistroEmpresaRequest;
import com.veterinaria.dto.request.RegistroVeterinarioRequest;
import com.veterinaria.dto.response.AuthResponse;
import com.veterinaria.dto.response.RegistroResponse;
import com.veterinaria.exception.AccountNotVerifiedException;
import com.veterinaria.model.entity.*;
import com.veterinaria.model.enums.UserRol;
import com.veterinaria.repository.*;
import com.veterinaria.security.JwtService;
import com.veterinaria.service.AuthService;
import com.veterinaria.service.EmailService;
import com.veterinaria.validation.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final TipoEmpresaRepository tipoEmpresaRepository;
    private final VerificationTokenRepository tokenRepository;

    private final EmailService emailService;

    private final AuthValidator authValidator;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public RegistroResponse registrarCliente(RegistroClienteRequest request) {
        authValidator.validarCorreoUnico(request.correo());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(request.correo());
        nuevoUsuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        nuevoUsuario.setRol(UserRol.CLIENTE);
        usuarioRepository.save(nuevoUsuario);

        PerfilCliente nuevoPerfil = new PerfilCliente(
                nuevoUsuario,
                request.nombres(),
                request.apepa(),
                request.apema(),
                request.telefono(),
                request.pais());
        clienteRepository.save(nuevoPerfil);

        prepararVerificacionYEnviarEmail(nuevoUsuario);

        return new RegistroResponse(
                "Registro exitoso. Por favor, verifica tu bandeja de entrada para activar tu cuenta.",
                nuevoUsuario.getCorreo()
        );
    }

    @Override
    @Transactional
    public RegistroResponse registrarEmpresa(RegistroEmpresaRequest request) {
        authValidator.validarCorreoUnico(request.correo());
        authValidator.validarTipoEmpresaExiste(request.idTipoEmpresa());

        TipoEmpresa tipo = tipoEmpresaRepository.getReferenceById(request.idTipoEmpresa());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(request.correo());
        nuevoUsuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        nuevoUsuario.setRol(UserRol.EMPRESA);
        usuarioRepository.save(nuevoUsuario);

        Empresa nuevaEmpresa = new Empresa(
                nuevoUsuario,
                tipo,
                request.nombreComercial(),
                request.telefono(),
                request.pais(),
                request.ciudad(),
                request.direccion());
        empresaRepository.save(nuevaEmpresa);

        prepararVerificacionYEnviarEmail(nuevoUsuario);

        return new RegistroResponse(
                "Registro exitoso. Por favor, verifica tu bandeja de entrada para activar tu cuenta.",
                nuevoUsuario.getCorreo()
        );
    }

    @Override
    @Transactional
    public RegistroResponse registrarVeterinario(RegistroVeterinarioRequest request) {
        authValidator.validarCorreoUnico(request.correo());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(request.correo());
        nuevoUsuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        nuevoUsuario.setRol(UserRol.VETERINARIO);
        usuarioRepository.save(nuevoUsuario);

        Veterinario nuevoVeterinario = new Veterinario(
                request.especialidad(),
                request.telefono(),
                request.apema(),
                request.apepa(),
                request.nombres(),
                nuevoUsuario);
        veterinarioRepository.save(nuevoVeterinario);

        prepararVerificacionYEnviarEmail(nuevoUsuario);

        return new RegistroResponse(
                "Registro exitoso. Por favor, verifica tu bandeja de entrada para activar tu cuenta.",
                nuevoUsuario.getCorreo()
        );
    }

    @Override
    @Transactional(noRollbackFor = AccountNotVerifiedException.class)
    public AuthResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contrasenia())
        );

        Usuario usuario = (Usuario) auth.getPrincipal();

        if(!usuario.isEmailVerificado()){
            prepararVerificacionYEnviarEmail(usuario);
            throw new AccountNotVerifiedException("Cuenta no verificada. Se envió un nuevo enlace.");
        }

        authValidator.validarEstadoUsuario(usuario.isEnabled());

        return generarAuthResponse(usuario);
    }

    @Override
    @Transactional
    public void verificarCuenta(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token de activación no válido o inexistente"));

        if (verificationToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El enlace de activación ha expirado");
        }

        Usuario usuario = verificationToken.getUsuario();
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        tokenRepository.delete(verificationToken);
    }

    // Metodos auxiliares

    private AuthResponse generarAuthResponse(Usuario usuario) {
        return new AuthResponse(
                jwtService.getToken(usuario),
                usuario.getCorreo(),
                usuario.getRol().name()
        );
    }

    private void prepararVerificacionYEnviarEmail(Usuario usuario) {
        VerificationToken vToken = tokenRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    VerificationToken nuevo = new VerificationToken();
                    nuevo.setUsuario(usuario);
                    return nuevo;
                });

        vToken.setToken(UUID.randomUUID().toString());
        vToken.setFechaExpiracion(LocalDateTime.now().plusHours(24));

        tokenRepository.save(vToken);

        emailService.sendRegistrationEmail(usuario, vToken.getToken());
    }
}
