import java.util.List;

public class PessoaController {
    private PessoaRepository repository;

    public PessoaController() {
        repository = new PessoaRepository();
    }

    public void criarPessoa(String nome, int idade, String cpf, String telefone, String profissao) {
        Pessoa pessoa = new Pessoa(nome, idade, cpf, telefone, profissao);
        repository.adicionarPessoa(pessoa);
    }

    public Pessoa buscarPessoa(String cpf) {
        return repository.buscarPessoa(cpf);
    }

    public void atualizarPessoa(String cpf,  int idade, String telefone, String profissao) {
        repository.atualizarPessoa(cpf, idade, telefone, profissao);
    }

    public void removerPessoa(String cpf) {
        repository.removerPessoa(cpf);
    }

   
}
