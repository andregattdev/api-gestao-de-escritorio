package com.ag.apicadastropessoas.repository;

import com.ag.apicadastropessoas.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {}
