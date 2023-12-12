package br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoValidarPrecoService;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1ValidarPrecoService implements ProdutoValidarPrecoService {

    @Override
    public boolean validarPreco(Double preco) {
            boolean status = true;
            if (preco <= 0 || preco == null) {
                status = false;
            }
            return status;
        }


}
