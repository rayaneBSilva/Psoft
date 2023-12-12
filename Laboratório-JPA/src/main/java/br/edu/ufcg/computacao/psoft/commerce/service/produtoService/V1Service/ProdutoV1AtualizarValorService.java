package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchValorDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository.ProdutoRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoAtualizarValorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1AtualizarValorService implements ProdutoAtualizarValorService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Produto updateValor(Long id, ProdutoPatchValorDTO produtoPatchValorDTO) {
        Double valor = produtoPatchValorDTO.getValor();
        if (validarPreco(valor));
        return produtoRepository.updateValor(
                id,
                modelMapper.map(produtoPatchValorDTO, Produto.class)

        );
    }
    public Boolean validarPreco(Double preco) {
        if (preco == null || preco <= 0) {
            return false;
        }else
            return true;
    }
}