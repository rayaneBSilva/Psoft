package com.ufcg.psoft.commerce.service.pedido;


import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface EstabelecimentoPrepararPedidoService {

     Pedido prepararPedido(Long pedidoId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}