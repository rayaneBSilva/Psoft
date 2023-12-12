package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Entregadores")
public class EntregadorControllerTests {

    final String URI_ENTREGADORES = "/entregadores";

    @Autowired
    MockMvc driver;

    @Autowired
    EntregadorRepository entregadorRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Entregador entregador;

    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;

    EntregadorGetRequestDTO entregadorDTO;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Lana Del Rey")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("moto")
                .codigoAcesso("123456")
                .build()
        );
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome(entregador.getNome())
                .codigoAcesso(entregador.getCodigoAcesso())
                .placaVeiculo(entregador.getPlacaVeiculo())
                .corVeiculo(entregador.getCorVeiculo())
                .tipoVeiculo(entregador.getTipoVeiculo())
                .build();

        entregadorDTO = new EntregadorGetRequestDTO(entregador);
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de fluxos básicos API Rest")
    class EntregadorVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos entregadores salvos")
        void quandoBuscamosTodosEntregadores() throws Exception {
            // Arrange
            // Vamos ter 3 entregadores no banco
            Entregador entregador1 = Entregador.builder()
                    .nome("Jose")
                    .placaVeiculo("GHF-1212")
                    .corVeiculo("Prata")
                    .tipoVeiculo("carro")
                    .codigoAcesso("654321")
                    .build();
            Entregador entregador2 = Entregador.builder()
                    .nome("Halloran")
                    .placaVeiculo("MRD-0217")
                    .corVeiculo("Preto")
                    .tipoVeiculo("carro")
                    .codigoAcesso("217217")
                    .build();
            entregadorRepository.saveAll(Arrays.asList(entregador1, entregador2));

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<EntregadorResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(3, resultado.size());
        }

        @Test
        @DisplayName("Quando buscamos por um entregador salvo pelo id")
        void quandoBuscamosEntregadorPorId() throws Exception {
            // Arrange
            entregadorDTO.setCodigoAcesso("123456");

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(entregadorDTO.getId(), resultado.getId()),
                    () -> assertEquals(entregadorDTO.getNome(), resultado.getNome()),
                    () -> assertEquals(entregadorDTO.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                    () -> assertEquals(entregadorDTO.getCorVeiculo(), resultado.getCorVeiculo()),
                    () -> assertEquals(entregadorDTO.getTipoVeiculo(), resultado.getTipoVeiculo()),
                    () -> assertEquals(entregadorDTO.getCodigoAcesso(), resultado.getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando buscamos um entregador inexistente")
        void quandoBuscamosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando buscamos um entregador com id invalido")
        void quandoBuscamosEntregadorComIdInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um entregador com dados válidos")
        void quandoCriamosEntregadorValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ENTREGADORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(entregadorPostPutRequestDTO.getNome(), resultado.getNome()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getCorVeiculo(), resultado.getCorVeiculo()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getTipoVeiculo(), resultado.getTipoVeiculo())
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador com dados válidos")
        void quandoAlteramosEntregadorValido() throws Exception {
            // Arrange
            Long entregadorId = entregador.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(entregadorId, resultado.getId().longValue()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getNome(), resultado.getNome()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getCorVeiculo(), resultado.getCorVeiculo()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getTipoVeiculo(), resultado.getTipoVeiculo())
            );
        }

        @Test
        @DisplayName("Quando alteramos um entregador inexistente")
        void quandoAlteramosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos um entregador passando um código de acesso inválido")
        void quandoAlteramosEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluímos um entregador salvo")
        void quandoExcluimosEntregadorSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um entregador inexistente")
        void quandoExcluimosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluímos um entregador passando um código de acesso inválido")
        void quandoExcluimosEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }
    }


    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class EntregadorVerificacaoNome {

        @Test
        @DisplayName("Quando alteramos o nome do entregador com dados válidos")
        void quandoAlteramosNomeDoEntregadorValido() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setNome("Lana Del Rey Alterada");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("Lana Del Rey Alterada", resultado.getNome());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com nome vazio")
        void quandoAlteramosEntregadorComNomeVazio() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setNome("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome e obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de placa")
    class EntregadorVerificacaoPlaca {

        @Test
        @DisplayName("Quando alteramos o entregador com placa válida")
        void quandoAlteramosEntregadorComPlacaValida() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setPlacaVeiculo("DEF-3456");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("DEF-3456", resultado.getPlacaVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com placa vazia")
        void quandoAlteramosEntregadorComPlacaVazia() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setPlacaVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Placa do veiculo e obrigatoria", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de tipo")
    class EntregadorVerificacaoTipo {

        @Test
        @DisplayName("Quando alteramos o entregador com tipo de veiculo válido")
        void quandoAlteramosEntregadorComTipoVeiculoValido() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setTipoVeiculo("carro");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("carro", resultado.getTipoVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com tipo de veiculo nulo")
        void quandoAlteramosEntregadorComTipoVeiculoVazio() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setTipoVeiculo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo do veiculo e obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador com tipo de veiculo inválido")
        void quandoAlteramosEntregadorComTipoVeiculoInvalido() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setTipoVeiculo("bicicleta");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo do veiculo deve ser moto ou carro", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o tipo passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de cor do veiculo")
    class EntregadorVerificacaoCorVeiculo {

        @Test
        @DisplayName("Quando alteramos o entregador com cor do veiculo válida")
        void quandoAlteramosEntregadorComCorVeiculoValida() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCorVeiculo("preto");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("preto", resultado.getCorVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com cor do veiculo vazia")
        void quandoAlteramosEntregadorComCorVeiculoVazia() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCorVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Cor do veiculo e obrigatoria", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos a cor do veiculo passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de alteração de disponibilidade do entregador")
    class EntregadorDefinirDisponibilidade {

        @Test
        @DisplayName("Quando alteramos a disponibilidade do entregador para disponível")
        void quandoAlteramosDisponibilidadeParaDisponivel() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setId(entregador.getId());

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString, EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertTrue(resultado.isDisponibilidade());
            assertEquals(resultado.getStatusEntregador(), "Ativo");
        }


        @Test
        @DisplayName("Quando alteramos a disponibilidade do entregador para indisponível")
        void quandoAlteramosDisponibilidadeParaIndisponivel() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setId(entregador.getId());

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertFalse(resultado.isDisponibilidade());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um entregador inexistente")
        void quandoAlteramosDisponibilidadeDeEntregadorInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + 999999 + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade do entregador para disponível com estabelecimento associado")
        void quandoAlteramosDisponibilidadeParaDisponivelComEstabelecimentoAssociado() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setId(entregador.getId());
            Estabelecimento estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("654321")
                    .build());

            List<Entregador> entregadores = new LinkedList<>();
            entregadores.add(entregador);
            estabelecimento.setEntregadoresAprovados(entregadores);
            estabelecimentoRepository.save(estabelecimento);


            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.class);

            // Assert
            assertTrue(resultado.isDisponibilidade());
            assertEquals(resultado.getId(), entregador.getId());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um entregador passando codigo de acesso inválido")
        void quandoAlteramosDisponibilidadeDeEntregadorComCodigoAcessoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
            assertFalse(entregador.isDisponibilidade());
        }
    }


    @Nested
    @DisplayName("Conjunto de casos de testes para EntregadorGetRequestDTO")
    class EntregadorGetRequestDTOTest {

        @Test
        public void testGettersAndSetters() {
            EntregadorGetRequestDTO entregador = new EntregadorGetRequestDTO();
            entregador.setId(1L);
            entregador.setNome("Nome do Entregador");
            entregador.setPlacaVeiculo("XYZ1234");
            entregador.setTipoVeiculo("Moto");
            entregador.setCorVeiculo("Azul");

            assertEquals(Long.valueOf(1), entregador.getId());
            assertEquals("Nome do Entregador", entregador.getNome());
            assertEquals("XYZ1234", entregador.getPlacaVeiculo());
            assertEquals("Moto", entregador.getTipoVeiculo());
            assertEquals("Azul", entregador.getCorVeiculo());
        }

        @Test
        public void testEqualsAndHashCode() {
            EntregadorGetRequestDTO entregador1 = new EntregadorGetRequestDTO();
            entregador1.setId(1L);

            EntregadorGetRequestDTO entregador2 = new EntregadorGetRequestDTO();
            entregador2.setId(1L);

            EntregadorGetRequestDTO entregador3 = new EntregadorGetRequestDTO();
            entregador3.setId(2L);

            assertEquals(entregador1, entregador2);
            assertEquals(entregador1.hashCode(), entregador2.hashCode());

            assertNotEquals(entregador1, entregador3);
            assertNotEquals(entregador2, entregador3);
        }

        @Test
        public void testBuilder() {
            EntregadorGetRequestDTO entregador = EntregadorGetRequestDTO.builder()
                    .id(1L)
                    .nome("Nome do Entregador")
                    .placaVeiculo("XYZ1234")
                    .tipoVeiculo("Moto")
                    .corVeiculo("Azul")
                    .codigoAcesso("12345")
                    .build();

            assertEquals(Long.valueOf(1), entregador.getId());
            assertEquals("Nome do Entregador", entregador.getNome());
            assertEquals("XYZ1234", entregador.getPlacaVeiculo());
            assertEquals("Moto", entregador.getTipoVeiculo());
            assertEquals("Azul", entregador.getCorVeiculo());
            assertEquals("12345", entregador.getCodigoAcesso());
        }

        @Test
        public void testAllArgsConstructor() {
            EntregadorGetRequestDTO entregador = new EntregadorGetRequestDTO(
                    1L, "Nome do Entregador", "XYZ1234", "Moto", "Azul", "12345");

            assertEquals(Long.valueOf(1), entregador.getId());
            assertEquals("Nome do Entregador", entregador.getNome());
            assertEquals("XYZ1234", entregador.getPlacaVeiculo());
            assertEquals("Moto", entregador.getTipoVeiculo());
            assertEquals("Azul", entregador.getCorVeiculo());
            assertEquals("12345", entregador.getCodigoAcesso());
        }
    }
}