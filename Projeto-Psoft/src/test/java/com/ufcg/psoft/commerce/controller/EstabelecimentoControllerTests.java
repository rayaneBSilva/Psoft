package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.estabelecimento.EstabelecimentoBuscarCardapioService;
import com.ufcg.psoft.commerce.service.sabor.SaborCriarService;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de estabelecimentos")
public class EstabelecimentoControllerTests {

    @Autowired
    MockMvc driver;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SaborCriarService saborCriarService;

    @Autowired
    EstabelecimentoBuscarCardapioService estabelecimentoBuscarCardapioService;
    ObjectMapper objectMapper = new ObjectMapper();

    Estabelecimento estabelecimento;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("123456")
                .build());
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class EstabelecimentoVerificacaoFluxosBasicosApiRest {
        final String URI_ESTABELECIMENTOS = "/estabelecimentos";
        EstabelecimentoPostPutRequestDTO estabelecimentoPutRequestDTO;
        EstabelecimentoPostPutRequestDTO estabelecimentoPostRequestDTO;

        @BeforeEach
        void setup() {
            estabelecimentoPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("123456")
                    .build();
            estabelecimentoPostRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("654321")
                    .build();
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados válidos")
        void quandoCriarEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Estabelecimento resultado = objectMapper.readValue(responseJsonString, Estabelecimento.class);

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(estabelecimentoPostRequestDTO.getCodigoAcesso(), resultado.getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados inválidos (vazio)")
        void quandoCriarEstabelecimentoComCodigoVazio() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostRequestDTOInvalido = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("") // Código vazio
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTOInvalido)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            //Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", customErrorType.getMessage()),
                    () -> assertEquals(2, customErrorType.getErrors().size()),
                    () -> assertTrue(customErrorType.getErrors().stream().anyMatch((msg) -> msg.contains("Codigo de acesso Obrigatorio!"))),
                    () -> assertTrue(customErrorType.getErrors().stream().anyMatch((msg) -> msg.contains("Codigo de acesso deve ter exatamente 6 digitos numericos")))
            );
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados inválidos (nulo)")
        void quandoCriarEstabelecimentoComCodigoNulo() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostRequestDTOInvalido = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso(null)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTOInvalido)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            //Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", customErrorType.getMessage()),
                    () -> assertEquals(1, customErrorType.getErrors().size()),
                    () -> assertTrue(customErrorType.getErrors().stream().anyMatch((msg) -> msg.contains("Codigo de acesso Obrigatorio!")))
            );
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados inválidos")
        void quandoCriarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostPutRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando excluímos um estabelecimento salvo")
        void quandoExcluimosEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um estabelecimento inexistente")
        void quandoExcluimosEstabelecimentoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ESTABELECIMENTOS + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }


        @Test
        @DisplayName("Quando atualizamos um estabelecimento salvo")
        void quandoAtualizamosEstabelecimentoValido() throws Exception {
            // Arrange
            estabelecimentoPutRequestDTO.setCodigoAcesso("131289");

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Estabelecimento resultado = objectMapper.readValue(responseJsonString, Estabelecimento.class);

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), estabelecimento.getId().longValue()),
                    () -> assertEquals("131289", resultado.getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando alteramos um estabelecimento com codigo de acesso inválido")
        void quandoAlterarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do estabelecimento mais de 6 digitos")
        void quandoAlteramosCodigoAcessoDoEstabelecimentoMaisDe6Digitos() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("1234567")
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do estabelecimento caracteres não numéricos")
        void quandoAlteramosCodigoAcessoDoEstabelecimentoCaracteresNaoNumericos() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("a*c4e@")
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos um estabelecimento com codigo de acesso inválido (nulo)")
        void quandoAlterarEstabelecimentoInvalidoNulo() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso(null)
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(1, resultado.getErrors().size()),
                    () -> assertEquals("Codigo de acesso Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando buscamos por todos os estabelecimentos salvos")
        void quandoBuscamosPorTodosEstabelecimentosSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 estabelecimentos no banco
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("246810")
                    .build();
            Estabelecimento estabelecimento2 = Estabelecimento.builder()
                    .codigoAcesso("135790")
                    .build();

            estabelecimentoRepository.saveAll(Arrays.asList(estabelecimento1, estabelecimento2));

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Estabelecimento> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size()),
                    () -> assertTrue(resultado.contains(estabelecimento1)),
                    () -> assertTrue(resultado.contains(estabelecimento2)),
                    () -> assertTrue(resultado.contains(estabelecimento))
            );
        }


        @Test
        @DisplayName("Quando buscamos um estabelecimento salvo pelo id")
        void quandoBuscamosPorUmEstabelecimentoSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Estabelecimento resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(estabelecimento.getCodigoAcesso(), resultado.getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando buscamos um estabelecimento inexistente")
        void quandoBuscamosPorUmEstebelecimentoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos um estabelecimento com codigo invalido")
        void quandoBuscamosPorUmEstebelecimentoComCodigoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "111111")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento")
        void quandoBuscarCardapioEstabelecimento() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(4, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento que não existe")
        void quandoBuscarCardapioEstabelecimentoInexistente() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento com codigo de acesso invalido")
        void quandoBuscarCardapioEstabelecimentoComCodigoInvalido() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo (salgado)")
        void quandoBuscarCardapioEstabelecimentoPorTipo() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "salgado")
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio (por tipo) de um estabelecimento que não existe")
        void quandoBuscarCardapioEstabelecimentoPorTipoComEstabelecimentoInexistente() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "salgado")
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento com codigo de acesso invalido")
        void quandoBuscarCardapioEstabelecimentoPorTipoComCodigoInvalido() throws Exception {
            // Arrange
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "salgado")
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo (doce)")
        void quandoBuscarCardapioEstabelecimentoPorTipoDoce() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "doce")
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }


        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo invalido")
        void quandoBuscarCardapioEstabelecimentoPorTipoInvalido() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "invalido")
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio (por tipo Doce) de um estabelecimento que não existe")
        void quandoBuscarCardapioEstabelecimentoPorTipoDoceComEstabelecimentoInexistente() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "doce")
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio (por tipo doce) de um estabelecimento com codigo de acesso invalido")
        void quandoBuscarCardapioEstabelecimentoPorTipoDoceComCodigoInvalido() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "doce")
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }
        @Test
        @DisplayName("Quando solicitamos a listagem dos sabores do cardápio por disponibilidade")
        void quandoListarSaboresDoCardapioPorDisponibilidade() throws Exception {
            // Arrange - Crie sabores fictícios e associe-os ao estabelecimento
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .disponivel(true)
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .disponivel(false)
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .disponivel(false)
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .disponivel(true)
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" )
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "doce")
                            .param("codigoAcesso", "123456")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(4, resultado.size())
            );
        }

        @Test
        public void testHashCode() {

            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .disponivel(true)
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1))
                    .build();
            Estabelecimento estabelecimento2 = Estabelecimento.builder()
                    .codigoAcesso("111111")
                    .sabores(Set.of(sabor1))
                    .build();

            assertEquals(estabelecimento1.hashCode(), estabelecimento1.hashCode());
            assertNotEquals(estabelecimento1.hashCode(), estabelecimento2.hashCode());
        }
    }
}