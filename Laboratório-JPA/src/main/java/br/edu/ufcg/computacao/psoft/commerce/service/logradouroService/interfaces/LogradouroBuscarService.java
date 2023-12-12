package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

import java.util.List;

@FunctionalInterface
public interface LogradouroBuscarService {
    public List<Logradouro> buscar();
}