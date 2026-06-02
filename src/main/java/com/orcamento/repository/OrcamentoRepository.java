package com.orcamento.repository;

import com.orcamento.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findByClienteEmpresaId(Long empresaId);
}
