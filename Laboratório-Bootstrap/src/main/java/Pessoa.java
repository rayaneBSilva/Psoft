import java.util.ArrayList;
import java.util.List;

class Pessoa {
    private String nome;
    private int idade;
    private String cpf;
    private String telefone;
    private String profissao;
    private List<Endereco> enderecos;

    public Pessoa(String nome, int idade, String cpf, String telefone, String profissao) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.telefone = telefone;
        this.profissao = profissao;
        this.enderecos = new ArrayList<>();
    }

    public Object getCpf() {
        return this.cpf;
    }

    public void setNome(String nome){
        this.nome = nome; 
    }

    public void setTelefone (String telefone){
        this.telefone = telefone; 
    }

    public void setIdade(int idade){
        this.idade = idade; 
    }

    public void setProfissao(String profissao){
        this.profissao = profissao; 
    }

    public void adicionarEndereco(Endereco endereco) {
        enderecos.add(endereco);
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", Idade: " + idade + ", CPF: " + cpf + ", Telefone: " + telefone + ", Profissão: " + profissao;
    }

    
}
