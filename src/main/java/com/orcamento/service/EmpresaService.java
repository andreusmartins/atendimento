package com.orcamento.service;

import com.orcamento.model.Empresa;
import com.orcamento.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public List<Empresa> listarTodas() {
        return empresaRepository.findAllByOrderByNomeAsc();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    public Empresa criar(Empresa empresa) {
        empresa.setAtivo(true);
        return empresaRepository.save(empresa);
    }

    public Empresa atualizar(Long id, Empresa dados) {
        Empresa empresa = buscarPorId(id);
        empresa.setNome(dados.getNome());
        empresa.setTelefone(dados.getTelefone());
        empresa.setValorMensalidade(dados.getValorMensalidade());
        empresa.setDataVencimento(dados.getDataVencimento());
        return empresaRepository.save(empresa);
    }

    public Empresa alternarAtivo(Long id) {
        Empresa empresa = buscarPorId(id);
        empresa.setAtivo(!empresa.isAtivo());
        return empresaRepository.save(empresa);
    }

    public void deletar(Long id) {
        empresaRepository.deleteById(id);
    }
}
