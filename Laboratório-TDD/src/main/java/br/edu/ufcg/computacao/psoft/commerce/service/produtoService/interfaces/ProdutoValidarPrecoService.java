package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces;

@FunctionalInterface
public interface ProdutoValidarPrecoService {

    public boolean validarPreco(Double preco);
}
