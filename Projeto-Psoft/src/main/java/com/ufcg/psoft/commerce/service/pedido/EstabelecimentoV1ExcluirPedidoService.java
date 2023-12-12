package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoV1ExcluirPedidoService implements  EstabelecimentoExcluirPedidoService{


    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private Validation validation;


    @Override
    public void excluir(Long pedidoId, Long estabelecimentoId, String codigoDeAcesso) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaEstabelecimento(estabelecimentoId, codigoDeAcesso);
        validation.validaEstabelecimento(pedido.getEstabelecimentoId(), codigoDeAcesso);
        pedidoRepository.delete(pedido);
    }

    @Override
    public void excluir(Long estabelecimentoId, String codigoDeAcesso) {
        validation.validaEstabelecimento(estabelecimentoId, codigoDeAcesso);

        for(Pedido pedido: pedidoRepository.findAll()){
            if(pedido.getEstabelecimentoId().equals(estabelecimentoId)){
                pedidoRepository.delete(pedido);
            }
        }
    }
}
