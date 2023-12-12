package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces;


import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchValorDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import jakarta.validation.Valid;

@FunctionalInterface
public interface ProdutoAtualizarValorService {

    Produto updateValor(Long id, @Valid ProdutoPatchValorDTO produtoPatchCodigo);
}