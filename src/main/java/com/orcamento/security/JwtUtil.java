package com.orcamento.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:minha-chave-secreta-super-segura-para-o-sistema-2024}")
    private String secret;

    private static final long EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(String email, String role, Long empresaId) {
        var builder = Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS));
        if (empresaId != null) {
            builder.claim("empresaId", empresaId);
        }
        return builder.signWith(getKey()).compact();
    }

    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extrairRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Long extrairEmpresaId(String token) {
        Object val = getClaims(token).get("empresaId");
        if (val == null) return null;
        return ((Number) val).longValue();
    }

    public boolean validar(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
