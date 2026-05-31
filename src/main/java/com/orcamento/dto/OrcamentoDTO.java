package com.orcamento.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrcamentoDTO {
    private Long clienteId;
    private List<ItemOrcamentoDTO> itens;
    private String status;
    private String tipoServico;
    private LocalDateTime dataVisita;
    private LocalDateTime dataServico;
    private LocalDateTime dataRetorno;
    private String observacoesAgenda;
}
