package com.orcamento.controller;

import com.orcamento.model.Empresa;
import com.orcamento.model.Usuario;
import com.orcamento.repository.EmpresaRepository;
import com.orcamento.repository.UsuarioRepository;
import com.orcamento.service.ContextoEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContextoEmpresaService contexto;

    @GetMapping
    public List<Usuario> listar() {
        List<Usuario> lista;
        if (contexto.isSuperAdmin()) {
            lista = usuarioRepository.findAll();
        } else {
            Long empresaId = contexto.getEmpresaId();
            lista = empresaId != null ? usuarioRepository.findByEmpresaId(empresaId) : List.of();
        }
        lista.forEach(u -> u.setSenha(""));
        return lista;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (usuarioRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Email já cadastrado"));
        }
        var u = new Usuario();
        u.setNome(body.get("nome"));
        u.setEmail(email);
        u.setSenha(passwordEncoder.encode(body.get("senha")));
        u.setRole(Usuario.Role.CLIENTE);
        u.setAtivo(true);

        // Vincula à empresa do admin que criou (ou empresa informada pelo SUPER_ADMIN)
        String empresaIdStr = body.get("empresaId");
        if (empresaIdStr != null) {
            empresaRepository.findById(Long.parseLong(empresaIdStr))
                .ifPresent(u::setEmpresa);
        } else if (!contexto.isSuperAdmin()) {
            Long empresaId = contexto.getEmpresaId();
            if (empresaId != null) {
                empresaRepository.findById(empresaId).ifPresent(u::setEmpresa);
            }
        }

        var salvo = usuarioRepository.save(u);
        salvo.setSenha("");
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}/ativo")
    public ResponseEntity<?> alterarAtivo(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return usuarioRepository.findById(id).map(u -> {
            u.setAtivo(body.getOrDefault("ativo", true));
            usuarioRepository.save(u);
            u.setSenha("");
            return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<?> alterarSenha(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return usuarioRepository.findById(id).map(u -> {
            u.setSenha(passwordEncoder.encode(body.get("senha")));
            usuarioRepository.save(u);
            return ResponseEntity.ok(Map.of("ok", true));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
