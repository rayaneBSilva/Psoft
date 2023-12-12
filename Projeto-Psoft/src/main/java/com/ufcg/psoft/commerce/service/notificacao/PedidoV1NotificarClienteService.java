package com.ufcg.psoft.commerce.service.notificacao;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoV1NotificarClienteService implements PedidoNotificaClienteService {

    @Autowired
    Validation validation;

    @Autowired
    EmailService email;

    @Override
    public void notificar(Long pedidoId) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaStatusDePedido(pedido, "Pedido em rota");
        Cliente cliente = validation.validaCliente(pedido.getClienteId(), null);
        Entregador entregador = validation.validaEntregador(pedido.getEntregadorId(), null);
        Estabelecimento estabelecimento = validation.validaEstabelecimento(pedido.getEstabelecimentoId(), null);

        String notificacao = "Prezado(a) " + cliente.getNome() + ", o seu pedido está a caminho!\n"
                + "Entregador: " + entregador.getNome() + "\n"
                + "Tipo do Veículo: " + entregador.getTipoVeiculo() + "\n"
                + "Placa do Veículo: " + entregador.getPlacaVeiculo() + "\n"
                + "Cor do Veículo: " + entregador.getCorVeiculo();
        System.out.println(notificacao);

        if(cliente.getEmail() != null){
            email.sendEmail(cliente.getEmail(), "Pedido em Rota - Informações de Entrega", notificacao);
        }
    }
}