package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteV1BuscaPedidoService implements ClienteBuscaPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    Validation validation;

    @Override
    public Pedido clienteBusca(Long pedidoId, Long clienteId, String codigoAcessoCliente) {
        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaCliente(clienteId, codigoAcessoCliente);

        validation.validaCliente(pedido.getClienteId(), codigoAcessoCliente);

        return pedido;
    }

    @Override
    public List<Pedido> clienteBusca(Long clienteId, String codigoAcesso) {
        validation.validaCliente(clienteId, codigoAcesso);

        List<Pedido> pedidosEncontrados = new ArrayList<>();

        for(Pedido pedido: pedidoRepository.findAll()){
            if(pedido.getClienteId().equals(clienteId)){
                pedidosEncontrados.add(pedido);

            }
        }
        return pedidosEncontrados;
    }
}
