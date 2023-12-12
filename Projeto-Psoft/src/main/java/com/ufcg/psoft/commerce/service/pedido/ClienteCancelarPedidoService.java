package com.ufcg.psoft.commerce.service.pedido;

@FunctionalInterface
public interface ClienteCancelarPedidoService {

    void cancelar(Long pedidoId, String codigoAcesso);
}
