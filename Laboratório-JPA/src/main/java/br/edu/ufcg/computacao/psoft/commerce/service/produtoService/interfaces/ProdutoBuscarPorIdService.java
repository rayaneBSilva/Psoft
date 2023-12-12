package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces;

import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

import java.util.List;

@FunctionalInterface
public interface ProdutoBuscarPorIdService {

    public Produto buscarProdutoPorId(Long id);
}