package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

public interface EstabelecimentoBuscaPedidoService {

    Pedido estabelecimentoBusca(Long pedidoId, Long estabelecimentoId, String codigoDeAcesso);

    List<Pedido> estabelecimentoBusca(Long estabelecimentoId, String codigoDeAcesso);
}