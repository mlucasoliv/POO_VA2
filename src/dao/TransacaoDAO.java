package dao;

import model.Transacao;
import util.Conexao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    public int nextId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next FROM transacao";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("next");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void salvar(int tokenId, Transacao t) {
        String sql = "INSERT INTO transacao (id, valor, datahora, token_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, t.getId());
            stmt.setDouble(2, t.getValor());
            stmt.setTimestamp(3, Timestamp.valueOf(t.getData()));
            stmt.setInt(4, tokenId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Transacao> listarPorToken(int tokenId) {
        List<Transacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacao WHERE token_id = ? ORDER BY datahora DESC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tokenId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Transacao(
                        rs.getInt("id"),
                        rs.getDouble("valor"),
                        rs.getTimestamp("datahora").toLocalDateTime()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean creditar(int id, double valor) {
        String sqlGetSaldo = "SELECT saldo FROM token WHERE usuario_id = ?";
        String sqlUpdate = "UPDATE token SET saldo = ? WHERE usuario_id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement stmtGet = conn.prepareStatement(sqlGetSaldo);
            stmtGet.setInt(1, id);
            ResultSet rs = stmtGet.executeQuery();

            if (!rs.next()) return false;

            double saldoAtual = rs.getDouble("saldo");
            double novoSaldo = saldoAtual + valor;

            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtUpdate.setDouble(1, novoSaldo);
            stmtUpdate.setInt(2, id);
            stmtUpdate.executeUpdate();

            Transacao t = new Transacao(nextId(), valor, LocalDateTime.now());
            salvar(id, t);

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean debitar(int id, double valor) {
        String sqlGetSaldo = "SELECT saldo FROM token WHERE usuario_id = ?";
        String sqlUpdate = "UPDATE token SET saldo = ? WHERE usuario_id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement stmtGet = conn.prepareStatement(sqlGetSaldo);
            stmtGet.setInt(1, id);
            ResultSet rs = stmtGet.executeQuery();

            if (!rs.next()) return false;

            double saldoAtual = rs.getDouble("saldo");
            if (saldoAtual < valor) return false;

            double novoSaldo = saldoAtual - valor;

            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtUpdate.setDouble(1, novoSaldo);
            stmtUpdate.setInt(2, id);
            stmtUpdate.executeUpdate();

            Transacao t = new Transacao(nextId(), -valor, LocalDateTime.now());
            salvar(id, t);

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transferir(int idOrigem, int idDestino, double valor) {
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            if (!debitar(idOrigem, valor)) {
                conn.rollback();
                return false;
            }

            if (!creditar(idDestino, valor)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
