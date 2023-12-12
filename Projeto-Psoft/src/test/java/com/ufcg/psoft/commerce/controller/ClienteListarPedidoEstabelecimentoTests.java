package com.ufcg.psoft.commerce.controller;


import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.IdNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.service.pedido.ClienteListarPedidoEstabelecimentoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de listagem de pedidos por parte de clientes em um estabelecimento")
public class ClienteListarPedidoEstabelecimentoTests {

    @Autowired
    ClienteListarPedidoEstabelecimentoService driver;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    Cliente cliente;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizza1;
    Pizza pizza2;
    Estabelecimento estabelecimento;

    Pedido pedido;
    Pedido pedido2;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

    @BeforeEach
    void setup() {
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("123456")
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
                .precoM(10.0)
                .precoG(30.0)
                .disponivel(true)
                .build());
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Anton Ego")
                .endereco("Paris")
                .codigoAcesso("123456")
                .build());
        pizza1 = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build());
        pizza2 = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor2)
                .tamanho("media")
                .build());
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        pedido = pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1))
                .statusEntrega("Pedido entregue")
                .build());
        pedido2 = pedidoRepository.save(Pedido.builder()
                .preco(20.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido entregue")
                .build());
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Apartamento 237")
                .pizzas(pizzas)
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Test
    @DisplayName("Listar pedido valido de um cliente em um estabelecimento")
    void testListarPedidoValido() {
        // Arrange

        // Act
        List<Pedido> pedidos = driver.listar(pedido.getId(), cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), null);
        // Assert
        assertAll(
                () -> assertEquals(1, pedidos.size()),
                () -> assertEquals(pedido.getId(), pedidos.get(0).getId())
        );
    }

    @Test
    @DisplayName("Listar pedido inválido de um cliente em um estabelecimento")
    void testListarPedidoInvalido() {

        // Act
        IdNaoExisteException thrown = assertThrows(
                IdNaoExisteException.class, // Altere para o tipo correto da exceção
                () -> driver.listar(0L, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), null)
        );

        // Assert
        assertAll(
                () -> assertEquals("O id consultado é invalido", thrown.getMessage())
        );
    }


    @Test
    @DisplayName("Listar pedido de um cliente em um estabelecimento invalido")
    void testListarPedidoEstabelecimentoInvalido() {

        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.listar(pedido.getId(), cliente.getId(), 99999L, cliente.getCodigoAcesso(), null)
        );
        // Assert
        assertAll(
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Listar pedido de um cliente com codigo de acesso invalido em um estabelecimento")
    void testListarPedidoCodigoAcessoInvalido() {

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.listar(pedido.getId(), cliente.getId(), estabelecimento.getId(), "000000", null)
        );
        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage())
        );
    }


    @Test
    @DisplayName("Listar pedido invalido de um cliente em um estabelecimento (cliente errado)")
    void testListarPedidoInvalidoClienteErrado() {
        // Arrange
        Cliente cliente2 = clienteRepository.save(Cliente.builder()
                .nome("Linguini")
                .endereco("Paris")
                .codigoAcesso("654321")
                .build());
        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.listar(pedido.getId(), cliente2.getId(), estabelecimento.getId(), cliente2.getCodigoAcesso(), null)
        );
        // Assert
        assertAll(
                () -> assertEquals("O pedido consultado nao existe!", thrown.getMessage())
        );
    }
    @Test
    @DisplayName("Listar pedidos de um cliente em um estabelecimento")
    void testListarPedidos() {
        // Arrange
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .preco(30.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido entregue")
                .build());
        // Act
        List<Pedido> pedidos = driver.listar(null, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), null);
        // Assert
        assertAll(
                () -> assertEquals(3, pedidos.size()),
                () -> assertEquals(pedido.getId(), pedidos.get(0).getId()),
                () -> assertEquals(pedido2.getId(), pedidos.get(1).getId()),
                () -> assertEquals(pedido3.getId(), pedidos.get(2).getId())
        );
    }

    @Test
    @DisplayName("Listar pedidos de um cliente em um estabelecimento com status de Pedido entregue")
    void testListarPedidosStatusEntrega() {
        // Arrange
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .preco(30.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido entregue")
                .build());
        // Act
        List<Pedido> pedidos = driver.listar(null, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), "Pedido entregue");
        // Assert
        assertAll(
                () -> assertEquals(3, pedidos.size()),
                () -> assertEquals(pedido.getId(), pedidos.get(0).getId()),
                () -> assertEquals(pedido2.getId(), pedidos.get(1).getId()),
                () -> assertEquals(pedido3.getId(), pedidos.get(2).getId())
        );
    }

    @Test
    @DisplayName("Listar pedidos de um cliente em um estabelecimento com status de Pedido recebido")
    void testListarPedidosStatusEntregaRecebido() {
        // Arrange
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .preco(30.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido recebido")
                .build());
        // Act
        List<Pedido> pedidos = driver.listar(null, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), "Pedido recebido");
        // Assert
        assertAll(
                () -> assertEquals(1, pedidos.size()),
                () -> assertEquals(pedido3.getId(), pedidos.get(0).getId())
        );
    }



    @Test
    @DisplayName("Listar pedidos de um cliente em um estabelecimento com status de Pedido em preparo")
    void testListarPedidosStatusEntregaPreparo() {
        // Arrange
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .preco(30.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido em preparo")
                .build());
        // Act
        List<Pedido> pedidos = driver.listar(null, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), "Pedido em preparo");
        // Assert
        assertAll(
                () -> assertEquals(1, pedidos.size()),
                () -> assertEquals(pedido3.getId(), pedidos.get(0).getId())
        );
    }

    @Test
    @DisplayName("Listar pedidos de um cliente em um estabelecimento com status de Pedido em entrega")
    void testListarPedidosStatusEmEntrega() {
        // Arrange
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .preco(30.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido em entrega")
                .build());
        // Act
        List<Pedido> pedidos = driver.listar(null, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), "Pedido em entrega");
        // Assert
        assertAll(
                () -> assertEquals(1, pedidos.size()),
                () -> assertEquals(pedido3.getId(), pedidos.get(0).getId())
        );
    }

    @Test
    @DisplayName("Listar pedidos de um cliente em um estabelecimento")
    void testListarPedidosCliente() {
        // Arrange
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .preco(30.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(List.of(pizza1, pizza2))
                .statusEntrega("Pedido em preparo")
                .build());
        // Act
        List<Pedido> pedidos = driver.listar(null, cliente.getId(), estabelecimento.getId(), cliente.getCodigoAcesso(), null);
        // Assert
        assertAll(
                () -> assertEquals(3, pedidos.size()),
                () -> assertEquals(pedido3.getId(), pedidos.get(0).getId()),
                () -> assertEquals(pedido.getId(), pedidos.get(1).getId()),
                () -> assertEquals(pedido2.getId(), pedidos.get(2).getId())
        );
    }
}
