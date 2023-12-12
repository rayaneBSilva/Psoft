package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import com.ufcg.psoft.commerce.service.estabelecimento.EstabelecimentoAlterarFilaDePedidosService;
import com.ufcg.psoft.commerce.service.notificacao.EstabelecimentoNotificaClientePedidoPronto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EstabelecimentoV1PrepararPedidoService implements EstabelecimentoPrepararPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    Validation validation;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EstabelecimentoAlterarFilaDePedidosService estabelecimentoAlterarFilaDePedidosService;

    @Autowired
    EstabelecimentoNotificaClientePedidoPronto estabelecimentoNotificaClientePedidoPronto;

    @Override
    public Pedido prepararPedido(Long pedidoId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Estabelecimento estabelecimento = validation.validaEstabelecimento(estabelecimentoId, estabelecimentoCodigoAcesso);
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaStatusDePedido(pedido, "Pedido em preparo");

        pedido.setStatusEntrega("Pedido pronto");
        estabelecimentoAlterarFilaDePedidosService.alterarFilaDePedidos(pedido, estabelecimento);
        estabelecimentoNotificaClientePedidoPronto.notifica(pedidoId, estabelecimentoId);

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        return pedidoRepository.save(pedido);
    }
}