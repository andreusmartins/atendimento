package com.orcamento.controller;

import com.orcamento.model.Empresa;
import com.orcamento.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.criar(empresa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.atualizar(id, empresa));
    }

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<Empresa> alternarAtivo(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.alternarAtivo(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
