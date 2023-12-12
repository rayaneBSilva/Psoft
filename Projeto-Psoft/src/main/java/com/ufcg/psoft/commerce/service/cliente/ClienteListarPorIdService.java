package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.model.Cliente;

@FunctionalInterface
public interface ClienteListarPorIdService {

    Cliente listarPorId(Long id);
}
