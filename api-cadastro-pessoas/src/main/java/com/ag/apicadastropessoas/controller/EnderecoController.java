package com.ag.apicadastropessoas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ag.apicadastropessoas.model.Endereco;
import com.ag.apicadastropessoas.service.EnderecoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enderecos")
@CrossOrigin(origins = "*")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.buscarPorIdOuFalhar(id));
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<Endereco> buscarPorCep(@PathVariable String cep) {
        Endereco endereco = enderecoService.buscarEnderecoPorCep(cep);
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<Endereco> buscarPorPessoaId(@PathVariable Long pessoaId) {
        Endereco endereco = enderecoService.buscarPorPessoaId(pessoaId)
                .orElseThrow(() -> new com.ag.apicadastropessoas.exceptions.ResourceNotFoundException(
                        "Nenhum endereço encontrado para a pessoa com ID: " + pessoaId));
        return ResponseEntity.ok(endereco);
    }

    @PostMapping
    public ResponseEntity<Endereco> criar(@Valid @RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.salvar(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}