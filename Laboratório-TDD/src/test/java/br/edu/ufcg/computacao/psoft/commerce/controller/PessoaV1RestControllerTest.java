package br.edu.ufcg.computacao.psoft.commerce.controller;


import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.exception.PessoaNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Testes da Entidade Pessoa")
@AutoConfigureMockMvc
class PessoaV1RestControllerTest {
    @Autowired
    PessoaRepository pessoaRepository;
    PessoaPatchEmailDTO pessoaPatchEmailDTO;


    @Autowired
    MockMvc driver;


    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    ModelMapper modelMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        pessoaRepository.clear();
    }


    @Test
    @DisplayName("Quando criar pessoa com dados válidos, então a pessoa criada é retornada")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoCriarPessoaComDadosValidos() throws Exception {
        // Arrange
        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilápia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();


        PessoaPostPutDTO pessoaPostPutDTO = PessoaPostPutDTO.builder()
                .nome("Rayane")
                .cpf("124.982.904-60")
                .email("rayanebezerra@gmail.com")
                .listaTelefones(Collections.singletonList("1234-6987"))
                .dataNascimento("29/03/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroPostPutDTO.getNomeLogradouro())
                                .tipoLogradouro(logradouroPostPutDTO.getTipoLogradouro())
                                .bairro(logradouroPostPutDTO.getBairro())
                                .cidade(logradouroPostPutDTO.getCidade())
                                .estado(logradouroPostPutDTO.getEstado())
                                .cep(logradouroPostPutDTO.getCep())
                                .complemento(logradouroPostPutDTO.getComplemento())
                                .build()
                ))
                .profissao("cantora")
                .build();


        // Act
        String resultadoStr = driver.perform(MockMvcRequestBuilders.post("/v1/pessoa/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaPostPutDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        Pessoa pessoaResultado = objectMapper.readValue(resultadoStr, Pessoa.class);


        // Assert
        assertNotNull(pessoaResultado.getId());
        assertTrue(pessoaResultado.getId() > 0);


        assertEquals(pessoaPostPutDTO.getNome(), pessoaResultado.getNome());
        assertEquals(pessoaPostPutDTO.getCpf(), pessoaResultado.getCpf());
        assertEquals(pessoaPostPutDTO.getEmail(), pessoaResultado.getEmail());
        assertEquals(pessoaPostPutDTO.getListaTelefones(), pessoaResultado.getListaTelefones());
        assertEquals(pessoaPostPutDTO.getDataNascimento(), pessoaResultado.getDataNascimento());
        assertEquals(pessoaPostPutDTO.getListaEnderecos(), pessoaResultado.getListaEnderecos());
        assertEquals(pessoaPostPutDTO.getProfissao(), pessoaResultado.getProfissao());
    }


    @Test
    @DisplayName("Quando atualizar pessoa com dados válidos, então a pessoa é atualizada e retornada")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarPessoaComDadosValidos() throws Exception {
        // Arrange
        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilápia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();




        PessoaPostPutDTO pessoaPostPutDTO = PessoaPostPutDTO.builder()
                .nome("Rayane")
                .cpf("124.982.904-60")
                .email("raiodeSol@gmail.com")
                .listaTelefones(Collections.singletonList("1234-6987"))
                .dataNascimento("29/03/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroPostPutDTO.getNomeLogradouro())
                                .tipoLogradouro(logradouroPostPutDTO.getTipoLogradouro())
                                .bairro(logradouroPostPutDTO.getBairro())
                                .cidade(logradouroPostPutDTO.getCidade())
                                .estado(logradouroPostPutDTO.getEstado())
                                .cep(logradouroPostPutDTO.getCep())
                                .complemento(logradouroPostPutDTO.getComplemento())
                                .build()
                ))
                .profissao("OnlyFans")
                .build();






        Pessoa pessoaAtualizada = pessoaRepository.add(modelMapper.map(pessoaPostPutDTO, Pessoa.class));

        //ACT
        // atualizando email e profissao
        pessoaPostPutDTO.setEmail("rayane.raiodesola@gmail.com");
        pessoaPostPutDTO.setProfissao("onlyfans");

        // Criando uma nova lista de telefones baseada na lista existente
        // Adicioando o novo telefone à lista criada
        // Atualizando a lista de telefones no objeto pessoaPostPutDTO
        List<String> novaListaTelefones = new ArrayList<>(pessoaPostPutDTO.getListaTelefones());
        novaListaTelefones.add("telefoneatualizado");
        pessoaPostPutDTO.setListaTelefones(novaListaTelefones);










        // Act
        String resultadoStr = driver.perform(MockMvcRequestBuilders.put("/v1/pessoa/" + pessoaAtualizada.getId() + "/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(pessoaPostPutDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();




        Pessoa pessoaResultado = objectMapper.readValue(resultadoStr, Pessoa.class);




        // Assert
        assertNotNull(pessoaResultado.getId());
        assertTrue(pessoaResultado.getId() > 0);




        assertNotNull(pessoaResultado.getEmail());
        assertEquals(pessoaPostPutDTO.getEmail(), pessoaResultado.getEmail());




        assertNotNull(pessoaResultado.getProfissao());
        assertEquals(pessoaPostPutDTO.getProfissao(), pessoaResultado.getProfissao());
    }


    @Test
    @DisplayName("Quando atualizar email com dados válidos, então a pessoa com o email  atualizado é retornada")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoAtualizarEmailPessoaComDadosValidos() throws Exception {
        // Arrange
        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilápia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();


        PessoaPostPutDTO pessoaPostPutDTO = PessoaPostPutDTO.builder()
                .nome("Rayane")
                .cpf("124.982.904-60")
                .email("raiodeSol@gmail.com")
                .listaTelefones(Collections.singletonList("1234-6987"))
                .dataNascimento("29/03/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroPostPutDTO.getNomeLogradouro())
                                .tipoLogradouro(logradouroPostPutDTO.getTipoLogradouro())
                                .bairro(logradouroPostPutDTO.getBairro())
                                .cidade(logradouroPostPutDTO.getCidade())
                                .estado(logradouroPostPutDTO.getEstado())
                                .cep(logradouroPostPutDTO.getCep())
                                .complemento(logradouroPostPutDTO.getComplemento())
                                .build()
                ))
                .profissao("OnlyFans")
                .build();


        Pessoa pessoaBase = pessoaRepository.add(modelMapper.map(pessoaPostPutDTO, Pessoa.class));






        // Act


        pessoaPatchEmailDTO = PessoaPatchEmailDTO.builder()
                .email("dudinhaFofolandia@gmail.com")
                .build();


        String resultadoStr = driver.perform(MockMvcRequestBuilders.patch("/v1/pessoa/"  + pessoaBase.getId() + "/email")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(pessoaPatchEmailDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        Pessoa pessoaResultado = objectMapper.readValue(resultadoStr, Pessoa.class);


        // Assert
        assertAll(
                () -> assertNotNull(pessoaResultado.getId()),
                () -> assertNotNull(pessoaResultado.getEmail()),
                () -> assertEquals(
                        pessoaPatchEmailDTO.getEmail(),
                        pessoaResultado.getEmail())
        );


    }
    @Test
    @DisplayName("Quando excluir  uma  pessoa com id  dados válidos, Entao nada é retornado")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoExcluirPessoaComDadoValido() throws Exception {


        LogradouroPostPutDTO logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilápia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();




        PessoaPostPutDTO pessoaPostPutDTO = PessoaPostPutDTO.builder()
                .nome("Rayane")
                .cpf("124.982.904-60")
                .email("raiodeSol@gmail.com")
                .listaTelefones(Collections.singletonList("1234-6987"))
                .dataNascimento("29/03/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroPostPutDTO.getNomeLogradouro())
                                .tipoLogradouro(logradouroPostPutDTO.getTipoLogradouro())
                                .bairro(logradouroPostPutDTO.getBairro())
                                .cidade(logradouroPostPutDTO.getCidade())
                                .estado(logradouroPostPutDTO.getEstado())
                                .cep(logradouroPostPutDTO.getCep())
                                .complemento(logradouroPostPutDTO.getComplemento())
                                .build()
                ))
                .profissao("OnlyFans")
                .build();


        Pessoa pessoaBase = pessoaRepository.add(modelMapper.map(pessoaPostPutDTO, Pessoa.class));


        String resultadoStr = driver.perform(MockMvcRequestBuilders.delete("/v1/pessoa/"  + pessoaBase.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent()) // 204
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        assertAll (
                () -> assertTrue(resultadoStr.isBlank())
        );
    }


    @Test
    @DisplayName("Quando busca todas as pessoas com Id validos Entao todas as pessoas cadastradas sao retornadas")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoBuscoVariasPessoas() throws Exception {


        //Arrage
        Logradouro logradouroUm = Logradouro.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das antas")
                .bairro(("Antatunes"))
                .cidade("Cidade das antas")
                .estado("Antaturite")
                .pais("Antilápia")
                .cep("123456-678")
                .complemento("Uma rua antes a lagoa das antas")
                .build();




        Pessoa pessoaUm = Pessoa.builder()
                .nome("Rayane")
                .cpf("124.982.904-60")
                .email("raiodeSol@gmail.com")
                .listaTelefones(Collections.singletonList("1234-6987"))
                .dataNascimento("29/03/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroUm.getNomeLogradouro())
                                .tipoLogradouro(logradouroUm.getTipoLogradouro())
                                .bairro(logradouroUm.getBairro())
                                .cidade(logradouroUm.getCidade())
                                .estado(logradouroUm.getEstado())
                                .cep(logradouroUm.getCep())
                                .complemento(logradouroUm.getComplemento())
                                .build()
                ))
                .profissao("OnlyFans")
                .build();
        Logradouro logradouroDois = Logradouro.builder()
                .tipoLogradouro("Rua")
                .nomeLogradouro(" das barbizinhas")
                .bairro(("Palmirinha"))
                .cidade("Cidade Barbilandia")
                .estado("Mahatan")
                .pais("Estados Unidos")
                .cep("123456-222")
                .complemento("Uma rua antes da casa do ken ")
                .build();




        Pessoa pessoaDois = Pessoa.builder()
                .nome("Rodrigo")
                .cpf("123.982.114-60")
                .email("meuroro_mostrinho_galoDeAzeite@gmail.com")
                .listaTelefones(Collections.singletonList("1234-6987"))
                .dataNascimento("15/05/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroDois.getNomeLogradouro())
                                .tipoLogradouro(logradouroDois.getTipoLogradouro())
                                .bairro(logradouroDois.getBairro())
                                .cidade(logradouroDois.getCidade())
                                .estado(logradouroDois.getEstado())
                                .cep(logradouroDois.getCep())
                                .complemento(logradouroDois.getComplemento())
                                .build()
                ))
                .profissao("OnlyFans")
                .build();


        Logradouro logradouroTres = Logradouro.builder()
                .tipoLogradouro("Rua do Pedregal")
                .nomeLogradouro(" perto dos animais de rua")
                .bairro(("Pedregal"))
                .cidade("Cidade de Campina Grande")
                .estado("Paraiba")
                .pais("Brazil")
                .cep("123456-222")
                .complemento("Metade chique, Metade lixao")
                .build();




        Pessoa pessoaTres = Pessoa.builder()
                .nome("Joao Victor")
                .cpf("123.444.114-60")
                .email("JoaoJoao_dinossauroPHP@gmail.com")
                .listaTelefones(Collections.singletonList("4002-8922"))
                .dataNascimento("24/01/2003")
                .listaEnderecos(Collections.singletonList(
                        Logradouro.builder()
                                .nomeLogradouro(logradouroDois.getNomeLogradouro())
                                .tipoLogradouro(logradouroDois.getTipoLogradouro())
                                .bairro(logradouroDois.getBairro())
                                .cidade(logradouroDois.getCidade())
                                .estado(logradouroDois.getEstado())
                                .cep(logradouroDois.getCep())
                                .complemento(logradouroDois.getComplemento())
                                .build()
                ))
                .profissao("Dinossauro do php")
                .build();


        pessoaUm = pessoaRepository.add(pessoaUm);
        pessoaDois = pessoaRepository.add(pessoaDois);
        pessoaTres = pessoaRepository.add(pessoaTres);




        String resultadoStr = driver.perform(MockMvcRequestBuilders.get("/v1/pessoa")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


        List<Pessoa> pessoas = objectMapper.readValue(resultadoStr, new TypeReference<List<Pessoa>>() {});


        Pessoa finalPessoaUm = pessoaUm;
        Pessoa finalPessoaDois = pessoaDois;
        Pessoa finalPessoaTres = pessoaTres;


        assertAll (
                () -> assertTrue(!pessoas.isEmpty()),
                () -> assertTrue(pessoas.contains(finalPessoaUm)),
                () -> assertTrue(pessoas.contains(finalPessoaDois)),
                () -> assertTrue(pessoas.contains(finalPessoaTres))


        );
    }
    @Test
    @DisplayName("Quando busca uma pessoa com ID válido, então essa pessoa é retornada")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void quandoBuscoPessoaPorId() throws Exception {
        // Arrange
        Logradouro logradouro = Logradouro.builder()
                .tipoLogradouro("Floresta")
                .nomeLogradouro("do Espirito da Agua")
                .bairro("Hometree")
                .cidade("Caminho das Aguas")
                .estado("Pandora")
                .pais("Pandora")
                .cep("00000-000") // O CEP pode ser fictício em um mundo fictício
                .complemento("Area sagrada da Tribo do Caminho das Aguas")
                .build();

        Pessoa pessoaUm = Pessoa.builder()
                .nome("Neteyam")
                .cpf("000.000.000-00") // Um CPF fictício
                .email("neteyam@triboaguas.pandora")
                .listaTelefones(Collections.singletonList("0000-0000")) // Um telefone fictício
                .dataNascimento("Desconhecida")
                .listaEnderecos(Collections.singletonList(logradouro))
                .profissao("Guia Espiritual da Tribo do Caminho das Aguas")
                .build();

        pessoaUm = pessoaRepository.add(pessoaUm);

        // Act & Assert
        String resultadoStr = driver.perform(MockMvcRequestBuilders.get("/v1/pessoa/" + pessoaUm.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Pessoa pessoaRetornada = objectMapper.readValue(resultadoStr, Pessoa.class);

        assertEquals(pessoaUm, pessoaRetornada);
    }

    @Test
    @DisplayName("Quando buscar pessoa por ID inexistente, então deve retornar PessoaNaoExisteException")
    public void quandoBuscarPessoaPorIdInexistente() throws Exception {
        Long idInexistente = 99989999L;
        // Act & Assert
        driver.perform(MockMvcRequestBuilders.get("/v1/pessoa/" + idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Codigo 404
                .andDo(print())
                .andExpect(result -> {
                    Throwable throwable = result.getResolvedException();
                    assertNotNull(throwable);
                    assertTrue(throwable instanceof PessoaNaoExisteException);
                    assertEquals("A pessoa não existe!", throwable.getMessage());
                });
    }

    @Test
    @DisplayName("Quando buscar pessoa por ID inexistente, então deve retornar PessoaNaoExisteException")
    public void quandoExcluirPessoaPorIdInexistente() throws Exception {
        Long idInexistente = 99989999L;
        // Act & Assert
        driver.perform(MockMvcRequestBuilders.delete("/v1/pessoa/" + idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Codigo 404
                .andDo(print())
                .andExpect(result -> {
                    Throwable throwable = result.getResolvedException();
                    assertNotNull(throwable);
                    assertTrue(throwable instanceof PessoaNaoExisteException);
                    assertEquals("A pessoa não existe!", throwable.getMessage());
                });
    }



}