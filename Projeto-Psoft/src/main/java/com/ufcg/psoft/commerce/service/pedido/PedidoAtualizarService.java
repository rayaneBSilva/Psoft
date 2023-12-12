package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface PedidoAtualizarService {

     Pedido atualizar(Long pedidoId, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}