package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.model.Associacao;

@FunctionalInterface
public interface AssociacaoCriarService {

    Associacao criar(Long idEntregador, String codigoAcessoEntregador, Long idEstabelecimento);
}