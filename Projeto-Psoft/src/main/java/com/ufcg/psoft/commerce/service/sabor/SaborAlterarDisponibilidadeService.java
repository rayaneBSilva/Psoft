package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.model.Sabor;

@FunctionalInterface
public interface SaborAlterarDisponibilidadeService {
    Sabor alterarDisponibilidade(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, Boolean disponibilidade);

}
