public class PedidoEletronico extends Pedido implements Entregavel {
    public PedidoEletronico(int id, String nomeCliente, double valor, StatusPedido status) {
        super(id, nomeCliente, valor, status, Categoria.ELETRONICO);
    }

    @Override
    public double calcularValorFinal() {
        double desconto = (valor > 100) ? valor * 0.10 : 0.0;
        return (valor - desconto) + calcularFrete();
    }

    @Override
    public double calcularFrete() {
        return 20.0 + (valor * 0.10);
    }
}