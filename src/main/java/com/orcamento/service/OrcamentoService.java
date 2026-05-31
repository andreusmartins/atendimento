package com.orcamento.service;

import com.orcamento.dto.OrcamentoDTO;
import com.orcamento.model.Cliente;
import com.orcamento.model.ItemOrcamento;
import com.orcamento.model.Orcamento;
import com.orcamento.repository.ClienteRepository;
import com.orcamento.repository.OrcamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ClienteRepository clienteRepository;

    public List<Orcamento> listarTodos() {
        return orcamentoRepository.findAll();
    }

    public Orcamento criar(OrcamentoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));

        Orcamento orcamento = new Orcamento();
        orcamento.setCliente(cliente);
        aplicarDadosAgenda(orcamento, dto);
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            orcamento.setStatus(dto.getStatus());
        }

        List<ItemOrcamento> itens = dto.getItens().stream().map(itemDTO -> {
            ItemOrcamento item = new ItemOrcamento();
            item.setDescricao(itemDTO.getDescricao());
            item.setValor(itemDTO.getValor());
            item.setObservacao(itemDTO.getObservacao());
            item.setOrcamento(orcamento);
            return item;
        }).toList();

        orcamento.setItens(itens);
        return orcamentoRepository.save(orcamento);
    }

    public Orcamento atualizarStatus(Long id, String status) {
        Orcamento orcamento = buscarPorId(id);
        orcamento.setStatus(status);
        return orcamentoRepository.save(orcamento);
    }

    public Orcamento atualizarAgenda(Long id, OrcamentoDTO dto) {
        Orcamento orcamento = buscarPorId(id);
        aplicarDadosAgenda(orcamento, dto);
        return orcamentoRepository.save(orcamento);
    }

    public Orcamento transformarEmServico(Long id) {
        Orcamento orcamento = buscarPorId(id);
        orcamento.setOrdemServico(true);
        if (orcamento.getStatus() == null || orcamento.getStatus().equals("pendente")) {
            orcamento.setStatus("aprovado");
        }
        return orcamentoRepository.save(orcamento);
    }

    public void deletar(Long id) {
        orcamentoRepository.deleteById(id);
    }

    private Orcamento buscarPorId(Long id) {
        return orcamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orcamento nao encontrado"));
    }

    private void aplicarDadosAgenda(Orcamento orcamento, OrcamentoDTO dto) {
        orcamento.setTipoServico(dto.getTipoServico());
        orcamento.setDataVisita(dto.getDataVisita());
        orcamento.setDataServico(dto.getDataServico());
        orcamento.setDataRetorno(dto.getDataRetorno());
        orcamento.setObservacoesAgenda(dto.getObservacoesAgenda());
    }
}
