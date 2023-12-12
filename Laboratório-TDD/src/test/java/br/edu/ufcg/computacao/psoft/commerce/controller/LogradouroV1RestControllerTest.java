package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPatchTipoNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPostPutDTO;

import br.edu.ufcg.computacao.psoft.commerce.exception.CodigoDeBarrasInvalidoException;
import br.edu.ufcg.computacao.psoft.commerce.exception.LogradouroNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.exception.PessoaNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository.LogradouroRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Testes da Entidade Logradouro")
@AutoConfigureMockMvc
class LogradouroV1RestControllerTest {

    @Autowired
    LogradouroRepository logradouroRepository;

    LogradouroPostPutDTO logradouroPostPutDTO;


    @Autowired
    MockMvc driver;


    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    ModelMapper modelMapper;

    LogradouroV1RestControllerTest() throws Exception {
    }


    @BeforeEach
    void setUp(
    ) {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Quando criar um logradouro com dados válidos, então o logradouro criado é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoCriarLogradouroComDadosValidos() throws Exception {
        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilapia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();

        String resultadoStr = driver.perform(MockMvcRequestBuilders.post("/v1/logradouro/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Logradouro logradouroResultado = objectMapper.readValue(resultadoStr, Logradouro.class);

        assertNotNull(logradouroResultado.getId());
        assertTrue(logradouroResultado.getId() > 0);
        assertEquals(logradouroPostPutDTO.getTipoLogradouro(), logradouroResultado.getTipoLogradouro());
        assertEquals(logradouroPostPutDTO.getNomeLogradouro(), logradouroResultado.getNomeLogradouro());
        assertEquals(logradouroPostPutDTO.getBairro(), logradouroResultado.getBairro());
        assertEquals(logradouroPostPutDTO.getCidade(), logradouroResultado.getCidade());
        assertEquals(logradouroPostPutDTO.getEstado(), logradouroResultado.getEstado());
        assertEquals(logradouroPostPutDTO.getPais(), logradouroResultado.getPais());
        assertEquals(logradouroPostPutDTO.getCep(), logradouroResultado.getCep());
        assertEquals(logradouroPostPutDTO.getComplemento(), logradouroResultado.getComplemento());

    }

    @Test
    @DisplayName("Quando atualizar um logradouro com dados válidos, então o logradouro é atualizado e retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarLogradouroComDadosValidos() throws Exception {

        //arrage

        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilapia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();

        Logradouro logradouroAtualizado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));
        logradouroPostPutDTO.setTipoLogradouro("Avenida");
        logradouroPostPutDTO.setNomeLogradouro("Maria aparecida silva");
        logradouroPostPutDTO.setBairro("Alto Branco");
        logradouroPostPutDTO.setCidade("Campina Grande");
        logradouroPostPutDTO.setEstado("Paraiba");
        logradouroPostPutDTO.setPais("Brasil");
        logradouroPostPutDTO.setCep("58419-270");
        logradouroPostPutDTO.setComplemento("Proximo a escola Tertuliano Maciel");

        // Act
        String resultadoStr = driver.perform(MockMvcRequestBuilders.put("/v1/logradouro/" + logradouroAtualizado.getId() + "/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Logradouro logradouroResultado = objectMapper.readValue(resultadoStr, Logradouro.class);

        assertNotNull(logradouroResultado.getId());
        assertTrue(logradouroResultado.getId() > 0);

        assertNotNull(logradouroResultado.getNomeLogradouro());
        assertEquals(logradouroResultado.getNomeLogradouro(), logradouroPostPutDTO.getNomeLogradouro());

        assertNotNull(logradouroResultado.getBairro());
        assertEquals(logradouroResultado.getBairro(), logradouroPostPutDTO.getBairro());

        assertNotNull(logradouroResultado.getCidade());
        assertEquals(logradouroResultado.getCidade(), logradouroPostPutDTO.getCidade());

        assertNotNull(logradouroResultado.getEstado());
        assertEquals(logradouroResultado.getEstado(), logradouroPostPutDTO.getEstado());

        assertNotNull(logradouroResultado.getPais());
        assertEquals(logradouroResultado.getPais(), logradouroPostPutDTO.getPais());

        assertNotNull(logradouroResultado.getCep());
        assertEquals(logradouroResultado.getCep(), logradouroPostPutDTO.getCep());

        assertNotNull(logradouroResultado.getComplemento());
        assertEquals(logradouroResultado.getComplemento(), logradouroPostPutDTO.getComplemento());


    }

    @Test
    @DisplayName("Quando atualizar o tipo e o nome do logradouro Entao o logradouro é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarTipoENomeLogradouroComDadosValidos() throws Exception {
        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro("Antatunes")
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilapia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();

        Logradouro logradouroBase = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

        // Atualizando tipo e nome
        LogradouroPatchTipoNomeDTO logradouroPatchTipoNomeDTO = LogradouroPatchTipoNomeDTO.builder()
                .tipoLogradouro("Avenida")
                .nomeLogradouro("Paulista")
                .build();

        String resultadoStr = driver.perform(MockMvcRequestBuilders.patch("/v1/logradouro/" + logradouroBase.getId() + "/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logradouroPatchTipoNomeDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Logradouro logradouroResultado = objectMapper.readValue(resultadoStr, Logradouro.class);

        // asserts
        assertAll(
                () -> assertNotNull(logradouroResultado.getId()),
                () -> assertEquals(logradouroPatchTipoNomeDTO.getTipoLogradouro(), logradouroResultado.getTipoLogradouro()),
                () -> assertEquals(logradouroPatchTipoNomeDTO.getNomeLogradouro(), logradouroResultado.getNomeLogradouro())
        );
    }

    @Test
    @DisplayName("Quando excluir um logradouro com id válido, entao nada é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoExcluirLogradouroComIdValido() throws Exception {

        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro("Antatunes")
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilapia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();

        Logradouro logradouroBase = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));


        String resultadoStr = driver.perform(MockMvcRequestBuilders
                        .delete("/v1/logradouro/" + logradouroBase.getId() + "/delete")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent()) // 204
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertAll(
                () -> assertTrue(resultadoStr.isBlank())
        );
    }

    @Test
    @DisplayName("Quando buscar todosos logradouros com id válido, Entao todos os logradouros são retornados")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoBuscarVariosLogradouros() throws Exception {
        Logradouro logradouroUm = Logradouro.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro("Julio Eduardo Silva")
                .bairro("Velame")
                .cidade("Queimadas")
                .estado("Paraiba")
                .pais("Brasil")
                .cep("58419-271")
                .complemento("Uma rua antes do mercado Kibom")
                .build();

        Logradouro logradouroDois = Logradouro.builder()
                .tipoLogradouro("Avenida")
                .nomeLogradouro("Avenida Beira Mar")
                .bairro("Praia de Iracema")
                .cidade("Fortaleza")
                .estado("Ceará")
                .pais("Brasil")
                .cep("60115-060")
                .complemento("Bloco B, Sala 202")
                .build();

        Logradouro logradouroTres = Logradouro.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro("Rua da Paz")
                .bairro("Ipanema")
                .cidade("Rio de Janeiro")
                .estado("Rio de Janeiro")
                .pais("Brasil")
                .cep("22071-010")
                .complemento("Casa 15")
                .build();

        logradouroUm = logradouroRepository.add(logradouroUm);
        logradouroDois = logradouroRepository.add(logradouroDois);
        logradouroTres = logradouroRepository.add(logradouroTres);

        String resultadoStr = driver.perform(MockMvcRequestBuilders.get("/v1/logradouro")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Logradouro> logradouros = objectMapper.readValue(resultadoStr, new TypeReference<List<Logradouro>>() {
        });
        Logradouro finalLogradouroUm = logradouroUm;
        Logradouro finalLogradouroDois = logradouroDois;
        Logradouro finalLogradouroTres = logradouroTres;

        assertAll(
                () -> assertTrue(!logradouros.isEmpty()),
                () -> assertTrue(logradouros.contains(finalLogradouroUm)),
                () -> assertTrue(logradouros.contains(finalLogradouroDois)),
                () -> assertTrue(logradouros.contains(finalLogradouroTres)
                ));


    }

    @Test
    @DisplayName("Quando busca um logradouro com ID válido, então esse Logradouro é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoBuscoLogradouroPorId() throws Exception {
        // Arrange
        Logradouro logradouro1 = Logradouro.builder()
                .tipoLogradouro("Floresta")
                .nomeLogradouro("do Espirito da Agua")
                .bairro("Hometree")
                .cidade("Caminho das Aguas")
                .estado("Pandora")
                .pais("Pandora")
                .cep("00000-000") // O CEP pode ser fictício em um mundo fictício
                .complemento("Area sagrada da Tribo do Caminho das Aguas")
                .build();

        Logradouro logradouro2 = Logradouro.builder()
                .tipoLogradouro("Floresta")
                .nomeLogradouro("da Vida Eterna")
                .bairro("Raiz Profunda")
                .cidade("Caminho das Almas")
                .estado("Pandora")
                .pais("Pandora")
                .cep("00000-001")
                .complemento("Local de conexão com a natureza e os espíritos")
                .build();


        logradouro1 = logradouroRepository.add(logradouro1);
        logradouro2 = logradouroRepository.add(logradouro2);

        // Act & Assert
        String resultadoStr = driver.perform(MockMvcRequestBuilders.get("/v1/logradouro/" + logradouro1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Logradouro logradouroRetornado = objectMapper.readValue(resultadoStr, Logradouro.class);

        assertEquals(logradouro1, logradouroRetornado);
    }

    @Test
    @DisplayName("Quando buscar logradouro por ID inexistente, então deve retornar LogradouroNaoExisteException")
    public void quandoBuscarLogradouroPorIdInexistente() throws Exception {
        Long idInexistente = 99989999L;
        // Act & Assert
        driver.perform(MockMvcRequestBuilders.get("/v1/logradouro/" + idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    Throwable throwable = result.getResolvedException();
                    assertNotNull(throwable);
                    assertTrue(throwable instanceof LogradouroNaoExisteException);
                    assertEquals("o logradouro não existe!", throwable.getMessage());
                });
    }


}