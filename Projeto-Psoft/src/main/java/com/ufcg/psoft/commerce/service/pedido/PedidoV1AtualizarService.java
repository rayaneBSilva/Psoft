package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoV1AtualizarService implements PedidoAtualizarService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Validation validation;

    @Override
    public Pedido atualizar(Long pedidoId, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoExisteException());
        validation.validaCliente(pedido.getClienteId(), codigoAcesso);

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        return pedidoRepository.save(pedido);
    }
}