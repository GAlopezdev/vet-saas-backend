package com.veterinaria.service;

import com.veterinaria.model.entity.Usuario;

public interface EmailService {

    void sendRegistrationEmail(Usuario usuario, String token);

}
