package com.ufcg.psoft.commerce.service.notificacao;

public interface EstabelecimentoNotificaClientePedidoPronto {
    void notifica(Long pedidoId, Long estabelecimentoId);
}
