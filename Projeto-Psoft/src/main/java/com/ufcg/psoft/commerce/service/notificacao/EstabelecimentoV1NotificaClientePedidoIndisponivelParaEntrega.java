package com.ufcg.psoft.commerce.service.notificacao;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoV1NotificaClientePedidoIndisponivelParaEntrega implements EstabelecimentoNotificaClientePedidoIndisponivelParaEntrega {

    @Autowired
    Validation validation;

    @Autowired
    EmailService email;

    @Override
    public void notifica(Long pedidoId, Long estabelecimentoId) {
        Pedido pedido = validation.validaPedido(pedidoId);
        Cliente cliente = validation.validaCliente(pedido.getClienteId(),null);
        validation.validaEstabelecimento(estabelecimentoId, null);
        validation.validaStatusDePedido(pedido, "Pedido pronto");
        validation.validaDisponibilidadeEntregador(estabelecimentoId);

        String notificacao = "Olá, " + cliente.getNome() + "! Seu pedido nº" + pedido.getId() + " está pronto mas não há entregadores disponíveis.";
        System.out.println(notificacao);

        if(cliente.getEmail() != null) {
            email.sendEmail(cliente.getEmail(), "Não tem entregadores disponíveis para seu pedido", notificacao );
        }
    }
}

