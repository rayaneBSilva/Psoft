package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorV1AtualizarService implements EntregadorAtualizarService{
    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    Validation validation;
    @Override
    public Entregador atualizar(String codigoAcesso, Long id, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = validation.validaEntregador(id, codigoAcesso);

        entregador.setCodigoAcesso(codigoAcesso);
        entregador.setNome(entregadorPostPutRequestDTO.getNome());
        entregador.setCorVeiculo(entregadorPostPutRequestDTO.getCorVeiculo());
        entregador.setTipoVeiculo(entregadorPostPutRequestDTO.getTipoVeiculo());
        entregador.setPlacaVeiculo(entregadorPostPutRequestDTO.getPlacaVeiculo());
        entregadorRepository.flush();
        return entregador;
    }
}
