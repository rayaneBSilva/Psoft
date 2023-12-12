package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


@RestController
@RequestMapping (
        value= "/pessoas", produces = MediaType.APPLICATION_JSON_VALUE
)

public class PessoaV1RestController {
    private Map<Long, Pessoa> pessoaRepository;
    private Long nextId;

    public PessoaV1RestController() {
        this.pessoaRepository = new HashMap<>();
        this.nextId = 1L;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> createPessoa(@RequestBody Pessoa pessoa) {

        if(!toCheckCpfValidate(pessoa.getCpf())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Cpf inválido");
        }

        if(pessoaAlreadyRegistered(pessoa.getNome(), pessoa.getCpf())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Pessoa já cadastrada");
        }

        pessoa.setId(nextId++);
        pessoa.setEnderecoIds(new ArrayList<>());

        this.pessoaRepository.put(pessoa.getId(), pessoa);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pessoa);
    }

    @GetMapping
    public ResponseEntity<?> listPessoas(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pessoaRepository.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPessoa(@PathVariable Long id) {
        Pessoa pessoa = this.pessoaRepository.get(id);

        return (pessoa != null) ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePessoa(@PathVariable Long id , @RequestBody Pessoa pessoaAtualizada) {
        if (!this.pessoaRepository.containsKey(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pessoa não encontrada");
        }

        Pessoa pessoaExistente = this.pessoaRepository.get(id);

        pessoaExistente.setEmail(pessoaAtualizada.getEmail());
        pessoaExistente.setTelefone(pessoaAtualizada.getTelefone());
        pessoaExistente.setDataNascimento(pessoaAtualizada.getDataNascimento());
        pessoaExistente.setEnderecoIds(pessoaAtualizada.getEnderecoIds());
        pessoaExistente.setProfissao(pessoaAtualizada.getProfissao());

        if (pessoaAtualizada.getEnderecoIds() != null) {
            pessoaExistente.setEnderecoIds(pessoaAtualizada.getEnderecoIds());
        }

        pessoaRepository.put(id, pessoaExistente);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaExistente);


    }

    @PutMapping("/{id}/adicionar-endereco/{enderecoId}")
    public ResponseEntity<?> adicionarEndereco(
            @PathVariable("id") Long pessoaId,
            @PathVariable("enderecoId") Long enderecoId
    ) {
        if (pessoaRepository.containsKey(pessoaId)) {
            Pessoa pessoa = pessoaRepository.get(pessoaId);
            if (!pessoa.getEnderecoIds().contains(enderecoId)) {
                pessoa.getEnderecoIds().add(enderecoId);
                pessoaRepository.put(pessoaId, pessoa);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Endereço adicionado com sucesso");
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Endereço já associado a esta pessoa");
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pessoa não encontrada");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody Pessoa pessoaAtualizada){
       if (this.pessoaRepository.containsKey(id)) {
           Pessoa pessoa = this.pessoaRepository.get(id);
           pessoa.setEmail(pessoaAtualizada.getEmail());

           pessoaRepository.put(id, pessoa);

           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(pessoa);
       } else {
           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body("Pessoa não encontrada");
       }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoa (@PathVariable Long id) {
        Pessoa pessoaExistente = this.pessoaRepository.get(id);

        return (pessoaExistente != null) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }

    private boolean toCheckCpfValidate(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", " ");
        return cpfLimpo.length() == 11;
    }

    private boolean pessoaAlreadyRegistered (String nome, String cpf) {
        return this.pessoaRepository.values().stream()
                .anyMatch(pessoa -> pessoa.getNome().equals(nome) && pessoa.getCpf().equals(cpf));
    }

}