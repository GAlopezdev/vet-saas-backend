package com.veterinaria.repository;

import com.veterinaria.model.entity.Veterinario;
import com.veterinaria.model.enums.EstadoRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByUsuarioCorreo(String currentUserEmail);

    @EntityGraph(attributePaths = {"usuario"})
    Page<Veterinario> findByEstadoRegistro(EstadoRegistro estado, Pageable pageable);

    @EntityGraph(attributePaths = {"usuario"})
    @Query("SELECT v FROM Veterinario v")
    Page<Veterinario> findAllVeterinarios(Pageable pageable);

    @Query("SELECT v FROM Veterinario v WHERE " +
            "(LOWER(v.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(v.apepa) LIKE LOWER(CONCAT('%', :filtro, '%'))) AND " +
            "v.idVeterinario NOT IN (SELECT ev.veterinario.idVeterinario FROM EmpresaVeterinario ev WHERE ev.empresa.idEmpresa = :idEmpresa)")
    List<Veterinario> buscarVeterinariosNoVinculados(String filtro, Long idEmpresa);
}
