package com.ag.apicadastropessoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ag.apicadastropessoas.model.Endereco;
import java.util.List;
import java.util.Optional;


public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByPessoaId(Long pessoaId);

    List<Endereco> findByEmpresaId(Long empresaId);
}
