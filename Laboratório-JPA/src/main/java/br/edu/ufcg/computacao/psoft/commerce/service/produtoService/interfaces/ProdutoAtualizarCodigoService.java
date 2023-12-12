package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchCodigoDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

@FunctionalInterface
public interface ProdutoAtualizarCodigoService {

    Produto updateCodigoBarras(Long id, ProdutoPatchCodigoDTO produtoPatchCodigo);
}