package com.ufcg.psoft.commerce.service.notificacao;

@FunctionalInterface
public interface PedidoNotificaClienteService {

    void notificar(Long pedidoId);
}