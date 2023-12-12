package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
@FunctionalInterface
public interface EntregadorCriarService {
     Entregador criar(EntregadorPostPutRequestDTO estabelecimentoPostPutRequestDTO);
}
