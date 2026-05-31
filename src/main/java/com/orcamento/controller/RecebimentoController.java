package com.orcamento.controller;

import com.orcamento.dto.RecebimentoDTO;
import com.orcamento.model.Recebimento;
import com.orcamento.service.RecebimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/recebimentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecebimentoController {

    private final RecebimentoService recebimentoService;

    @GetMapping
    public List<Recebimento> listar() {
        return recebimentoService.listarTodos();
    }

    @GetMapping("/status/{status}")
    public List<Recebimento> listarPorStatus(@PathVariable String status) {
        return recebimentoService.listarPorStatus(status);
    }

    @PostMapping
    public ResponseEntity<Recebimento> criar(@RequestBody RecebimentoDTO dto) {
        return ResponseEntity.ok(recebimentoService.criar(dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Recebimento> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(recebimentoService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recebimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
