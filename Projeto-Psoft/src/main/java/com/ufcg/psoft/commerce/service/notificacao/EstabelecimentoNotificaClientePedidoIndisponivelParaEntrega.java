package com.ufcg.psoft.commerce.service.notificacao;

public interface EstabelecimentoNotificaClientePedidoIndisponivelParaEntrega {
    void notifica(Long pedidoId, Long estabelecimentoId);
}
