package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.model.Sabor;


@FunctionalInterface
public interface ClienteDemonstrarInteresseService {

    Sabor demonstrarInteresse(Long clienteid, String codigoAcesso, Long saborId) ;
}
