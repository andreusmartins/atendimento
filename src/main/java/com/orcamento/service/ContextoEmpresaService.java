package com.orcamento.service;

import com.orcamento.model.Usuario;
import com.orcamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContextoEmpresaService {

    private final UsuarioRepository usuarioRepository;

    public boolean isSuperAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    public Long getEmpresaId() {
        if (isSuperAdmin()) return null;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .map(u -> u.getEmpresa() != null ? u.getEmpresa().getId() : null)
                .orElse(null);
    }

    public Usuario getUsuarioAtual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email).orElseThrow();
    }
}
