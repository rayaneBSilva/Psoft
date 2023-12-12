package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

public interface ClienteBuscaPedidoService {
    Pedido clienteBusca(Long pedidoId, Long clienteId, String codigoAcesso);
    List<Pedido> clienteBusca(Long clienteId, String codigoAcesso);
}