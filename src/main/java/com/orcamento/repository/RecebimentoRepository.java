package com.orcamento.repository;

import com.orcamento.model.Recebimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {
    List<Recebimento> findByStatus(String status);
    List<Recebimento> findByOrcamentoClienteEmpresaId(Long empresaId);
    List<Recebimento> findByStatusAndOrcamentoClienteEmpresaId(String status, Long empresaId);
}
