package com.orcamento.service;

import com.orcamento.dto.RecebimentoDTO;
import com.orcamento.model.Orcamento;
import com.orcamento.model.Recebimento;
import com.orcamento.repository.OrcamentoRepository;
import com.orcamento.repository.RecebimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecebimentoService {

    private final RecebimentoRepository recebimentoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final ContextoEmpresaService contexto;

    public List<Recebimento> listarTodos() {
        if (contexto.isSuperAdmin()) return recebimentoRepository.findAll();
        Long empresaId = contexto.getEmpresaId();
        if (empresaId == null) return List.of();
        return recebimentoRepository.findByOrcamentoClienteEmpresaId(empresaId);
    }

    public List<Recebimento> listarPorStatus(String status) {
        if (contexto.isSuperAdmin()) return recebimentoRepository.findByStatus(status);
        Long empresaId = contexto.getEmpresaId();
        if (empresaId == null) return List.of();
        return recebimentoRepository.findByStatusAndOrcamentoClienteEmpresaId(status, empresaId);
    }

    public Recebimento criar(RecebimentoDTO dto) {
        Orcamento orcamento = orcamentoRepository.findById(dto.getOrcamentoId())
            .orElseThrow(() -> new RuntimeException("Orcamento nao encontrado"));

        Recebimento recebimento = new Recebimento();
        recebimento.setOrcamento(orcamento);
        recebimento.setValor(dto.getValor());
        recebimento.setStatus(dto.getStatus());
        recebimento.setFormaPagamento(dto.getFormaPagamento());
        recebimento.setPix(dto.getPix());
        recebimento.setDataVencimento(dto.getDataVencimento());
        recebimento.setObservacao(dto.getObservacao());

        Recebimento salvo = recebimentoRepository.save(recebimento);
        return recebimentoRepository.findById(salvo.getId()).orElse(salvo);
    }

    public Recebimento atualizarStatus(Long id, String status) {
        Recebimento recebimento = recebimentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Recebimento nao encontrado"));
        recebimento.setStatus(status);
        return recebimentoRepository.save(recebimento);
    }

    public void deletar(Long id) {
        recebimentoRepository.deleteById(id);
    }
}
