package com.ufcg.psoft.commerce.service.notificacao;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoV1NotificaEntregaAoEstabelecimento implements PedidoNotificaEntregaAoEstabelecimento {
    @Autowired
    Validation validation;

    @Autowired
    EmailService email;

    @Override
    public void notifica(Pedido pedido) {
        validation.validaStatusDePedido(pedido, "Pedido entregue");
        Cliente cliente = this.validation.validaCliente(pedido.getClienteId(), null);
        String notificacao = "O Pedido " + pedido.getId() + " do cliente: " + pedido.getClienteId() + " do estabelecimento: " + pedido.getEstabelecimentoId() + " foi entregue!";

        System.out.println(notificacao);

        if(cliente.getEmail() != null){
            email.sendEmail(cliente.getEmail(), "Pedido entregue - informações de entrega", notificacao);
        }

    }
}


