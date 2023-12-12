package br.edu.ufcg.computacao.psoft.commerce.service;

import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.V1Service.ProdutoV1ValidarCodigoService;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.ProdutoValidarCodigoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoV1ValidarCodigoServiceTest {

    ProdutoValidarCodigoService driver;

    @BeforeEach
    void setUp() {
        driver = new ProdutoV1ValidarCodigoService();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Validação de código de barras")
    void validarCodigo() {
        assertEquals(driver.validar("7899137500100"), true);
    }

    @Test
    @DisplayName("Validação de código de barras inválido")
    void validarCodigoInvalido() {
        assertEquals(driver.validar("7199137500105"), false);
    }

    @Test
    @DisplayName("Validação de código de barras nulo")
    void validarCodigoNulo() {
        assertEquals(driver.validar(null), false);
    }

    @Test
    @DisplayName("Validação de código de barras com menos de 13 dígitos")
    void validarCodigoMenosDe13Digitos() {
        assertEquals(driver.validar("78991375010"), false);
    }

    @Test
    @DisplayName("Validação de código de barras com mais de 13 dígitos")
    void validarCodigoMaisDe13Digitos() {
        assertEquals(driver.validar("78991375001045"), false);
    }

    @Test
    @DisplayName("Validação de código de barras com dígito verificador inválido")
    void validarCodigoDigitoVerificadorInvalido() {
        assertEquals(driver.validar("7899137500105"), false);
    }



}