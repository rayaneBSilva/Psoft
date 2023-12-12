package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
@FunctionalInterface
public interface PessoaAtualizarService {
    public  Pessoa atualizar(Long id,PessoaPostPutDTO pessoaPostPutDTO);




}