package com.veterinaria.repository;

import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.EmpresaVeterinario;
import com.veterinaria.model.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaVeterinarioRepository extends JpaRepository<EmpresaVeterinario, Long> {

    boolean existsByEmpresaAndVeterinario(Empresa empresa, Veterinario veterinario);

    List<EmpresaVeterinario> findAllByEmpresaIdEmpresa(Long idEmpresa);

    void deleteByEmpresaAndVeterinario(Empresa empresa, Veterinario veterinario);

    void deleteByEmpresaIdEmpresaAndVeterinarioIdVeterinario(Long idEmpresa, Long idVeterinario);

    boolean existsByEmpresaIdEmpresaAndVeterinarioIdVeterinario(Long idEmpresa, Long idVeterinario);
}
