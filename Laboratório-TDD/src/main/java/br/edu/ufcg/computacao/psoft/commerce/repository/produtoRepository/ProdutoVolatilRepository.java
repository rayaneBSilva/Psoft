package br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository;

import br.edu.ufcg.computacao.psoft.commerce.exception.ProdutoNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProdutoVolatilRepository implements ProdutoRepository{
    Map<Long, Produto> produtos;
    Long nextId;
    Produto produto;

    public ProdutoVolatilRepository() {
        produtos = new HashMap<>();
        nextId = 1L;
    }

    @Override
    public Produto criar(Produto produto) {
        produto.setId(nextId);
        produtos.put(nextId, produto);
        nextId++;
        return produto;
    }

    @Override
    public Produto update(Long id, Produto produto) {
        Produto produtoExistente = produtos.get(id);
        produto.setId(id);
        produtos.put(id, produto);
        return produto;
    }

    @Override
    public Produto updateCodigoBarras(Long id, Produto produto) {
        Produto produtoExistente = produtos.get(id);
        produto.setId(id);
        produto.setNomeProduto(produtoExistente.getNomeProduto());
        produto.setValor(produtoExistente.getValor());
        produto.setNomeFabricante(produtoExistente.getNomeFabricante());
        return produto;
    }

    @Override
    public Produto updateValor(Long id, Produto produto) {
        Produto produtoExistente = produtos.get(id);
        produto.setId(id);
        produto.setNomeProduto(produtoExistente.getNomeProduto());
        produto.setCodigoBarras(produtoExistente.getCodigoBarras());
        produto.setNomeFabricante(produtoExistente.getNomeFabricante());
        return produto;
    }

    @Override
    public void excluir(Long id) {
        Produto produto = produtos.get(id);
        if(produto == null){
            throw new ProdutoNaoExisteException();
        }else {
            produtos.remove(id);
        }
    }

    @Override
    public List<Produto> buscarProdutos() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public Produto buscarProdutoPorId(Long id) {
        Produto produto = produtos.get(id);
        if(produto == null){
            throw new ProdutoNaoExisteException();
        }
        return produto;
    }
}