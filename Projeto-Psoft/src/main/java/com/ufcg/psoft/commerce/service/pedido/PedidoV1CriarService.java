package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.model.Pizza;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoV1CriarService implements PedidoCriarService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    Validation validation;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public Pedido criar(Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO){
        Cliente cliente = validation.validaCliente(clienteId, clienteCodigoAcesso);
        Estabelecimento estabelecimento = validation.validaEstabelecimento(estabelecimentoId, null);
        validation.validaSaborDoPedido(pedidoPostPutRequestDTO);

        Double valorTotal = calculaValorTotalDoPedido(pedidoPostPutRequestDTO);

        if(pedidoPostPutRequestDTO.getEnderecoEntrega() == null || pedidoPostPutRequestDTO.getEnderecoEntrega().isBlank()){
            pedidoPostPutRequestDTO.setEnderecoEntrega(cliente.getEndereco());
        }

        Pedido pedido = modelMapper.map(pedidoPostPutRequestDTO, Pedido.class);

        pedido.setClienteId(clienteId);
        pedido.setEstabelecimentoId(estabelecimentoId);
        pedido.setPreco(valorTotal);

        List<Pedido> pedidosPedentes = estabelecimento.getPedidosPendentes();
        pedidosPedentes.add(pedido);
        estabelecimento.setPedidosPendentes(pedidosPedentes);

        estabelecimentoRepository.save(estabelecimento);
        return pedidoRepository.save(pedido);
    }

    private Double calculaValorTotalDoPedido(PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Double valorTotal = 0.0;
        for(Pizza pizza: pedidoPostPutRequestDTO.getPizzas()){
            pizza.calcularPreco();
            valorTotal += pizza.getPreco();
        }
        return valorTotal;
    }
}
