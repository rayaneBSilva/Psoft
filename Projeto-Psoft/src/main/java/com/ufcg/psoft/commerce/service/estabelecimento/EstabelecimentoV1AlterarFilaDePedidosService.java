package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoV1AlterarFilaDePedidosService implements EstabelecimentoAlterarFilaDePedidosService{

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public void alterarFilaDePedidos(Pedido pedido, Estabelecimento estabelecimento) {
        List<Pedido> pedidosPendentes = estabelecimento.getPedidosPendentes();
        pedidosPendentes.add(pedido);
        estabelecimento.setPedidosPendentes(pedidosPendentes);

        estabelecimentoRepository.save(estabelecimento);

    }
}
