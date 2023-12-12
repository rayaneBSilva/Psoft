package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteV1ExcluirPedidoService implements  ClienteExcluirPedidoService{

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private Validation validation;

    @Override
    public void excluir(Long pedidoId, Long clienteId, String codigoDeAcesso) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaCliente(clienteId, codigoDeAcesso);
        validation.validaCliente(pedido.getClienteId(), codigoDeAcesso);

        pedidoRepository.delete(pedido);
    }

    @Override
    public void excluir(Long clienteId, String codigoDeAcesso) {
        validation.validaCliente(clienteId, codigoDeAcesso);

        for(Pedido pedido: pedidoRepository.findAll()){
            if(pedido.getClienteId().equals(clienteId)){
                pedidoRepository.delete(pedido);
            }
        }
    }
}
