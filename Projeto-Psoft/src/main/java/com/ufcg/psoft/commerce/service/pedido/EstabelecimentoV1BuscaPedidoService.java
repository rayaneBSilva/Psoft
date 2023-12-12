package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstabelecimentoV1BuscaPedidoService implements EstabelecimentoBuscaPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    Validation validation;

    @Override
    public Pedido estabelecimentoBusca(Long pedidoId, Long estabelecimentoId, String codigoDeAcessoEstabelecimento) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaEstabelecimento(estabelecimentoId, codigoDeAcessoEstabelecimento);
        validation.validaEstabelecimento(pedido.getEstabelecimentoId(), codigoDeAcessoEstabelecimento);
        return pedido;
    }

    @Override
    public List<Pedido> estabelecimentoBusca(Long estabelecimentoId, String codigoDeAcessoEstabelecimento) {
        validation.validaEstabelecimento(estabelecimentoId, codigoDeAcessoEstabelecimento);

        List<Pedido> pedidosEncontrados = new ArrayList<>();

        for(Pedido pedido: pedidoRepository.findAll()){
            if(pedido.getEstabelecimentoId().equals(estabelecimentoId)){
                pedidosEncontrados.add(pedido);
            }
        }
        return pedidosEncontrados;
    }
}

