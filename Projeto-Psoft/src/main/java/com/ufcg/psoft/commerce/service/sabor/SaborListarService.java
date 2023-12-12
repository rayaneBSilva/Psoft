package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.model.Sabor;

import java.util.List;

@FunctionalInterface
public interface SaborListarService {
    List<Sabor> listar(Long saborId, Long estabelecimentoId, String codigoDeAcesso);
}
