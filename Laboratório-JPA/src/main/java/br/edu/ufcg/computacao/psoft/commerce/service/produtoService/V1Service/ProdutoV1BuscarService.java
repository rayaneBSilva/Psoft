package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository.ProdutoRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoBuscarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoV1BuscarService implements ProdutoBuscarService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public List<Produto> buscar() {
        return produtoRepository.buscarProdutos();
    }
}