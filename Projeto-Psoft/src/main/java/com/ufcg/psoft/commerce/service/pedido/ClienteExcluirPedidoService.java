package com.ufcg.psoft.commerce.service.pedido;


public interface ClienteExcluirPedidoService {

     void excluir(Long pedidoId, Long clienteID, String codigoDeAcesso);
     void excluir(Long clienteId, String codigoDeAcesso);
}
