package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;


@FunctionalInterface
public interface PessoaBuscarPorIdService {
    public Pessoa buscarPessoaPorId(Long id);
}