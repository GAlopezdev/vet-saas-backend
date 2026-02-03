package com.veterinaria.service;

import com.veterinaria.model.entity.Usuario;

public interface AdminService {

    void aprobarVeterinario(Long idVeterinario, Usuario admin);
    void rechazarVeterinario(Long idVeterinario, Usuario admin);
    void aprobarEmpresa(Long idEmpresa, Usuario admin);
    void rechazarEmpresa(Long idEmpresa, Usuario admin);

}
