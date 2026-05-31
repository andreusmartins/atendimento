package com.orcamento.controller;

import com.orcamento.dto.OrcamentoDTO;
import com.orcamento.model.Orcamento;
import com.orcamento.service.OrcamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    @GetMapping
    public List<Orcamento> listar() {
        return orcamentoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Orcamento> criar(@RequestBody OrcamentoDTO dto) {
        return ResponseEntity.ok(orcamentoService.criar(dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Orcamento> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orcamentoService.atualizarStatus(id, status));
    }

    @PatchMapping("/{id}/agenda")
    public ResponseEntity<Orcamento> atualizarAgenda(@PathVariable Long id, @RequestBody OrcamentoDTO dto) {
        return ResponseEntity.ok(orcamentoService.atualizarAgenda(id, dto));
    }

    @PatchMapping("/{id}/servico")
    public ResponseEntity<Orcamento> transformarEmServico(@PathVariable Long id) {
        return ResponseEntity.ok(orcamentoService.transformarEmServico(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        orcamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
