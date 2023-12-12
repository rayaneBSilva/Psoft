package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteV1ListarPedidoEstabelecimentoService implements ClienteListarPedidoEstabelecimentoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Validation validation;

    public List<Pedido> listar(Long pedidoId, Long clienteId, Long estabelecimentoId, String clienteCodigoAcesso, String status) {
        validation.validaCliente(clienteId, clienteCodigoAcesso);
        validation.validaEstabelecimento(estabelecimentoId, null);

        if (pedidoId != null) {
            Pedido pedido = validation.validaPedido(pedidoId);
            validation.validaPedidoClienteEstabelecimento(pedido, clienteId, estabelecimentoId);
            return List.of(pedido);
        }

        List<Pedido> pedidos = pedidoRepository.findAll().stream()
                .filter(pedido -> pedido.getEstabelecimentoId().equals(estabelecimentoId) && pedido.getClienteId().equals(clienteId))
                .collect(Collectors.toList());

        if (status != null) {
            pedidos = pedidos.stream()
                    .filter(pedido -> pedido.getStatusEntrega().equals(status))
                    .collect(Collectors.toList());
        }

        pedidos.sort(Comparator.comparing((Pedido pedido) -> {
            if (pedido.getStatusEntrega().equals("Pedido entregue")) {
                return 1;
            }
            return 0;
        }));

        return pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, Pedido.class))
                .collect(Collectors.toList());
    }
}
