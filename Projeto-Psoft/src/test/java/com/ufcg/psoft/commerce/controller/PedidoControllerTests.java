package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.exception.EstabelecimentoEntregadorDisponivelException;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.service.notificacao.EstabelecimentoNotificaClientePedidoIndisponivelParaEntrega;
import com.ufcg.psoft.commerce.service.notificacao.PedidoNotificaClienteService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de pedidos")
public class PedidoControllerTests {
    final String URI_PEDIDOS = "/pedidos";

    @Autowired
    MockMvc driver;

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    Cliente cliente;
    Entregador entregador;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizzaM;
    Pizza pizzaG;
    Estabelecimento estabelecimento;
    Pedido pedido;
    Pedido pedido1;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build());
        sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build());
        sabor2 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.0)
                .precoG(30.0)
                .disponivel(true)
                .build());
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Anton Ego")
                .endereco("Paris")
                .codigoAcesso("123456")
                .build());
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Joãozinho")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("101010")
                .build());
        pizzaM = Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build();
        pizzaG = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();
        List<Pizza> pizzas = List.of(pizzaM);
        List<Pizza> pizzas1 = List.of(pizzaM, pizzaG);
        pedido = Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzas)
                .build();
        pedido1 = Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzas1)
                .build();
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega(pedido.getEnderecoEntrega())
                .pizzas(pedido.getPizzas())
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class PedidoVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando criamos um novo pedido com dados válidos")
        void quandoCriamosUmNovoPedidoComDadosValidos() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())// Codigo 201
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco()),
                    () -> assertEquals(pedido.getEnderecoEntrega(), resultado.getEnderecoEntrega())
            );
        }


        @Test
        @DisplayName("Quando criamos um novo pedido com dados válidos")
        void quandoCriamosUmNovoPedidoComDadosValidosEComVariasPizza() throws Exception {
            // Arrange
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .enderecoEntrega(pedido1.getEnderecoEntrega())
                    .pizzas(pedido1.getPizzas())
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isCreated())
                    .andDo(print())// Codigo 201
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO2.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO2.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido1.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido1.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(35.0, resultado.getPreco()),
                    () -> assertEquals(pedido1.getEnderecoEntrega(), resultado.getEnderecoEntrega())
            );
        }


        @Test
        @DisplayName("Quando criamos um novo pedido com id invalido, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComIdInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", "111222")
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cliente consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com codigo invalido, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComCodigoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", "111222")
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com id do estabelecimento invalido, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComIdEstabelecimentoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", "111222")
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage());
        }


        @Test
        @DisplayName("Quando criamos um novo pedido com dados válidos")
        void quandoCriamosUmNovoPedidoSemInformarEndereco() throws Exception {
            // Arrange
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .pizzas(pedido.getPizzas())
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isCreated())
                    .andDo(print())// Codigo 201
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals("Paris", resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO2.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco()),
                    () -> assertEquals(cliente.getEndereco(), resultado.getEnderecoEntrega())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com pizza com tamanho media e sabor não informado, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComPizzaMediaESaborInvalido() throws Exception {
            // Arrange
            Pizza pizzaMedia = Pizza.builder()
                    .tamanho("media")
                    .build();
            List<Pizza> pizzasMedia = List.of(pizzaMedia);
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .pizzas(pizzasMedia)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com pizza com tamanho grande e sabores não informado, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComPizzaGrandeESaborInvalido() throws Exception {
            // Arrange
            Pizza pizzaGrande = Pizza.builder()
                    .tamanho("grande")
                    .build();
            List<Pizza> pizzasGrande = List.of(pizzaGrande);
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .pizzas(pizzasGrande)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com pizza com tamanho grande e um sabor não informado, então o pedido é criado")
        void quandoCriamosUmNovoPedidoComPizzaGrandeEUmSaborInvalido() throws Exception {
            // Arrange
            Pizza pizzaGrande = Pizza.builder()
                    .sabor1(sabor1)
                    .tamanho("grande")
                    .build();
            List<Pizza> pizzasGrande = List.of(pizzaGrande);
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .pizzas(pizzasGrande)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isCreated())
                    .andDo(print())// Codigo 201
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO2.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertNull(resultado.getPizzas().get(0).getSabor2()),
                    () -> assertEquals(cliente.getId(), resultado.getClienteId()),
                    () -> assertEquals(estabelecimento.getId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(20.0, resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com pizza com tamanho grande e sabor não cadastrado, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComPizzaGrandeESaborNaoCadastrado() throws Exception {
            // Arrange
            Sabor sabor3 = Sabor.builder()
                    .id(111222L)
                    .nome("Sabor Tres")
                    .tipo("salgado")
                    .precoM(10.0)
                    .precoG(20.0)
                    .disponivel(true)
                    .build();
            Pizza pizzaGrande = Pizza.builder()
                    .sabor1(sabor3)
                    .tamanho("grande")
                    .build();
            List<Pizza> pizzasGrande = List.of(pizzaGrande);
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .pizzas(pizzasGrande)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um novo pedido com pizza com tamanho media e sabor não cadastrado, então uma excessão é retornada")
        void quandoCriamosUmNovoPedidoComPizzaMediaESaborNaoCadastrado() throws Exception {
            // Arrange
            Sabor sabor3 = Sabor.builder()
                    .id(111222L)
                    .nome("Sabor Tres")
                    .tipo("salgado")
                    .precoM(10.0)
                    .precoG(20.0)
                    .disponivel(true)
                    .build();
            Pizza pizzaMedia = Pizza.builder()
                    .sabor1(sabor3)
                    .tamanho("media")
                    .build();
            List<Pizza> pizzasMedia = List.of(pizzaMedia);
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO2 = PedidoPostPutRequestDTO.builder()
                    .pizzas(pizzasMedia)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO2)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos um novo pedido com dados válidos")
        void quandoAlteramosPedidoValido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Long pedidoId = pedido.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", pedido.getId().toString())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(pedidoId, resultado.getId().longValue()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando alteramos um pedido inexistente")
        void quandoAlteramosPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", "999999")
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos um pedido passando codigo de acesso invalido")
        void quandoAlteramosPedidoPassandoCodigoAcessoInvalido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", pedido.getId().toString())
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por todos seus pedidos salvos")
        void quandoClienteBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS)
                            .param("idCliente", cliente.getId().toString())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Quando um cliente inexistente busca por todos seus pedidos salvos")
        void quandoClienteInexistenteBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS)
                            .param("idCliente", "-1")
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido seu salvo pelo id primeiro")
        void quandoClienteBuscaPedidoPorId() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando um cliente invalido busca por um pedido")
        void quandoClienteInvalidoBuscaPedido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/-1")
                            .param("clienteId", cliente.getId().toString())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido seu salvo por id inexistente")
        void quandoClienteBuscaPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getId())
                            .param("clienteId", cliente.getId().toString())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido feito por outro cliente")
        void quandoClienteBuscaPedidoDeOutroCliente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Cliente cliente1 = clienteRepository.save(Cliente.builder()
                    .nome("Catarina")
                    .endereco("Casinha")
                    .codigoAcesso("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente1.getId())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }


        @Test
        @DisplayName("Quando um estabelecimento busca todos os pedidos feitos nele")
        void quandoEstabelecimentoBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "buscar-pedidos" + "/" + estabelecimento.getId() + "/" + estabelecimento.getCodigoAcesso())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Quando um estabelecimento inexistente busca todos os pedidos feitos nele")
        void quandoEstabelecimentoInexistenteBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "buscar-pedidos" + "/-1" + "/" + estabelecimento.getCodigoAcesso())
                            .param("idEstabelecimento", "-1")
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id primeiro")
        void quandoEstabelecimentoBuscaPedidoPorId() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento.getId() + "/" + estabelecimento.getCodigoAcesso())
                            .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id inexistente")
        void quandoEstabelecimentoBuscaPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + estabelecimento.getCodigoAcesso() + "/" + estabelecimento.getId() + "/" + "999999")
                            .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelecimento inexistente busca por um pedido")
        void quandoEstabelecimentoInexistenteBuscaPedido() throws Exception {
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/-1" + "/" + estabelecimento.getCodigoAcesso())
                            .param("estabelecimentoCodigoAcesso", "-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito em outro estabelecimento")
        void quandoEstabelecimentoBuscaPedidoDeOutroEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento1.getId() + "/" + estabelecimento1.getCodigoAcesso())
                            .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí um pedido feito por ele salvo")
        void quandoClienteExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente inexistente excluí um pedido feito por ele salvo")
        void quandoClienteInexistenteExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí um pedido inexistente")
        void quandoClienteExcluiPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí todos seus pedidos feitos por ele salvos")
        void quandoClienteExcluiTodosPedidosSalvos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(Pedido.builder()
                    .preco(10.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(List.of(pizzaM, pizzaG))
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/excluir-pedido/" + cliente.getId())
                            .param("clienteId", cliente.getId().toString())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido feito nele salvo")
        void quandoEstabelecimentoExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento.getId() + "/" + estabelecimento.getCodigoAcesso())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um estabelencimento inexistente excluí um pedido feito nele salvo")
        void quandoEstabelecimentoInexistenteExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/-1/" + estabelecimento.getCodigoAcesso())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O id consultado é invalido", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido inexistente")
        void quandoEstabelecimentoExcluiPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + "999999" + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido feito em outro estabelecimento")
        void quandoEstabelecimentoExcluiPedidoDeOutroEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento1.getId() + "/" + estabelecimento1.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento1.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí todos os pedidos feitos nele salvos")
        void quandoEstabelecimentoExcluiTodosPedidosSalvos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(Pedido.builder()
                    .preco(10.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(List.of(pizzaM, pizzaG))
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/excluir-pedido-estabelecimento/" + estabelecimento.getId())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente cancela um pedido")
        void quandoClienteCancelaPedido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/cancelar-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente excluir um pedido com status Pedido pronto")
        void quandoClienteExcluirPedidoComStatusPedidoPronto() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedido.setStatusEntrega("Pedido pronto");

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/cancelar-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Cancelamento de entrega invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluir um pedido com status Pedido em rota")
        void quandoClienteExcluirPedidoComStatusPedidoEmRota() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedido.setStatusEntrega("Pedido em rota");

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/cancelar-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Cancelamento de entrega invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get("/clientes/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido.getId(), resultado.get(0).getId());
            assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get( "/clientes/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + "999999" + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com pedido inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComPedidoInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get("/clientes/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + "999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com cliente inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComClienteInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get("/clientes/" + "pedido-cliente-estabelecimento" + "/" + "999999" + "/" + estabelecimento.getId() + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cliente consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com pedidoId null")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidoIdNull() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get("/clientes" + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido.getId(), resultado.get(0).getId());
            assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com status")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComStatus() throws Exception {
            // Arrange
            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                    .preco(30.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(List.of(pizzaM))
                    .statusEntrega("Pedido em preparo")
                    .build());


            // Act
            String responseJsonString = driver.perform(get("/clientes" + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + pedido3.getStatusEntrega())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido3.getId(), resultado.get(0).getId());
            assertEquals(pedido3.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido3.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento filtrados por entrega")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidosFiltradosPorEntrega() throws Exception {
            // Arrange
            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                    .preco(30.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(List.of(pizzaM))
                    .statusEntrega("Pedido entregue")
                    .build());
            Pedido pedido4 = pedidoRepository.save(Pedido.builder()
                    .preco(30.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(List.of(pizzaM))
                    .statusEntrega("Pedido em preparo")
                    .build());

            // Act
            String responseJsonString = driver.perform(get("/clientes" + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
            assertEquals(pedido4.getId(), resultado.get(0).getId());
            assertEquals(pedido4.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido4.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
            assertEquals(pedido3.getId(), resultado.get(1).getId());
            assertEquals(pedido3.getClienteId(), resultado.get(1).getClienteId());
            assertEquals(pedido3.getEstabelecimentoId(), resultado.get(1).getEstabelecimentoId());
        }
    }

        @Nested
        @DisplayName("Alteração de estado de pedido")
        public class AlteracaoEstadoPedidoTest {
            Pedido pedido1;

            @BeforeEach
            void setUp() {
                pedido1 = pedidoRepository.save(Pedido.builder()
                        .estabelecimentoId(estabelecimento.getId())
                        .clienteId(cliente.getId())
                        .enderecoEntrega("Rua 1")
                        .pizzas(List.of(pizzaG))
                        .preco(10.0)
                        .build()
                );
            }

            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador")
            void quandoEstabelecimentoAssociaPedidoEntregador() throws Exception {
                // Arrange

                pedido1.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadores = new LinkedList<>();
                entregadores.add(entregador);
                entregador.setDisponibilidade(true);
                estabelecimento.setEntregadoresAprovados(entregadores);
                List<Pedido> pedidosPendentes = estabelecimento.getPedidosPendentes();
                pedidosPendentes.add(pedido1);
                estabelecimento.setPedidosPendentes(pedidosPendentes);


                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido1.getId() + "/" + "associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                // Assert
                assertEquals(resultado.getStatusEntrega(), "Pedido em rota");
                assertEquals(entregador.getId(), resultado.getEntregadorId());
            }


            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador sem ter pedidos pedentes")
            void quandoEstabelecimentoAssociaPedidoEntregadorSemPedidosPedentes() throws Exception {
                // Arrange

                pedido1.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadores = new LinkedList<>();
                entregadores.add(entregador);
                entregador.setDisponibilidade(true);
                estabelecimento.setEntregadoresAprovados(entregadores);


                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido1.getId() + "/" + "associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("O pedido não esta pedente", resultado.getMessage());

            }

            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador com cliente que possui email")
            void quandoEstabelecimentoAssociaPedidoEntregadorComClienteQuePossuiEmail() throws Exception {
                // Arrange
                cliente.setEmail("pitsadelivery.contato@gmail.com");
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadores = new LinkedList<>();
                entregadores.add(entregador);
                entregador.setDisponibilidade(true);
                estabelecimento.setEntregadoresAprovados(entregadores);
                List<Pedido> pedidosPendentes = estabelecimento.getPedidosPendentes();
                pedidosPendentes.add(pedido);
                estabelecimento.setPedidosPendentes(pedidosPendentes);


                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                // Assert
                assertEquals(resultado.getStatusEntrega(), "Pedido em rota");
                assertEquals(entregador.getId(), resultado.getEntregadorId());
            }

            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador com estabelecimento invalido")
            void quandoEstabelecimentoAssociaPedidoEntregadorComEstabelecimentoInvalido() throws Exception {
                // Arrange
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadores = new LinkedList<>();
                entregadores.add(entregador);
                estabelecimento.setEntregadoresAprovados(entregadores);
                entregador.setDisponibilidade(true);


                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "/associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", "00")
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("O id consultado é invalido", resultado.getMessage());
            }

            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador com status de entrega invalido")
            void quandoEstabelecimentoAssociaPedidoEntregadorComStatusEntregaInvalido() throws Exception {
                // Arrange
                pedidoRepository.save(pedido);
                List<Entregador> entregadores = new LinkedList<>();
                entregadores.add(entregador);
                estabelecimento.setEntregadoresAprovados(entregadores);
                entregador.setDisponibilidade(true);


                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "/associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("Status de entrega invalido!", resultado.getMessage());
            }


            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador sem possuir entregadores disponiveis")
            void quandoEstabelecimentoAssociaPedidoEntregadorSemPossuirEntregadoresAprovados() throws Exception {
                // Arrange
                cliente.setEmail("pitsadelivery.contato@gmail.com");
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadores = new LinkedList<>();
                entregador.setDisponibilidade(false);
                entregadores.add(entregador);
                estabelecimento.setEntregadoresAprovados(entregadores);

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "/associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                // Assert
                assertEquals(resultado.getStatusEntrega(), "Pedido pronto");
                assertEquals(entregador.getId(), resultado.getEntregadorId());
                assertEquals(pedido.getId(), resultado.getId());
                assertEquals(estabelecimento.getId(), resultado.getEstabelecimentoId());
            }

            @Test
            @DisplayName("Quando o estabelecimento associa um pedido a um entregador sem possuir entregadores disponiveis validos e com cliente sem email cadastrado")
            void quandoEstabelecimentoAssociaPedidoEntregadorSemPossuirEntregadoresAprovadosValidosEComClienteSemEmail() throws Exception {
                // Arrange
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadores = new LinkedList<>();
                estabelecimento.setEntregadoresAprovados(entregadores);


                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "/associar-pedido-entregador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                // Assert
                assertEquals(resultado.getStatusEntrega(), "Pedido pronto");
                assertEquals(entregador.getId(), resultado.getEntregadorId());
                assertEquals(pedido.getId(), resultado.getId());
                assertEquals(estabelecimento.getId(), resultado.getEstabelecimentoId());
            }


            @Test
            @DisplayName("Quando o cliente confirma a entrega de um pedido")
            void quandoClienteConfirmaEntregaPedido() throws Exception {
                // Arrange
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido em rota");
                List<Entregador> entregadores = new LinkedList<>();
                entregador.setDisponibilidade(true);
                entregadores.add(entregador);
                estabelecimento.setEntregadoresAprovados(entregadores);
                pedido.setEstabelecimentoId(estabelecimento.getId());

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId() + "/cliente-confirmar-entrega")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                // Assert
                Estabelecimento estabelecimentoTeste = estabelecimentoRepository.findById(resultado.getEstabelecimentoId()).get();
                Entregador entregadorTeste = entregadorRepository.findById(resultado.getEntregadorId()).get();

                assertEquals(resultado.getStatusEntrega(), "Pedido entregue");
                assertTrue(estabelecimentoTeste.getEntregadoresAprovados().contains(entregadorTeste));
            }

            @Test
            @DisplayName("Quando o cliente que possui email confirma a entrega de um pedido ")
            void quandoClienteComEmailConfirmaEntregaPedido() throws Exception {
                // Arrange
                cliente.setEmail("pitsadelivery.contato@gmail.com");
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido em rota");
                List<Entregador> entregadores = new LinkedList<>();
                entregador.setDisponibilidade(true);
                estabelecimento.setEntregadoresAprovados(entregadores);
                pedido.setEstabelecimentoId(estabelecimento.getId());

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId() + "/cliente-confirmar-entrega")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                // Assert
                Estabelecimento estabelecimentoTeste = estabelecimentoRepository.findById(resultado.getEstabelecimentoId()).get();
                Entregador entregadorTeste = entregadorRepository.findById(resultado.getEntregadorId()).get();

                assertEquals(resultado.getStatusEntrega(), "Pedido entregue");
                assertTrue(estabelecimentoTeste.getEntregadoresAprovados().contains(entregadorTeste));
            }

        }

        @Nested
        @DisplayName("Conjunto de casos de teste da confirmação de pagamento de um pedido")
        public class PedidoConfirmarPagamentoTests {

            Pedido pedido1;

            @BeforeEach
            void setUp() {
                pedido1 = pedidoRepository.save(Pedido.builder()
                        .estabelecimentoId(estabelecimento.getId())
                        .clienteId(cliente.getId())
                        .enderecoEntrega("Rua 1")
                        .pizzas(List.of(pizzaG))
                        .preco(10.0)
                        .build()
                );
            }

            @Test
            @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
            void confirmaPagamentoCartaoCredito() throws Exception {
                // Arrange
                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "Cartão de crédito")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();
                // Assert
                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
                assertAll(
                        () -> assertTrue(resultado.getStatusPagamento()),
                        () -> assertEquals(10, resultado.getPreco()),
                        () -> assertEquals("Pedido em preparo", resultado.getStatusEntrega())
                );
            }

            @Test
            @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
            void confirmaPagamentoCartaoDebito() throws Exception {
                // Arrange
                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "Cartão de débito")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();
                // Assert
                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
                assertAll(
                        () -> assertTrue(resultado.getStatusPagamento()),
                        () -> assertEquals(9.75, resultado.getPreco()),
                        () -> assertEquals("Pedido em preparo", resultado.getStatusEntrega())
                );
            }

            @Test
            @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
            void confirmaPagamentoPIX() throws Exception {
                // Arrange
                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "PIX")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();
                // Assert
                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
                assertAll(
                        () -> assertTrue(resultado.getStatusPagamento()),
                        () -> assertEquals(9.5, resultado.getPreco()),
                        () -> assertEquals("Pedido em preparo", resultado.getStatusEntrega())
                );
            }


            @Test
            @DisplayName("Quando confirmamos o pagamento de um pedido por um metodo de pagamento invalido")
            void confirmaPagamentoMetodoInvalido() throws Exception {
                // Arrange
                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "Dinheiro")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("O meio de pagamento consultado nao existe!", resultado.getMessage());

            }

            @Test
            @DisplayName("Quando confirmamos o pagamento de um pedido por um metodo de pagamento vazio")
            void confirmaPagamentoMetodoVazio() throws Exception {
                // Arrange
                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("O meio de pagamento consultado nao existe!", resultado.getMessage());

            }

            @Test
            @DisplayName("Quando confirmamos o pagamento de um pedido com status invalido")
            void confirmaPagamentoDePedidoJaPago() throws Exception {
                // Arrange
                driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "PIX")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "PIX")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("Status de entrega invalido!", resultado.getMessage());

            }

        }

        @Nested
        @DisplayName("Conjunto de casos de teste de preparacao de um pedido")
        public class PedidoEstabeleciemntoPreparaPedidoTests {

            Pedido pedido1;

            @BeforeEach
            void setUp() {
                pedido1 = pedidoRepository.save(Pedido.builder()
                        .estabelecimentoId(estabelecimento.getId())
                        .clienteId(cliente.getId())
                        .enderecoEntrega("Rua 1")
                        .pizzas(List.of(pizzaG))
                        .preco(10.0)
                        .build()
                );
            }

            @Test
            @DisplayName("Quando estabelecimento prepara um pedido")
            void estabelecimentoPreparaUmPedido() throws Exception {
                // Arrange
                // confirmando um pagamento para mudar o status do pedido
                driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "Cartão de crédito")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido1.getId() + "/preparar-pedido")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();
                // Assert
                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                assertAll(
                        () -> assertNotNull(resultado.getId()),
                        () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                        () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                        () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                        () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                        () -> assertEquals(pedido.getPreco(), resultado.getPreco()),
                        () -> assertEquals(pedido.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                        () -> assertEquals("Pedido pronto", resultado.getStatusEntrega())
                );
            }


            @Test
            @DisplayName("Quando estabelecimento prepara um pedido com cliente que possui email")
            void estabelecimentoPreparaUmPedidoComClienteQuePossuiEmail() throws Exception {
                // Arrange
                cliente.setEmail("pitsadelivery.contato@gmail.com");
                // confirmando um pagamento para mudar o status do pedido
                driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "Cartão de crédito")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido1.getId() + "/preparar-pedido")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();
                // Assert
                Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

                assertAll(
                        () -> assertNotNull(resultado.getId()),
                        () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                        () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                        () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                        () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                        () -> assertEquals(pedido.getPreco(), resultado.getPreco()),
                        () -> assertEquals(pedido.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                        () -> assertEquals("Pedido pronto", resultado.getStatusEntrega())
                );
            }

            @Test
            @DisplayName("Quando estabelecimento prepara um pedido com estabelecimento invalido")
            void estabelecimentoPreparaUmPedidoComIdEstabelecimentoInvalido() throws Exception {
                // Arrange
                // confirmando um pagamento para mudar o status do pedido
                driver.perform(put(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                                .param("pedidoId", pedido1.getId().toString())
                                .param("metodoPagamento", "Cartão de crédito")
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andReturn().getResponse().getContentAsString();

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido1.getId() + "/preparar-pedido")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", "00")
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("O id consultado é invalido", resultado.getMessage());
            }

            @Test
            @DisplayName("Quando estabelecimento prepara um pedido com status de entrega invalido, ou seja um pedido que ainda não foi pago")
            void estabelecimentoPreparaUmPedidoQueAindaNaoFoiPago() throws Exception {
                // Arrange

                // Act
                String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido1.getId() + "/preparar-pedido")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("estabelecimentoId", estabelecimento.getId().toString())
                                .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
                                .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

                // Assert
                assertEquals("Status de entrega invalido!", resultado.getMessage());
            }
        }


        @Nested
        @DisplayName("Caso de testes para notificação")
        public class NotificacaoTest {
            Pedido pedido1;

            @Mock
            PedidoNotificaClienteService pedidoNotificaClienteService;

            @Autowired
            EstabelecimentoNotificaClientePedidoIndisponivelParaEntrega estabelecimentoNotificaClientePedidoIndisponivelParaEntrega;

            @BeforeEach
            void setUp() {
                pedido1 = pedidoRepository.save(Pedido.builder()
                        .estabelecimentoId(estabelecimento.getId())
                        .clienteId(cliente.getId())
                        .enderecoEntrega("Rua 1")
                        .pizzas(List.of(pizzaG))
                        .preco(10.0)
                        .build()
                );
            }

            @Test
            @DisplayName("Quando a notificação ocorre com sucesso")
            void quandoAnotificacaoOcorreComSucesso() throws Exception {
                // Arrange
                cliente.setEmail("pitsadelivery.contato@gmail.com");
                pedidoRepository.save(pedido);
                pedido.setStatusEntrega("Pedido em rota");

                // Assert
                pedidoNotificaClienteService.notificar(pedido.getId());

            }

            @Test
            public void testValidaDisponibilidadeEntregadorComEntregadoresDisponiveis() {
                pedido1.setStatusEntrega("Pedido pronto");
                List<Entregador> entregadoresAprovados = estabelecimento.getEntregadoresAprovados();
                entregador.setDisponibilidade(true);
                entregador.setStatusEntregador("Ativo");
                entregadoresAprovados.add(entregador);
                estabelecimento.setEntregadoresAprovados(entregadoresAprovados);
                estabelecimentoRepository.save(estabelecimento);

                try {
                    estabelecimentoNotificaClientePedidoIndisponivelParaEntrega.notifica(pedido1.getId(), estabelecimento.getId());
                    fail("Deveria ter lançado a EstabelecimentoEntregadorDisponivelException");

                } catch (EstabelecimentoEntregadorDisponivelException e) {
                    assertEquals("Existe entregador disponivel no estabelecimento!", e.getMessage());
                }
            }
        }

    @Nested
    @DisplayName("Caso de testes para excessão")
    public class ExcessaoTest {
        @Test
        public void testCommerceException() {
            try {
                throw new CommerceException();
            } catch (CommerceException e) {
                assertNotNull(e);
                assertEquals("Erro inesperado no AppCommerce!", e.getMessage());
            }
        }

        @Test
        public void testCustomErrorTypeConstruction() {
            CommerceException commerceException = new CommerceException("Mensagem de erro personalizada");
            CustomErrorType customErrorType = new CustomErrorType(commerceException);

            assertNotNull(customErrorType.getTimestamp());
            assertEquals("Mensagem de erro personalizada", customErrorType.getMessage());
            assertNotNull(customErrorType.getErrors());
            assertTrue(customErrorType.getErrors().isEmpty());
        }
    }
}

