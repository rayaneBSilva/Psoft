package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.sabor.SaborGetRequestDTO;

import java.util.List;

public interface EstabelecimentoBuscarCardapioService {
     List<SaborGetRequestDTO> listar(String codigoAcessoEstabelecimento, Long idEstabelecimento);
     List<SaborGetRequestDTO> listar(String codigoDeAcesso, Long id, String tipo);
}
