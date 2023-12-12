package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.Validation;
import com.ufcg.psoft.commerce.service.estabelecimento.EstabelecimentoAlterarFilaDeEntregadoresService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorV1DefinirDisponibilidadeService implements EntregadorDefinirDisponibilidadeService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoAlterarFilaDeEntregadoresService estabelecimentoAlterarFilaDeEntregadoresService;

    @Autowired
    Validation validation;


    // AJEITAR
    public Entregador definirDisponibilidade(Long entregadorId, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcessoEntregador, boolean disponibilidade) {
        Entregador entregador = validation.validaEntregador(entregadorId, codigoAcessoEntregador);

        entregador.setDisponibilidade(disponibilidade);

        entregadorRepository.save(entregador);
        if(disponibilidade){
            entregador.setStatusEntregador("Ativo");
            estabelecimentoAlterarFilaDeEntregadoresService.alterarFilaDeEntregadores(entregadorId, codigoAcessoEntregador);
        } else {
            entregador.setStatusEntregador("Descanso");
        }

        modelMapper.map(entregadorPostPutRequestDTO, entregador);

        entregadorRepository.save(entregador);
        return entregador;
    }

}
