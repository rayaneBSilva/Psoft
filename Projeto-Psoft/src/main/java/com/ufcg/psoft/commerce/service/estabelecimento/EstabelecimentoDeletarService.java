package com.ufcg.psoft.commerce.service.estabelecimento;

@FunctionalInterface
public interface EstabelecimentoDeletarService {
   void excluir(String codigoDeAcesso, Long id);
}
