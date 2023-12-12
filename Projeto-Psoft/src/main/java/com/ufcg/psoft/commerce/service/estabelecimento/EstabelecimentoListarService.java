package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.model.Estabelecimento;

import java.util.Collection;

public interface EstabelecimentoListarService {
     Collection<Estabelecimento> listar();
     Estabelecimento listar(String codigoDeAcesso, Long id);
}
