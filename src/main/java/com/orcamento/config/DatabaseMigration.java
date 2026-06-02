package com.orcamento.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1) // roda antes do AdminInitializer (que não tem @Order)
@RequiredArgsConstructor
public class DatabaseMigration implements ApplicationRunner {

    private final JdbcTemplate jdbc;

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Remove o CHECK constraint de enum antigo ('ADMIN','CLIENTE') e
            // transforma em VARCHAR livre para aceitar SUPER_ADMIN também
            jdbc.execute("ALTER TABLE usuarios ALTER COLUMN role VARCHAR(255) NOT NULL");
            log.info("Migration: coluna role atualizada para aceitar SUPER_ADMIN");
        } catch (Exception e) {
            // Coluna já foi migrada ou é compatível — ok
            log.debug("Migration role: {}", e.getMessage());
        }
    }
}
