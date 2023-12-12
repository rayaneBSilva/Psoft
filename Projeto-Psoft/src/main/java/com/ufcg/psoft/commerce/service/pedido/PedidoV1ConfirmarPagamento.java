package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PedidoV1ConfirmarPagamento implements PedidoConfirmaPagamentoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Validation validation;

    @Override
    public Pedido confirmarPagamento(Long clienteId, Long pedidoId, String metodoPagamento, String codigoAcessoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        validation.validaCliente(clienteId, codigoAcessoCliente);
        validation.isMeioPagamentoValido(metodoPagamento);

        Pedido pedido = validation.validaPedido(pedidoId);
        validation.validaStatusDePedido(pedido, "Pedido recebido");

        if (metodoPagamento.equals("PIX")) {
            pedido.setPreco(pedido.getPreco() * 0.95);
        } else if (metodoPagamento.equals("Cartão de débito")) {
            pedido.setPreco(pedido.getPreco() * 0.975);
        }

        pedido.setStatusPagamento(true);
        pedido.setStatusEntrega("Pedido em preparo");
        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        return pedidoRepository.save(pedido);
    }
}
