package model;

public class Token {

    private int id;
    private double saldo;

    public Token() {}

    public Token(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    @Override
    public String toString() {
        return "Token{id=" + id + ", saldo=" + saldo + "}";
    }
}
