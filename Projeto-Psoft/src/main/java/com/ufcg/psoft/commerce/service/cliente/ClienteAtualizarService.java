package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente;

@FunctionalInterface
public interface ClienteAtualizarService {
    Cliente atualizar(Long id, String codigoAcesso, ClientePostPutRequestDTO clientePostPutRequestDTO);
}