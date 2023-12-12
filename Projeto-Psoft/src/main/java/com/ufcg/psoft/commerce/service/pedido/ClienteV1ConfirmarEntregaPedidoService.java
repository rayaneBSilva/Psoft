package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import com.ufcg.psoft.commerce.service.notificacao.PedidoNotificaEntregaAoEstabelecimento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteV1ConfirmarEntregaPedidoService implements ClienteConfirmarEntregaPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoNotificaEntregaAoEstabelecimento pedidoNotificaEntregaAoEstabelecimento;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    Validation validation;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public Pedido clienteConfirmaEntrega(Long pedidoId, Long clienteId, String clienteCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaCliente(clienteId, clienteCodigoAcesso);
        validation.validaStatusDePedido(pedido, "Pedido em rota");

        adicionaEntregadorNaListaDeEntregadoresAprovados(pedido.getEntregadorId(), pedido.getEstabelecimentoId());

        pedido.setStatusEntrega("Pedido entregue");
        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedidoRepository.save(pedido);
        pedidoNotificaEntregaAoEstabelecimento.notifica(pedido);
        return pedido;
    }

    private void adicionaEntregadorNaListaDeEntregadoresAprovados(Long entregadorId, Long estabelecimentoId) {
        Entregador entregador = validation.validaEntregador(entregadorId, null);
        Estabelecimento estabelecimento = validation.validaEstabelecimento(estabelecimentoId, null);

        if (entregador.isDisponibilidade()) {
            List<Entregador> entregadoresAprovados = estabelecimento.getEntregadoresAprovados();
            entregadoresAprovados.add(entregador);
            estabelecimento.setEntregadoresAprovados(entregadoresAprovados);
            estabelecimentoRepository.save(estabelecimento);
        }
    }
}
