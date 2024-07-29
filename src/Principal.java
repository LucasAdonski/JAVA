import java.util.Scanner;

public class Principal {
    private static Controle controle = new Controle();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("# Menu principal #");
            System.out.println("[1] Produtos");
            System.out.println("[2] Pedidos");
            System.out.println("[3] Sair");
            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    menuProdutos(scanner);
                    break;
                case 2:
                    menuPedidos(scanner);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuPedidos(Scanner scanner) {
        while (true) {
            System.out.println("# Pedidos #");
            System.out.println("[1] Novo pedido");
            System.out.println("[2] Listagem pedidos");
            System.out.println("[3] Voltar ao menu principal");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    System.out.print("Código do produto: ");
                    int codigoProduto = scanner.nextInt();
                    System.out.print("Quantidade: ");
                    int quantidade = scanner.nextInt();
                    controle.novoPedido(codigoProduto, quantidade);
                    break;
                case 2:
                    controle.listarPedidos();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuProdutos(Scanner scanner) {
        while (true) {
            System.out.println("# Produtos #");
            System.out.println("[1] Incluir produto");
            System.out.println("[2] Editar quantidade estoque produto");
            System.out.println("[3] Editar preço unitário produto");
            System.out.println("[4] Excluir produto");
            System.out.println("[5] Listagem produtos");
            System.out.println("[6] Voltar ao menu principal");
            int escolha = scanner.nextInt();
            scanner.nextLine();  

            switch (escolha) {
                case 1:
                    System.out.print("Nome do produto: ");
                    String nome = scanner.nextLine();
                    System.out.print("Preço unitário: ");
                    double preco = scanner.nextDouble();
                    System.out.print("Quantidade em estoque: ");
                    int quantidade = scanner.nextInt();
                    controle.incluirProduto(nome, preco, quantidade);
                    break;
                case 2:
                    System.out.print("Código do produto: ");
                    int codigo = scanner.nextInt();
                    System.out.print("Nova quantidade: ");
                    int novaQuantidade = scanner.nextInt();
                    controle.editarQuantidadeEstoque(codigo, novaQuantidade);
                    break;
                case 3:
                    System.out.print("Código do produto: ");
                    int codigoPreco = scanner.nextInt();
                    System.out.print("Novo preço: ");
                    double novoPreco = scanner.nextDouble();
                    controle.editarPrecoUnitario(codigoPreco, novoPreco);
                    break;
                case 4:
                    System.out.print("Código do produto: ");
                    int codigoExcluir = scanner.nextInt();
                    controle.excluirProduto(codigoExcluir);
                    break;
                case 5:
                    controle.listarProdutos();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
