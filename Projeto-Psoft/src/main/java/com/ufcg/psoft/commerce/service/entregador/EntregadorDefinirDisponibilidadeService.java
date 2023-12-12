package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
@FunctionalInterface
public interface EntregadorDefinirDisponibilidadeService {
    Entregador definirDisponibilidade(Long id, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcesso, boolean disponibilidade);
}