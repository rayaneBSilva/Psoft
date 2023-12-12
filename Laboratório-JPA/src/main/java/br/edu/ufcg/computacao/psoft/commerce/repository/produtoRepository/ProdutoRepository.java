package br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

import java.util.List;

public interface ProdutoRepository {
    public Produto criar(Produto produto);

    public Produto update(Long id, Produto produto);

    public Produto updateCodigoBarras(Long id, Produto produto);

    public Produto updateValor(Long id, Produto produto);

    public void excluir(Long id);

    public List<Produto> buscarProdutos();

    public Produto buscarProdutoPorId(Long id);
}