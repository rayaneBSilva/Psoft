package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.model.Entregador;

@FunctionalInterface
public interface EntregadorListarByIdService {
    Entregador listarById(Long id);
}
