package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;


@FunctionalInterface
public interface PedidoCriarService {

    Pedido criar (Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
