package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchCodigoDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchValorDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPostPutDTO;


import br.edu.ufcg.computacao.psoft.commerce.exception.CodigoDeBarrasInvalidoException;
import br.edu.ufcg.computacao.psoft.commerce.exception.PessoaNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.exception.PrecoAbaixoIgualZeroException;
import br.edu.ufcg.computacao.psoft.commerce.exception.ProdutoNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produtoRepository.ProdutoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Testes da Entidade Produto")
@AutoConfigureMockMvc
class ProdutoV1RestControllerTest {

    @Autowired
    ProdutoRepository produtoRepository;

    ProdutoPatchCodigoDTO produtoPatchCodigoDTO;

    ProdutoPatchValorDTO produtoPatchValorDTO;

    @Autowired
    MockMvc driver;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    Produto produto;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Quando criar produto com dados válidos, então o produto é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoCriarProdutoComDadosValidos() throws Exception {
        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Biscoito recheadinho")
                .codigoBarras("7899137500100")
                .valor(4.50)
                .nomeFabricante("Bauducco")
                .build();

        String resultadoStr = driver.perform(MockMvcRequestBuilders.post("/v1/produto/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoPostPutDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Produto produtoResultado = objectMapper.readValue(resultadoStr, Produto.class);

        assertAll(
                () -> assertNotNull(produtoResultado.getId()),
                () -> assertTrue(produtoResultado.getId() > 0),
                () -> assertEquals(produtoPostPutDTO.getNomeProduto(), produtoResultado.getNomeProduto()),
                () -> assertEquals(produtoPostPutDTO.getCodigoBarras(), produtoResultado.getCodigoBarras()),
                () -> assertEquals(produtoPostPutDTO.getValor(), produtoResultado.getValor()),
                () -> assertEquals(produtoPostPutDTO.getNomeFabricante(), produtoResultado.getNomeFabricante())
        );
    }

    @Test
    @DisplayName("Quando atualizar produtos com dados válidos, então o produto é atualizado e retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarProdutoComDadosValidos() throws Exception {
        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Pão de Forma")
                .codigoBarras("7899137500100")
                .valor(3.00)
                .nomeFabricante("Ursinho")
                .build();

        Produto produtoAtualizado = produtoRepository.criar(modelMapper.map(produtoPostPutDTO, Produto.class));

        //Atualizando produto
        produtoPostPutDTO.setNomeProduto("Pão bisnaguinha");
        produtoPostPutDTO.setCodigoBarras("7899137500100");
        produtoPostPutDTO.setValor(4.75);
        produtoPostPutDTO.setNomeFabricante("Forno Nobre");


        //ACT
        String resultadoStr = driver.perform(MockMvcRequestBuilders.put("/v1/produto/" + produtoAtualizado.getId() + "/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(produtoPostPutDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        Produto produtoResultado = objectMapper.readValue(resultadoStr, Produto.class);

        // asserts
        assertAll(
                () -> assertNotNull(produtoResultado.getId()),
                () -> assertTrue(produtoResultado.getId() > 0),

                () -> assertNotNull(produtoResultado.getNomeProduto()),
                () -> assertEquals(produtoPostPutDTO.getNomeProduto(), produtoPostPutDTO.getNomeProduto()),

                () -> assertNotNull(produtoResultado.getCodigoBarras()),
                () -> assertEquals(produtoPostPutDTO.getCodigoBarras(), produtoPostPutDTO.getCodigoBarras()),

                () -> assertNotNull(produtoResultado.getValor()),
                () -> assertEquals(produtoPostPutDTO.getValor(), produtoPostPutDTO.getValor()),

                () -> assertNotNull(produtoResultado.getNomeFabricante()),
                () -> assertEquals(produtoPostPutDTO.getNomeFabricante(), produtoPostPutDTO.getNomeFabricante())

        );


    }


    @Test
    @DisplayName("Quando atualizar o código de barras com dados válidos, então o produto atualizado é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarCodigoDeBarrasProdutoComDadosValidos() throws Exception {
        // Arrange
        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Pão de Forma")
                .codigoBarras("7899137500100")
                .valor(3.00)
                .nomeFabricante("Ursinho")
                .build();


        Produto produtoBase = produtoRepository.criar(modelMapper.map(produtoPostPutDTO, Produto.class));

        // Act


        produtoPatchCodigoDTO = produtoPatchCodigoDTO.builder()
                .codigoBarras("7899137500100")
                .build();


        //ACT
        String resultadoStr = driver.perform(MockMvcRequestBuilders.patch("/v1/produto/" + produtoBase.getId() + "/codigo-de-barra")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(produtoPostPutDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        Produto produtoResultado = objectMapper.readValue(resultadoStr, Produto.class);


        // Assert
        assertAll(
                () -> assertNotNull(produtoResultado.getId()),
                () -> assertNotNull(produtoResultado.getCodigoBarras()),
                () -> assertEquals(
                        produtoPatchCodigoDTO.getCodigoBarras(),
                        produtoResultado.getCodigoBarras())
        );


    }

    @Test
    @DisplayName("Quando atualizar o valor com dados válidos, então o produto atualizado é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarValorProdutoComDadosValidos() throws Exception {
        // Arrange
        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Pão de Forma")
                .codigoBarras("7899137500100")
                .valor(3.00)
                .nomeFabricante("Ursinho")
                .build();


        Produto produtoBase = produtoRepository.criar(modelMapper.map(produtoPostPutDTO, Produto.class));

        // Act


        produtoPatchValorDTO = produtoPatchValorDTO.builder()
                .valor(5.00)
                .build();


        //ACT
        String resultadoStr = driver.perform(MockMvcRequestBuilders.patch("/v1/produto/" + produtoBase.getId() + "/valor")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(produtoPatchValorDTO)))  // Usar produtoPatchValorDTO aqui
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        Produto produtoResultado = objectMapper.readValue(resultadoStr, Produto.class);


        // Assert
        assertAll(
                () -> assertNotNull(produtoResultado.getId()),
                () -> assertNotNull(produtoResultado.getValor()),
                () -> assertEquals(
                        produtoPatchValorDTO.getValor(),
                        produtoResultado.getValor())
        );


    }

    @Test
    @DisplayName("Quando excluir produto por id com dados válidos, Entao nada é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoExcluirProdutoPorIdComDadosValidos() throws Exception {


        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Notebook lenovo ideapeding gaming i5")
                .codigoBarras("7899137500100")
                .valor(4200.00)
                .nomeFabricante("Lenovo")
                .build();


        Produto produtoBase = produtoRepository.criar(modelMapper.map(produtoPostPutDTO, Produto.class));


        String resultadoStr = driver.perform(MockMvcRequestBuilders.delete("/v1/produto/" + produtoBase.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent()) // 204
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        assertAll(
                () -> assertTrue(resultadoStr.isBlank())
        );
    }

    @Test
    @DisplayName("Quando busca todos os produtos com Id validos Entao todos os produtos cadastradas sao retornados")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoBuscoVariosProdutos() throws Exception {


        //Arrage
        Produto produtoUm = Produto.builder()
                .nomeProduto("Pelúcia Mang BT21")
                .codigoBarras("7899137500103")
                .valor(250.00)
                .nomeFabricante("BT21")
                .build();

        Produto produtoDois = Produto.builder()
                .nomeProduto("Abajur de estrelas")
                .codigoBarras("7899137500210")
                .valor(80.00)
                .nomeFabricante("ShineB")
                .build();

        Produto produtoTres = Produto.builder()
                .nomeProduto("Cobertor")
                .codigoBarras("7899137500327")
                .valor(75.00)
                .nomeFabricante("PurePlumes")
                .build();


        produtoUm = produtoRepository.criar(produtoUm);
        produtoDois = produtoRepository.criar(produtoDois);
        produtoTres = produtoRepository.criar(produtoTres);


        String resultadoStr = driver.perform(MockMvcRequestBuilders.get("/v1/produto")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        List<Produto> produtos = objectMapper.readValue(resultadoStr, new TypeReference<List<Produto>>() {
        });


        Produto finalProdutoUm = produtoUm;
        Produto finalProdutoDois = produtoDois;
        Produto finalProdutoTres = produtoTres;


        assertAll(
                () -> assertTrue(!produtos.isEmpty()),
                () -> assertTrue(produtos.contains(finalProdutoUm)),
                () -> assertTrue(produtos.contains(finalProdutoDois)),
                () -> assertTrue(produtos.contains(finalProdutoTres))


        );
    }

    @Test
    @DisplayName("Quando busca um produto com ID válido, então esse produto é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoBuscoProdutoPorId() throws Exception {
        // Arrange
        Produto produtoUm = Produto.builder()
                .nomeProduto("Baton")
                .codigoBarras("7899137500103")
                .valor(2.00)
                .nomeFabricante("Garoto")
                .build();

        produtoUm = produtoRepository.criar(produtoUm);

        // Act & Assert
        String resultadoStr = driver.perform(MockMvcRequestBuilders.get("/v1/produto/" + produtoUm.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Produto produtoRetornado = objectMapper.readValue(resultadoStr, Produto.class);

        assertEquals(produtoUm, produtoRetornado);
    }

    @Test
    @DisplayName("Quando criar produto com código de barras inválido, então deve retornar CodigoDeBarrasInvalidoException")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoCriarProdutoComCodigoInvalido() throws Exception {
        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Biscoito recheadinho")
                .codigoBarras("222222222")
                .valor(4.50)
                .nomeFabricante("Bauducco")
                .build();

        // Act & Assert
        driver.perform(MockMvcRequestBuilders.post("/v1/produto/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoPostPutDTO)))
                .andExpect(status().isBadRequest()) // Codigo 400
                .andDo(print())
                .andExpect(result -> {
                    Throwable throwable = result.getResolvedException();
                    assertNotNull(throwable);
                    assertTrue(throwable instanceof CodigoDeBarrasInvalidoException);
                    assertEquals("O codigo de Barra está inválido!", throwable.getMessage());
                });
    }

    @Test
    @DisplayName("Quando criar produto com preco abaixo de zero, então deve retornar PrecoAbaixoOuIgualZeroException")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoCriarProdutoComPrecoInvalido() throws Exception {
        ProdutoPostPutDTO produtoPostPutDTO = ProdutoPostPutDTO.builder()
                .nomeProduto("Biscoito recheadinho")
                .codigoBarras("7899137500100")
                .valor(0)
                .nomeFabricante("Bauducco")
                .build();

        // Act & Assert
        driver.perform(MockMvcRequestBuilders.post("/v1/produto/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoPostPutDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    Throwable throwable = result.getResolvedException();
                    assertNotNull(throwable);
                    assertTrue(throwable instanceof PrecoAbaixoIgualZeroException);
                    assertEquals("O preço está abaixo ou igual a zero!", throwable.getMessage());
                });
    }


}