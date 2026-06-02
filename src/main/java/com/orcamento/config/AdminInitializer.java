package com.orcamento.config;

import com.orcamento.model.Usuario;
import com.orcamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@sistema.com}")
    private String adminEmail;

    @Value("${admin.senha:admin123}")
    private String adminSenha;

    @Bean
    public ApplicationRunner criarAdminInicial() {
        return args -> {
            if (!usuarioRepository.existsByEmail(adminEmail)) {
                var admin = new Usuario();
                admin.setNome("Super Admin");
                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode(adminSenha));
                admin.setRole(Usuario.Role.SUPER_ADMIN);
                admin.setAtivo(true);
                // SUPER_ADMIN não tem empresa vinculada
                usuarioRepository.save(admin);
                log.info("==============================================");
                log.info("SUPER ADMIN criado! Email: {} | Senha: {}", adminEmail, adminSenha);
                log.info("==============================================");
            } else {
                // Migra admin existente para SUPER_ADMIN se necessário
                usuarioRepository.findByEmail(adminEmail).ifPresent(u -> {
                    if (u.getRole() == Usuario.Role.ADMIN) {
                        u.setRole(Usuario.Role.SUPER_ADMIN);
                        usuarioRepository.save(u);
                        log.info("Admin migrado para SUPER_ADMIN: {}", adminEmail);
                    }
                });
            }
        };
    }
}
