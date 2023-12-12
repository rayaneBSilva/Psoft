package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface EstabelecimentoAlterarFilaDePedidosService {

     void alterarFilaDePedidos(Pedido pedido, Estabelecimento estabelecimento);
}
