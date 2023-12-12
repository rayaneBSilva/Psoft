package com.ufcg.psoft.commerce.service.sabor;

@FunctionalInterface
public interface SaborDeletarService {
    void excluir(String codigoDeAcesso, Long id, Long estabelecimentoId);
}
