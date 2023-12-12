package br.edu.ufcg.computacao.psoft.commerce.service;

import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service.ProdutoV1ValidarPrecoService;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoValidarPrecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoV1ValidarPrecoServiceTest {

    private ProdutoValidarPrecoService driver;

    @BeforeEach
    void setUp() {
        driver = new ProdutoV1ValidarPrecoService();
    }

    @Test
    void validarPreco() {
        assertEquals(driver.validarPreco(4.50), true);
    }
}