package model;

import java.time.LocalDateTime;

public class Transacao {

    private int id;
    private double valor;
    private LocalDateTime data;

    public Transacao() {}

    public Transacao(int id, double valor, LocalDateTime data) {
        this.id = id;
        this.valor = valor;
        this.data = data;
    }

    public int getId() { return id; }
    public double getValor() { return valor; }
    public LocalDateTime getData() { return data; }

    @Override
    public String toString() {
        return "Transacao{id=" + id + ", valor=" + valor + ", data=" + data + "}";
    }
}
