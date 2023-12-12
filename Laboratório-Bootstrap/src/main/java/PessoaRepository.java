import java.util.HashMap;
import java.util.Map;

public class PessoaRepository {
    private Map<String, Pessoa> pessoas;

    public PessoaRepository() {
        pessoas = new HashMap<>();
    }

    public void adicionarPessoa(Pessoa pessoa) {
        pessoas.put((String) pessoa.getCpf(), pessoa);
    }

    public Pessoa buscarPessoa(String cpf) {
        return pessoas.get(cpf);
    }

    public void atualizarPessoa(String cpf, int idade, String telefone, String profissao) {
        Pessoa pessoa = pessoas.get(cpf);
        if (pessoa != null) {
            pessoa.setIdade(idade);
            pessoa.setTelefone(telefone);
            pessoa.setProfissao(profissao);
        }
    }

    public void removerPessoa(String cpf) {
        pessoas.remove(cpf);
    }
}

