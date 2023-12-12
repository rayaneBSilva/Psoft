package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoV1AlterarFilaDeEntregadoresService implements EstabelecimentoAlterarFilaDeEntregadoresService{

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private Validation validation;

    @Override
    public void alterarFilaDeEntregadores(Long entregadorId, String codigoAcessoEntregador) {
        Entregador entregador = validation.validaEntregador(entregadorId, codigoAcessoEntregador);

        for(Estabelecimento estabelecimento: estabelecimentoRepository.findAll()){
            if(estabelecimento.getEntregadoresAprovados().contains(entregador)){
                List<Entregador> entregadoresAprovados = estabelecimento.getEntregadoresAprovados();
                entregadoresAprovados.remove(entregador);
                entregadoresAprovados.add(entregador); // Adiciona no final da fila
                estabelecimento.setEntregadoresAprovados(entregadoresAprovados);
                estabelecimentoRepository.save(estabelecimento);
            }
        }
    }
}
