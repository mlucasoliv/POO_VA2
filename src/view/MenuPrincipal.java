package view;

import dao.UsuarioDAO;
import dao.TransacaoDAO;
import model.Usuario;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Usuários");
            System.out.println("3. Consultar Usuário por ID");
            System.out.println("4. Creditar Token");
            System.out.println("5. Debitar Token");
            System.out.println("6. Transferir Tokens entre usuários");
            System.out.println("7. Sair");

            System.out.print("\nEscolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> cadastrarUsuario();
                case 2 -> listarUsuarios();
                case 3 -> buscarUsuarioPorId();
                case 4 -> creditarTokens();
                case 5 -> debitarTokens();
                case 6 -> transferirTokens();
                case 7 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 7);
    }

    private void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Tipo (física, jurídica): ");
        String tipo = sc.nextLine();

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setTipo(tipo);
        usuarioDAO.criar(usuario);

        System.out.println("Usuário criado com sucesso!");
    }

    private void listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listarTodos();

        System.out.println("\n=== LISTA DE USUÁRIOS ===");
        for (Usuario u : lista) {
            double saldo = (u.getToken() != null) ? u.getToken().getSaldo() : 0;
            System.out.println(
                    "ID: " + u.getId() +
                    " | Nome: " + u.getNome() +
                    " | Tipo: " + u.getTipo() +
                    " | Saldo: " + saldo
            );
        }
    }

    private void buscarUsuarioPorId() {
        System.out.print("ID do usuário: ");
        int id = sc.nextInt();
        sc.nextLine();

        Usuario u = usuarioDAO.buscarPorId(id);

        if (u != null) {
            System.out.println("\nUsuário encontrado:");
            System.out.println("ID: " + u.getId());
            System.out.println("Nome: " + u.getNome());
            System.out.println("Tipo: " + u.getTipo());
            double saldo = (u.getToken() != null) ? u.getToken().getSaldo() : 0;
            System.out.println("Saldo: " + saldo);
        } else {
            System.out.println("Usuário não encontrado!");
        }
    }

    private void creditarTokens() {
        System.out.print("ID do usuário: ");
        int id = sc.nextInt();

        System.out.print("Valor para creditar: ");
        double valor = sc.nextDouble();

        boolean ok = transacaoDAO.creditar(id, valor);

        System.out.println(ok ? "Crédito realizado!" : "Falha ao creditar.");
    }

    private void debitarTokens() {
        System.out.print("ID do usuário: ");
        int id = sc.nextInt();

        System.out.print("Valor para debitar: ");
        double valor = sc.nextDouble();

        boolean ok = transacaoDAO.debitar(id, valor);

        System.out.println(ok ? "Débito realizado!" : "Falha ao debitar (saldo insuficiente?).");
    }

    private void transferirTokens() {
        System.out.print("ID do remetente: ");
        int idOrigem = sc.nextInt();

        System.out.print("ID do destinatário: ");
        int idDestino = sc.nextInt();

        System.out.print("Valor da transferência: ");
        double valor = sc.nextDouble();

        boolean ok = transacaoDAO.transferir(idOrigem, idDestino, valor);

        System.out.println(ok ? "Transferência concluída!" : "Falha na transferência.");
    }
}
