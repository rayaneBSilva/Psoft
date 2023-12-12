package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;


@FunctionalInterface
public interface ProdutoAtualizarService {
    public Produto atualizar(Long id, ProdutoPostPutDTO produtoPostPutDTO);
}
