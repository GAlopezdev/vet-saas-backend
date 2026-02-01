package com.veterinaria.repository;

import com.veterinaria.model.entity.Usuario;
import com.veterinaria.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUsuario(Usuario usuario);

    @Modifying
    @Transactional
    void deleteByUsuario(Usuario usuario);

}
