package com.orcamento.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RecebimentoDTO {
    private Long orcamentoId;
    private Double valor;
    private String status;
    private String formaPagamento;
    private String pix;
    private LocalDate dataVencimento;
    private String observacao;
}
