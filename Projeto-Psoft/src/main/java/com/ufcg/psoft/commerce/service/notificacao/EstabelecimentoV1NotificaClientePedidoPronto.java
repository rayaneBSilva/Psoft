package com.ufcg.psoft.commerce.service.notificacao;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoV1NotificaClientePedidoPronto implements EstabelecimentoNotificaClientePedidoPronto {

    @Autowired
    Validation validation;

    @Autowired
    EmailService email;

    @Override
    public void notifica(Long pedidoId, Long estabelecimentoId) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaEstabelecimento(estabelecimentoId, null);
        validation.validaStatusDePedido(pedido, "Pedido pronto");

        Cliente cliente = validation.validaCliente(pedido.getClienteId(), null);
        String notificacao = "Prezado(a) " + cliente.getNome() + "\nGostaríamos de informar que o seu pedido " + pedido.getId() + " está pronto e em breve será atribuído a um entregador.";

        System.out.println(notificacao);

        if(cliente.getEmail() != null){
            email.sendEmail(cliente.getEmail(), "Pedido pronto - status do pedido", notificacao);
        }
    }
}


