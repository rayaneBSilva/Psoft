package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import com.ufcg.psoft.commerce.service.notificacao.EstabelecimentoV1NotificaClientePedidoIndisponivelParaEntrega;
import com.ufcg.psoft.commerce.service.notificacao.PedidoNotificaClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoV1AssociarPedidoEntregadorService implements EstabelecimentoAssociarPedidoEntregadorService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Validation validation;

    @Autowired
    PedidoNotificaClienteService pedidoNotificaClienteService;

    @Autowired
    EstabelecimentoV1NotificaClientePedidoIndisponivelParaEntrega estabelecimentoV1NotificaClientePedidoIndisponivelParaEntrega;

    // MELHORAR
    @Override
    public Pedido associarPedidoEntregador(Long pedidoId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        // Validações
        Estabelecimento estabelecimento = validation.validaEstabelecimento(estabelecimentoId, estabelecimentoCodigoAcesso);
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaStatusDePedido(pedido, "Pedido pronto");

        List<Entregador> entregadoresAprovados = estabelecimento.getEntregadoresAprovados();
        List<Pedido> pedidosPendentes = estabelecimento.getPedidosPendentes();

        // Obtenção de entregador disponível
        Entregador entregador = procurarEntregadorDisponivel(entregadoresAprovados);

        if(entregador != null){
            // Associação do pedido ao entregador
            pedido.setEntregadorId(entregador.getId());
            entregador.setDisponibilidade(false);
            estabelecimento.setEntregadoresAprovados(entregadoresAprovados);

            // Remoção do pedido da lista de pedidos pendentes
            validation.isPedidoPedente(pedido, pedidosPendentes);
            pedidosPendentes.remove(pedido);
            estabelecimento.setPedidosPendentes(pedidosPendentes);

            // Atualização do estado
            pedido.setStatusEntrega("Pedido em rota");

            // Salvamento das alterações
            estabelecimentoRepository.save(estabelecimento);
            modelMapper.map(pedidoPostPutRequestDTO, pedido);
            pedidoRepository.save(pedido);

            // Notificação ao cliente
            pedidoNotificaClienteService.notificar(pedidoId);
        } else{
                    estabelecimentoV1NotificaClientePedidoIndisponivelParaEntrega.notifica(pedidoId, estabelecimentoId);
        }

        return pedido;
    }

    private Entregador procurarEntregadorDisponivel(List<Entregador> entregadoresAprovados) {
        for (Entregador entregador : entregadoresAprovados) {
            if (entregador.isDisponibilidade()){
                return entregador;
            }
        }
        return null;

    }

}

