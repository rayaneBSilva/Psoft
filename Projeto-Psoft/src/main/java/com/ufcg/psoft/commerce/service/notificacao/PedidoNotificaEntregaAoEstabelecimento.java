package com.ufcg.psoft.commerce.service.notificacao;

import com.ufcg.psoft.commerce.model.Pedido;

public interface PedidoNotificaEntregaAoEstabelecimento {
    void notifica(Pedido pedido);
}
