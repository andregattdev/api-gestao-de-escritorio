package com.ag.apicadastropessoas.controller;

import com.ag.apicadastropessoas.model.Empresa;
import com.ag.apicadastropessoas.service.EmpresaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {
    private final EmpresaService service;

    public EmpresaController(EmpresaService service) { this.service = service; }

    @GetMapping
    public List<Empresa> listar() { return service.listarTodas(); }

    @GetMapping("/{id}")
    public Empresa buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(empresa));
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Long id, @RequestBody Empresa empresa) { return service.atualizar(id, empresa); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
