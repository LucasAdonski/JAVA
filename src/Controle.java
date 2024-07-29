import java.io.*;
import java.util.ArrayList;

public class Controle{
    private static final String ARQUIVO_PRODUTOS = "produtos.txt";
    private static final String ARQUIVO_PEDIDOS= "pedidos.txt";
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public Controle(){
        carregarProdutos();
        carregarPedidos();
    }

    private void carregarProdutos(){
        try(BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PRODUTOS))){ //inicia o buffer repare no reader de ler em vez de escrever aqui ele le para poder atribuir a info do buffer nas variaveis
            String linha;
            while((linha = br.readLine()) != null){
                String[] dados = linha.split(";");
                int codigo = Integer.parseInt(dados[0]);
                String nome = dados[1];
                double preco = Double.parseDouble(dados[2]);
                int quantidade = Integer.parseInt(dados[3]);
                produtos.add(new Produto(codigo, nome, preco, quantidade)); // e aqui ele instancia a class para colocar as info do ARQUIVO no produto
            }
        }
        catch(IOException e){
            System.out.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void carregarPedidos(){
        try(BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PEDIDOS))){
            String linha;
            while((linha = br.readLine()) != null){
                String[] dados = linha.split(";");
                int numero = Integer.parseInt(dados[0]);
                int codigoProduto = Integer.parseInt(dados[1]);
                double preco = Double.parseDouble(dados[2]);
                int quantidade = Integer.parseInt(dados[3]);
                pedidos.add(new Pedido(numero, codigoProduto, preco, quantidade));
            }
        }
        catch(IOException e){
            System.out.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void salvarProduto(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PRODUTOS))){ // repare que aqui ele escreve resgatando o buffer e escrevendo nele os gets
            for(Produto produto : produtos){ //for each para percorrer o ArrayList de produtos
                bw.write(produto.getCodigoProduto() + ";" + produto.getNomeProduto() + ";" + produto.getPrecoUnitario() + ";" + produto.getQuantidadeEstoque()); // escrevendo no buffer cada get por linha
                bw.newLine(); // gera uma nova linha quando termina de os gets de cada produto
            }
        }
        catch(IOException e){
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }

    private void salvarPedido(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PEDIDOS))){ 
            for(Pedido pedido : pedidos){
                bw.write(pedido.getNumeroPedido() + ";" + pedido.getCodigoProduto() + ";" + pedido.getPrecoUnitario() + ";" + pedido.getQuantidade());
                bw.newLine();
            }
        }
        catch(IOException e){
            System.out.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }

    public void incluirProduto(String nome, double preco, int quantidade){ //recupera o nome, preco, quantidade do produto
        int codigo = produtos.size() + 1; // aqui é interessante! o size() diz o tamanho do ArrayList de produtos e inclui + 1 no codigo como se fosse um "auto increment" no mysql
        Produto produto = new Produto(codigo, nome, preco, quantidade); // inclui as info nas variaveis e instancia a class
        produtos.add(produto); //adiciona no Array
        salvarProduto(); //salva no arquivo de texto
    }

    public void editarQuantidadeEstoque(int codigo, int novaQuantidade){
        Produto produto = buscaProduto(codigo); //busca o produto com base no codigo fornecido pelo usuario
        if(produto != null){ //se o codigo fornecido nao for nulo
            produto.setQuantidadeEstoque(novaQuantidade);//atualiza pelo set de quantidade em estoque
            salvarProduto();//salva no arquivo de texto
            System.out.println("Quantidade em estoque atualizada com sucesso!");
        }
        else{
            System.out.println("Produto não encontrado!");
        }
    }

    public void editarPrecoUnitario(int codigo, double novoPreco){
        Produto produto = buscaProduto(codigo);
        if(produto != null){
            produto.setPrecoUnitario(novoPreco);//atualiza pelo set do preco unitario
            salvarProduto();
            System.out.println("Preço unitário atualizado com sucesso");
        }
        else{
            System.out.println("Produto não encontrado");
        }
    }

    public void excluirProduto(int codigo) {
        Produto produto = buscaProduto(codigo);
        if(produto != null){
            produtos.remove(produto);// Remove o produto do ArrayList 
            salvarProduto();//salva a remoção no arquivo de texto
            System.out.println("Produto excluido com sucesso!");
        }
        else{
            System.out.println("Produto não encontrado!");
        }
    }

    public void listarProdutos(){
        if(produtos.isEmpty()){ // Se o ArrayList de produtos estiver vazio
            System.out.println("Nenhum produto cadastado!");
            return; //sai da funçao
        }

        System.out.printf("%-10s %-50s %-15s %-10s\n", "Código", "Nome", "Preço unitário", "Quantidade estoque"); //(-) significa que o conteudo sera alinha a esquerda e (10) ou (50) a quantidade que o campo ocupará
        System.out.println("--------------------------------------------------------------------------------------");

        double valorTotalEstoque = 0;

        for(Produto produto : produtos){
            System.out.printf("%-10s %-50s %-15.2f %-10d\n", produto.getCodigoProduto(), produto.getNomeProduto(), produto.getPrecoUnitario(), produto.getQuantidadeEstoque());
            valorTotalEstoque += produto.getPrecoUnitario() * produto.getQuantidadeEstoque(); //gera o valor total do em dinheiro do estoque
        }

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("Valor total estoque: %.2f\n", valorTotalEstoque); //printa o valor total
    }

    public Produto buscaProduto(int codigo){ // envia o codigo como paramentro
        for(Produto produto : produtos){ //for each para percorrer o ArrayList 
            if(produto.getCodigoProduto() == codigo){ // verifica se existe o id
                return produto; //retorna o produto com o id do paramentro
            }
        }
        return null; // se nao existir retorna nulo
    }

    public void novoPedido(int codigoProduto, int quantidade){ // envia o codigo e a quantidade desejada por parametro
        Produto produto = buscaProduto(codigoProduto); //busca o produto pelo id
        if(produto != null && produto.getQuantidadeEstoque() >= quantidade){ // se o produto nao retorna nulo e existir uma quantidade maior no estoque do que foi solicitado ele faz o pedido
            int numeroPedido = pedidos.size() + 1; // gera o id "auto increment"
            Pedido novoPedido = new Pedido(numeroPedido, codigoProduto, produto.getPrecoUnitario(), quantidade); //instancia a class adicionando o codigo do produto 
            pedidos.add(novoPedido); //adiciona ao ArrayList
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade); // Atualiza o estoque e retira a quantidade que foi gerada no pedido
            salvarProduto(); // salva no ARQUIVO.txt
            salvarPedido(); // salva no ARQUIVO.txt
            System.out.println("Pedido salvo com sucesso!");
        }
        else{
            System.out.println("Estoque insuficiente ou produto não encontrado!");
        }
    }

    public void listarPedidos(){
        if(pedidos.isEmpty()){ //verifica se o pedido nao esta vazio
            System.out.println("Nenhum pedido cadastrado!");
            return;
        }

        for(Pedido pedido : pedidos){ //percorre o arraylist de pedidos
            System.out.printf("Pedido número: %d\n", pedido.getNumeroPedido());// imprime o id do pedido
            Produto produto = buscaProduto(pedido.getCodigoProduto()); //busca o codigo do produto que foi atualizado pela funcao novoPedido()
            if(produto != null){ // verifica se existe o produto
                double subtotal = pedido.getPrecoUnitario() * pedido.getQuantidade(); //calcula o total
                System.out.printf("%-50s %-15.2f %-10d %-15.2f\n", produto.getNomeProduto(), pedido.getPrecoUnitario(), pedido.getQuantidade(), subtotal); //impressão
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("Valor total pedido: %.2f\n", subtotal);
            }
            else{
                System.out.println("Pedido não encontrado!");
            }
        }
    }

}