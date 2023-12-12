package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository.ProdutoRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoExcluirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1ExcluirService implements ProdutoExcluirService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public void excluir(Long id) {
        produtoRepository.excluir(id);
    }
}