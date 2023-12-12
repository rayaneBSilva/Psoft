package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.model.Associacao;

@FunctionalInterface
public interface AssociacaoAprovarService {
    Associacao atualizar(String codigoAcesso, Long idEstabelecimento, Long idEntregador);
}