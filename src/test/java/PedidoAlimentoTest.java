import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoAlimentoTest {
    private PedidoAlimento pedido;

    @BeforeEach
    void preparar() {
        pedido = new PedidoAlimento(1, "Ana", 50.0, StatusPedido.PENDENTE);
    }

    @AfterEach
    void finalizar(org.junit.jupiter.api.TestInfo info) {
        System.out.println("Teste finalizado: " + info.getDisplayName());
    }

    @Test
    void valorFinalEOValorMaisCincoReaisDeTaxa() {
        assertEquals(55.0, pedido.calcularValorFinal(), 0.001);
    }

    @Test
    void categoriaEAlimento() {
        assertEquals(Categoria.ALIMENTO, pedido.getCategoria());
    }

    @Test
    void naoEEntregavel() {
        assertFalse(pedido instanceof Entregavel);
    }

    @Test
    void valorNegativoLancaValorInvalidoException() {
        assertThrows(ValorInvalidoException.class,
                () -> new PedidoAlimento(2, "Bruno", -1.0, StatusPedido.PENDENTE));
    }
}
