package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;


@FunctionalInterface
public interface PessoaAtualizarEmailService {


    public Pessoa updateEmail(Long id, PessoaPatchEmailDTO pessoaPatchEmailDTO);
}