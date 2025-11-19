package model;

public class Usuario {

    private int id;
    private String nome;
    private String tipo;
    private Token token;

    public Usuario() {}

    public Usuario(int id, String nome, String tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Token getToken() { return token; }
    public void setToken(Token token) { this.token = token; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', tipo='" + tipo + "'}";
    }
}
