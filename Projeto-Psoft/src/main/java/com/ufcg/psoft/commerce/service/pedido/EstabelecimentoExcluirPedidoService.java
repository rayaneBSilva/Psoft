package com.ufcg.psoft.commerce.service.pedido;


public interface EstabelecimentoExcluirPedidoService {

     void excluir(Long pedidoId, Long estabelecimentoId, String codigoDeAcesso);
     void excluir(Long estabelecimentoId, String codigoDeAcesso);
}
