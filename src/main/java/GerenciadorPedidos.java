import java.util.ArrayList;
import java.util.List;

/**
 * Regras de negócio sobre a lista de pedidos. Antes ficavam espalhadas no
 * Main; agora o Main só lê o teclado e imprime, e estas regras podem ser
 * testadas sem depender do Scanner.
 */
public class GerenciadorPedidos {
    private final List<Pedido> pedidos = new ArrayList<>();

    public void adicionar(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    /** Devolve o pedido com o id informado ou null se não existir. */
    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /** Processa só os pedidos elegíveis; cancelados e entregues são ignorados. */
    public int processarElegiveis() {
        int processados = 0;
        for (Pedido p : pedidos) {
            StatusPedido status = p.getStatus();
            if (status == StatusPedido.CANCELADO || status == StatusPedido.ENTREGUE) {
                continue;
            }
            try {
                p.processarPedido();
                processados++;
            } catch (PedidoInvalidoException e) {
                // não acontece: os status inválidos foram filtrados acima
            }
        }
        return processados;
    }

    /** Remove da lista os pedidos cancelados e diz quantos saíram. */
    public int removerCancelados() {
        int antes = pedidos.size();
        pedidos.removeIf(p -> p.getStatus() == StatusPedido.CANCELADO);
        return antes - pedidos.size();
    }

    public List<Pedido> listarAtivos() {
        List<Pedido> ativos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getStatus() != StatusPedido.CANCELADO) {
                ativos.add(p);
            }
        }
        return ativos;
    }

    public List<Entregavel> listarEntregaveis() {
        List<Entregavel> entregaveis = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p instanceof Entregavel) {
                entregaveis.add((Entregavel) p);
            }
        }
        return entregaveis;
    }
}
