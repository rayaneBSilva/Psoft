package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPatchTipoNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

@FunctionalInterface
public interface LogradouroAtualizarTipoNomeService {
    public Logradouro atualizarNomeTipo(Long id, LogradouroPatchTipoNomeDTO logradouroPatchTipoNomeDTO);
}