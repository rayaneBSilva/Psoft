package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Sabor;

@FunctionalInterface

public interface SaborCriarService {
    Sabor criar(SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String codigoDeAcesso);
}
