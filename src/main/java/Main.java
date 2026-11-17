import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- SISTEMA DE GERENCIAMENTO DE PEDIDOS ---");
            System.out.println("1. Cadastrar pedido");
            System.out.println("2. Alterar status de um pedido");
            System.out.println("3. Processar pedidos");
            System.out.println("4. Remover cancelados");
            System.out.println("5. Exibir lista de pedidos");
            System.out.println("6. Exibir pedidos entregáveis");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opçao: ");
            opcao = scanner.nextInt();

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("ID do pedido: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nome do cliente: ");
                        String cliente = scanner.nextLine();
                        System.out.print("Valor do pedido: R$ ");
                        double valor = scanner.nextDouble();
                        System.out.println("Categoria (1-ELETRONICO, 2-ROUPA, 3-ALIMENTO): ");
                        int catOp = scanner.nextInt();

                        Pedido novoPedido = null;
                        if (catOp == 1) {
                            novoPedido = new PedidoEletronico(id, cliente, valor, StatusPedido.PENDENTE);
                        } else if (catOp == 2) {
                            novoPedido = new PedidoRoupa(id, cliente, valor, StatusPedido.PENDENTE);
                        } else if (catOp == 3) {
                            novoPedido = new PedidoAlimento(id, cliente, valor, StatusPedido.PENDENTE);
                        }

                        if (novoPedido != null) {
                            gerenciador.adicionar(novoPedido);
                            System.out.println("Pedido cadastrado com sucesso!");
                        }
                        break;

                    case 2:
                        System.out.print("Informe o ID do pedido que deseja alterar: ");
                        int buscaId = scanner.nextInt();
                        Pedido pedidoEncontrado = gerenciador.buscarPorId(buscaId);
                        if (pedidoEncontrado != null) {
                            System.out.println("Novo status (0-PENDENTE, 1-PROCESSANDO, 2-ENVIADO, 3-ENTREGUE, 4-CANCELADO): ");
                            int statusOp = scanner.nextInt();
                            StatusPedido novoStatus = StatusPedido.values()[statusOp];
                            pedidoEncontrado.setStatus(novoStatus);
                            System.out.println("Status alterado com sucesso!");
                        } else {
                            System.out.println("Pedido não encontrado.");
                        }
                        break;

                    case 3:
                        gerenciador.processarElegiveis();
                        System.out.println("Todos os pedidos elegíveis foram processados.");
                        break;

                    case 4:
                        gerenciador.removerCancelados();
                        System.out.println("Pedidos cancelados removidos da lista.");
                        break;

                    case 5:
                        System.out.println("\n--- LISTA DE PEDIDOS ATIVOS ---");
                        for (Pedido p : gerenciador.listarAtivos()) {
                            System.out.println("ID: " + p.getId() + " | Cliente: " + p.getNomeCliente() + " | Status: " + p.getStatus() + " | Valor Final: R$ " + p.calcularValorFinal());
                        }
                        break;

                    case 6:
                        System.out.println("\n--- PEDIDOS ENTREGAVEIS ---");
                        for (Entregavel e : gerenciador.listarEntregaveis()) {
                            Pedido p = (Pedido) e;
                            System.out.println("ID: " + p.getId() + " | Cliente: " + p.getNomeCliente() + " | Frete: R$ " + e.calcularFrete());
                        }
                        break;

                    case 7:
                        System.out.println("Encerrando o sistema...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro capturado: " + e.getMessage());
            }

        } while (opcao != 7);
        
        scanner.close();
    }
}