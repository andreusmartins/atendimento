package com.orcamento.service;

import com.orcamento.model.Cliente;
import com.orcamento.model.Empresa;
import com.orcamento.repository.ClienteRepository;
import com.orcamento.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final ContextoEmpresaService contexto;

    public List<Cliente> listarTodos() {
        if (contexto.isSuperAdmin()) return clienteRepository.findAll();
        Long empresaId = contexto.getEmpresaId();
        if (empresaId == null) return List.of();
        return clienteRepository.findByEmpresaId(empresaId);
    }

    public Cliente criar(Cliente cliente) {
        if (!contexto.isSuperAdmin()) {
            Long empresaId = contexto.getEmpresaId();
            if (empresaId != null) {
                Empresa empresa = empresaRepository.findById(empresaId).orElseThrow();
                cliente.setEmpresa(empresa);
            }
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.setNome(dados.getNome());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEndereco(dados.getEndereco());
        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
}
