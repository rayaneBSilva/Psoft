package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;


import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;




@FunctionalInterface
public interface PessoaCriarService {
    public Pessoa criar(PessoaPostPutDTO pessoaPostPutDTO);
}