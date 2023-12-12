package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchCodigoDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository.ProdutoRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoAtualizarCodigoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1AtualizarCodigoService implements ProdutoAtualizarCodigoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Produto updateCodigoBarras(Long id, ProdutoPatchCodigoDTO produtoPatchCodigo) {
        String codigoBarras = produtoPatchCodigo.getCodigoBarras();
        if (!validarCodigoBarra(codigoBarras)) {
            throw new IllegalArgumentException("Código de barras inválido");
        }
        return produtoRepository.updateCodigoBarras(
                id,
                modelMapper.map(produtoPatchCodigo, Produto.class)

        );

    }
    public Boolean validarCodigoBarra(String codigo) {
        if (codigo == null || !codigo.substring(0, 8).equals("78991375") || codigo.length() != 13) {
            return false;
        }
        int[] numeros = codigo.chars().map(Character::getNumericValue).toArray();
        int somaPares = numeros[1] + numeros[3] + numeros[5] + numeros[7] + numeros[9] + numeros[11];
        int somaImpares = numeros[0] + numeros[2] + numeros[4] + numeros[6] + numeros[8] + numeros[10];
        int resultado = somaImpares + somaPares * 3;
        int digitoVerificador = (10 - resultado % 10) % 10;
        return digitoVerificador == numeros[12];
    }

}