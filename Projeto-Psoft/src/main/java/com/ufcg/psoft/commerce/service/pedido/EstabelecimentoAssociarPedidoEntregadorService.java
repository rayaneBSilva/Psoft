package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

public interface EstabelecimentoAssociarPedidoEntregadorService {

    Pedido associarPedidoEntregador(Long pedidoId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}