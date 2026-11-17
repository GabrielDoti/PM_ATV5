import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Regras comuns a todos os tipos de pedido (status, processamento, exceções). */
class PedidoTest {
    private List<Pedido> todosOsTipos;

    @BeforeEach
    void preparar() {
        todosOsTipos = List.of(
                new PedidoEletronico(1, "Ana", 150.0, StatusPedido.PENDENTE),
                new PedidoRoupa(2, "Bruno", 150.0, StatusPedido.PENDENTE),
                new PedidoAlimento(3, "Carla", 150.0, StatusPedido.PENDENTE));
    }

    @AfterEach
    void finalizar(org.junit.jupiter.api.TestInfo info) {
        System.out.println("Teste finalizado: " + info.getDisplayName());
    }

    // ---- processamento: validar alteração do status ----

    @Test
    void processarPedidoPendenteVaiParaProcessando() throws PedidoInvalidoException {
        for (Pedido p : todosOsTipos) {
            p.processarPedido();
            assertEquals(StatusPedido.PROCESSANDO, p.getStatus(), p.getClass().getSimpleName());
        }
    }

    @Test
    void processarPedidoEnviadoVaiParaProcessando() throws PedidoInvalidoException {
        Pedido p = new PedidoAlimento(9, "Dani", 10.0, StatusPedido.ENVIADO);
        p.processarPedido();
        assertEquals(StatusPedido.PROCESSANDO, p.getStatus());
    }

    @Test
    void processarPedidoCanceladoLancaPedidoInvalidoException() throws PedidoInvalidoException {
        for (Pedido p : todosOsTipos) {
            p.setStatus(StatusPedido.CANCELADO);
            PedidoInvalidoException e = assertThrows(PedidoInvalidoException.class, p::processarPedido);
            assertEquals("Erro: Um pedido cancelado não pode ser processado.", e.getMessage());
            assertEquals(StatusPedido.CANCELADO, p.getStatus(), "status não deve mudar");
        }
    }

    @Test
    void processarPedidoEntregueLancaPedidoInvalidoException() throws PedidoInvalidoException {
        for (Pedido p : todosOsTipos) {
            p.setStatus(StatusPedido.ENTREGUE);
            PedidoInvalidoException e = assertThrows(PedidoInvalidoException.class, p::processarPedido);
            assertEquals("Erro: Um pedido entregue não pode mudar de status.", e.getMessage());
            assertEquals(StatusPedido.ENTREGUE, p.getStatus());
        }
    }

    // ---- regra de enum: ENTREGUE não pode ser alterado ----

    @Test
    void pedidoEntregueNaoPodeIrParaNenhumOutroStatus() throws PedidoInvalidoException {
        for (Pedido p : todosOsTipos) {
            p.setStatus(StatusPedido.ENTREGUE);
            for (StatusPedido novo : StatusPedido.values()) {
                PedidoInvalidoException e = assertThrows(PedidoInvalidoException.class, () -> p.setStatus(novo));
                assertEquals("Erro: Pedido já entregue, status imutável.", e.getMessage());
                assertEquals(StatusPedido.ENTREGUE, p.getStatus(), "tentou ir para " + novo);
            }
        }
    }

    @Test
    void statusNaoEntreguePodeSerAlterado() throws PedidoInvalidoException {
        for (StatusPedido inicial : StatusPedido.values()) {
            if (inicial == StatusPedido.ENTREGUE) {
                continue;
            }
            Pedido p = new PedidoRoupa(10, "Edu", 50.0, inicial);
            p.setStatus(StatusPedido.ENVIADO);
            assertEquals(StatusPedido.ENVIADO, p.getStatus(), "de " + inicial);
        }
    }

    // ---- exceções personalizadas ----

    @Test
    void valorInvalidoExceptionEUncheckedEPedidoInvalidoExceptionEChecked() {
        assertInstanceOf(RuntimeException.class, new ValorInvalidoException("x"));
        Exception checked = new PedidoInvalidoException("x");
        assertFalse(checked instanceof RuntimeException);
    }

    @Test
    void todosOsTiposRejeitamValorZeroENegativo() {
        assertThrows(ValorInvalidoException.class, () -> new PedidoEletronico(1, "A", 0, StatusPedido.PENDENTE));
        assertThrows(ValorInvalidoException.class, () -> new PedidoRoupa(1, "A", -0.01, StatusPedido.PENDENTE));
        assertThrows(ValorInvalidoException.class, () -> new PedidoAlimento(1, "A", -50, StatusPedido.PENDENTE));
    }

    // ---- exceções do Java ----

    @Test
    void statusInexistenteNoEnumLancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> StatusPedido.valueOf("DEVOLVIDO"));
    }

    @Test
    void opcaoDeStatusForaDoIntervaloLancaArrayIndexOutOfBoundsException() {
        // É o que o menu faz com StatusPedido.values()[opcao]
        StatusPedido[] todos = StatusPedido.values();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            StatusPedido invalido = todos[todos.length];
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            StatusPedido invalido = todos[-1];
        });
    }

    @Test
    void removerDaListaEnquantoPercorreLancaConcurrentModificationException() {
        java.util.List<Pedido> lista = new java.util.ArrayList<>(todosOsTipos);
        assertThrows(java.util.ConcurrentModificationException.class, () -> {
            for (Pedido p : lista) {
                lista.remove(p);
            }
        });
    }

    @Test
    void usarPedidoNuloLancaNullPointerException() {
        Pedido nulo = null;
        assertThrows(NullPointerException.class, () -> nulo.getStatus());
    }
}
