package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;


import java.util.List;


@FunctionalInterface
public interface PessoaBuscarService {


    public List<Pessoa> buscar();

}