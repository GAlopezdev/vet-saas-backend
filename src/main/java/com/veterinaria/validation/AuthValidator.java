package com.veterinaria.validation;

import com.veterinaria.repository.TipoEmpresaRepository;
import com.veterinaria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UsuarioRepository usuarioRepository;
    private final TipoEmpresaRepository tipoEmpresaRepository;

    public void validarCorreoUnico(String correo) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new RuntimeException("Este correo ya esta en uso. Por favor, utilice otro.");
        }
    }

    public void validarEstadoUsuario(boolean estado) {
        if (!estado) {
            throw new RuntimeException("La cuenta se encuentra desactivada. Contacte al soporte.");
        }
    }

    public void validarTipoEmpresaExiste(Long tipoEmpresaId) {
        if (!tipoEmpresaRepository.existsById(tipoEmpresaId)) {
            throw new RuntimeException("El tipo de empresa no existe");
        }
    }

}
