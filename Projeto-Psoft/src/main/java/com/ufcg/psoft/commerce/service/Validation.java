package com.ufcg.psoft.commerce.service;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Validation {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    public Cliente validaCliente(Long clienteId, String clienteCodigoAcesso){
        validarId(clienteId);
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNaoExisteException());

        if(clienteCodigoAcesso != null) {
            if (!cliente.getCodigoAcesso().equals(clienteCodigoAcesso)) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }

        return cliente;
    }

    public Estabelecimento validaEstabelecimento(Long estabelecimentoId, String estabelecimentoCodigoAcesso){
        validarId(estabelecimentoId);
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoExisteException());

        if(estabelecimentoCodigoAcesso != null) {
            if (!estabelecimento.getCodigoAcesso().equals(estabelecimentoCodigoAcesso)) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }

        return estabelecimento;
    }

    public void validaDisponibilidadeEntregador(Long estabelecimentoId){
        Estabelecimento estabelecimento = validaEstabelecimento(estabelecimentoId, null);

        for(Entregador entregador: estabelecimento.getEntregadoresAprovados()){
            if(entregador.isDisponibilidade()){
                throw new EstabelecimentoEntregadorDisponivelException();
            }
        }
    }

    public Entregador validaEntregador(Long entregadorId, String entregadorCodigoAcesso){
        validarId(entregadorId);
        Entregador entregador = entregadorRepository.findById(entregadorId).orElseThrow(() -> new EntregadorNaoExisteException());

        if(entregadorCodigoAcesso != null) {
            if (!entregador.getCodigoAcesso().equals(entregadorCodigoAcesso)) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }

        return entregador;
    }

    public void validaPedidoClienteEstabelecimento(Pedido pedido, Long clienteId, Long estabelecimentoId) {
        if (!pedido.getClienteId().equals(clienteId) || !pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new PedidoNaoExisteException();
        }
    }

    public Pedido validaPedido(Long pedidoId){
        validarId(pedidoId);
        return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoExisteException());
    }

    public void validaSaborDoPedido(PedidoPostPutRequestDTO pedidoPostPutRequestDTO){
        for(Pizza pizza: pedidoPostPutRequestDTO.getPizzas()){
            String tamanhoPizza = pizza.getTamanho();

            if(tamanhoPizza.equals("media") && pizza.getSabor1() != null){

                saborRepository.findById(pizza.getSabor1().getId()).orElseThrow(() -> new SaborNaoExisteException());

            } else if (tamanhoPizza.equals("grande") && (pizza.getSabor1() != null || pizza.getSabor2() != null)) {
                if(pizza.getSabor1() != null){
                    saborRepository.findById(pizza.getSabor1().getId()).orElseThrow(() -> new SaborNaoExisteException());
                }
                if(pizza.getSabor2() != null){
                    saborRepository.findById(pizza.getSabor2().getId()).orElseThrow(() -> new SaborNaoExisteException());
                }
            } else {
                throw new SaborNaoExisteException();
            }
        }
    }


    private void validarId(Long id){
        if(id == null || id <= 0){
            throw new IdNaoExisteException();
        }
    }


    public void isMeioPagamentoValido(String tipoPagamento){
        if(tipoPagamento.isBlank() || !(tipoPagamento.equals("PIX") || tipoPagamento.equals("Cartão de débito") || tipoPagamento.equals("Cartão de crédito"))){
            throw new MeioDePagamentoInvalidoException();
        }
    }

    public void validaStatusDePedido(Pedido pedido, String statusDeEntrega) {
        if(!pedido.getStatusEntrega().equals(statusDeEntrega)){
            throw new PedidoStatusDeEntregaInvalidoException();
        }
    }

    public void isPedidoPedente(Pedido pedido, List<Pedido> pedidosPedentes) {
        if(!pedidosPedentes.contains(pedido)){
            throw new PedidoNaoEstaPedenteException();
        }
    }
}
