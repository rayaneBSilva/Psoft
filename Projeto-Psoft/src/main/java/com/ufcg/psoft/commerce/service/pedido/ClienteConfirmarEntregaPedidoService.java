package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface ClienteConfirmarEntregaPedidoService {
    Pedido clienteConfirmaEntrega(Long pedidoId, Long clienteId, String clienteCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}