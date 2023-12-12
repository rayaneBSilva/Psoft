package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces;


@FunctionalInterface
public interface ProdutoValidarCodigoService {
    Boolean validar(String codigo);

}
