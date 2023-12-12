package com.ufcg.psoft.commerce.service.pedido;


import com.ufcg.psoft.commerce.exception.CancelamentoDePedidoInvalidoException;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteV1CancelarPedidoService implements  ClienteCancelarPedidoService {

    @Autowired
    Validation validation;

    @Autowired
    PedidoRepository pedidoRepository;

    @Override
    public void cancelar(Long pedidoId, String codigoAcesso) {
        Pedido pedido = this.validation.validaPedido(pedidoId);
        this.validation.validaCliente(pedido.getClienteId(),codigoAcesso );

        if (pedido.getStatusEntrega().equals("Pedido pronto") || pedido.getStatusEntrega().equals("Pedido em rota")) {
            throw new CancelamentoDePedidoInvalidoException();
        } else {
            pedidoRepository.deleteById(pedidoId);
        }
    }
}
