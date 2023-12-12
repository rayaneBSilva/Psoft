package com.ufcg.psoft.commerce.service.pedido;


import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

@FunctionalInterface
public interface ClienteListarPedidoEstabelecimentoService {
    List<Pedido> listar(Long pedidoId, Long clienteId, Long estabelecimentoId, String codigoAcesso, String status);
}
