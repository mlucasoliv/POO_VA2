package dao;

import model.Usuario;
import model.Token;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void criar(Usuario usuario) {
    String sqlUsuario = "INSERT INTO usuario (nome, tipo) VALUES (?, ?) RETURNING id";
    String sqlToken = "INSERT INTO token (saldo, usuario_id) VALUES (?, ?)";

    try (Connection conn = Conexao.getConnection()) {

        // 1️⃣ Cria usuário
        PreparedStatement stmtUser = conn.prepareStatement(sqlUsuario);
        stmtUser.setString(1, usuario.getNome());
        stmtUser.setString(2, usuario.getTipo());

        ResultSet rs = stmtUser.executeQuery();
        if (rs.next()) {
            usuario.setId(rs.getInt("id"));
        }

        // 2️⃣ Inicializa token se não existir
        if (usuario.getToken() == null) {
            usuario.setToken(new Token(0, 0.0));
        }

        // 3️⃣ Cria token
        PreparedStatement stmtToken = conn.prepareStatement(sqlToken);
        stmtToken.setDouble(1, usuario.getToken().getSaldo());
        stmtToken.setInt(2, usuario.getId());
        stmtToken.executeUpdate();

        System.out.println("Usuário criado com sucesso! ID: " + usuario.getId());

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public Usuario buscarPorId(int id) {
        String sql = """
                SELECT u.id, u.nome, u.tipo, t.id AS token_id, t.saldo
                FROM usuario u
                JOIN token t ON t.usuario_id = u.id
                WHERE u.id = ?
            """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setTipo(rs.getString("tipo"));

                Token token = new Token(
                        rs.getInt("token_id"),
                        rs.getDouble("saldo")
                );

                u.setToken(token);
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();

        String sql = """
                SELECT u.id, u.nome, u.tipo, t.id AS token_id, t.saldo
                FROM usuario u
                JOIN token t ON t.usuario_id = u.id
            """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setTipo(rs.getString("tipo"));

                Token token = new Token(
                        rs.getInt("token_id"),
                        rs.getDouble("saldo")
                );

                u.setToken(token);
                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void deletar(int id) {
        String sqlToken = "DELETE FROM token WHERE usuario_id = ?";
        String sqlUser = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = Conexao.getConnection()) {

            PreparedStatement stmtToken = conn.prepareStatement(sqlToken);
            stmtToken.setInt(1, id);
            stmtToken.executeUpdate();

            PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
            stmtUser.setInt(1, id);
            stmtUser.executeUpdate();

            System.out.println("Usuário deletado!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
