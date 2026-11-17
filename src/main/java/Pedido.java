public abstract class Pedido implements Processavel {
    protected int id;
    protected String nomeCliente;
    protected double valor;
    protected StatusPedido status;
    protected Categoria categoria;

    public Pedido(int id, String nomeCliente, double valor, StatusPedido status, Categoria categoria) {
        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do pedido deve ser maior que zero.");
        }
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valor = valor;
        this.status = status;
        this.categoria = categoria;
    }

    @Override
    public void processarPedido() throws PedidoInvalidoException {
        if (this.status == StatusPedido.CANCELADO) {
            throw new PedidoInvalidoException("Erro: Um pedido cancelado não pode ser processado.");
        }
        if (this.status == StatusPedido.ENTREGUE) {
            throw new PedidoInvalidoException("Erro: Um pedido entregue não pode mudar de status.");
        }
        this.status = StatusPedido.PROCESSANDO;
    }

    public int getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) throws PedidoInvalidoException {
        if (this.status == StatusPedido.ENTREGUE) {
            throw new PedidoInvalidoException("Erro: Pedido já entregue, status imutável.");
        }
        this.status = status;
    }
}