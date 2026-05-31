package com.orcamento.repository;

import com.orcamento.model.Recebimento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {
    @Override
    @EntityGraph(attributePaths = {"orcamento", "orcamento.cliente"})
    List<Recebimento> findAll();

    @Override
    @EntityGraph(attributePaths = {"orcamento", "orcamento.cliente"})
    Optional<Recebimento> findById(Long id);

    @EntityGraph(attributePaths = {"orcamento", "orcamento.cliente"})
    List<Recebimento> findByStatus(String status);
}
