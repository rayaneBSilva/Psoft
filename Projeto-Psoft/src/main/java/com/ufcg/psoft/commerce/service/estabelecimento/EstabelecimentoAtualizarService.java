package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;

@FunctionalInterface
public interface EstabelecimentoAtualizarService {
     Estabelecimento atualizar(String codigoDeAcesso, Long id, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO);
}
