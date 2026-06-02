package com.orcamento.controller;

import com.orcamento.model.Usuario;
import com.orcamento.repository.UsuarioRepository;
import com.orcamento.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        return usuarioRepository.findByEmail(email)
                .filter(Usuario::isAtivo)
                .filter(u -> passwordEncoder.matches(senha, u.getSenha()))
                .map(u -> {
                    Long empresaId = u.getEmpresa() != null ? u.getEmpresa().getId() : null;
                    String token = jwtUtil.gerarToken(u.getEmail(), u.getRole().name(), empresaId);

                    Map<String, Object> resp = new HashMap<>();
                    resp.put("token", token);
                    resp.put("nome", u.getNome());
                    resp.put("email", u.getEmail());
                    resp.put("role", u.getRole().name());
                    if (empresaId != null) resp.put("empresaId", empresaId);
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("erro", "Email ou senha incorretos")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        return usuarioRepository.findByEmail(principal.getName())
                .map(u -> {
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("nome", u.getNome());
                    resp.put("email", u.getEmail());
                    resp.put("role", u.getRole().name());
                    if (u.getEmpresa() != null) resp.put("empresaId", u.getEmpresa().getId());
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
