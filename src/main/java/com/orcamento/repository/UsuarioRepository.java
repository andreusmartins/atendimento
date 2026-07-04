package com.orcamento.repository;

import com.orcamento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByEmpresaId(Long empresaId);

    @Query("SELECT u.empresa.id FROM Usuario u WHERE u.email = :email AND u.empresa IS NOT NULL")
    Optional<Long> findEmpresaIdByEmail(@Param("email") String email);
}
