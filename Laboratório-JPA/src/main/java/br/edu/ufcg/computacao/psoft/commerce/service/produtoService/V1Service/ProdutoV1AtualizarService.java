package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service;


import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository.ProdutoRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoAtualizarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProdutoV1AtualizarService implements ProdutoAtualizarService {


    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public Produto atualizar(Long id, ProdutoPostPutDTO produtoPostPutDTO) {
        return produtoRepository.update(
                id,
                Produto.builder()
                        .nomeProduto(produtoPostPutDTO.getNomeProduto())
                        .valor(produtoPostPutDTO.getValor())
                        .codigoBarras((produtoPostPutDTO.getCodigoBarras()))
                        .nomeFabricante(produtoPostPutDTO.getNomeFabricante())
                        .build()
        );
    }
}

