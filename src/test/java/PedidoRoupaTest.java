import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoRoupaTest {
    private PedidoRoupa semDesconto;
    private PedidoRoupa comDesconto;

    @BeforeEach
    void preparar() {
        semDesconto = new PedidoRoupa(1, "Ana", 200.0, StatusPedido.PENDENTE);
        comDesconto = new PedidoRoupa(2, "Bruno", 300.0, StatusPedido.PENDENTE);
    }

    @AfterEach
    void finalizar(org.junit.jupiter.api.TestInfo info) {
        System.out.println("Teste finalizado: " + info.getDisplayName());
    }

    @Test
    void freteEDezMaisCincoPorCentoDoValor() {
        assertEquals(20.0, semDesconto.calcularFrete(), 0.001);
        assertEquals(25.0, comDesconto.calcularFrete(), 0.001);
    }

    @Test
    void valorFinalNaoTemDescontoQuandoValorEhDuzentosOuMenos() {
        // 200 + frete 20
        assertEquals(220.0, semDesconto.calcularValorFinal(), 0.001);
    }

    @Test
    void valorFinalTemDescontoDeVintePorCentoAcimaDeDuzentos() {
        // 300 - 60 de desconto + frete 25
        assertEquals(265.0, comDesconto.calcularValorFinal(), 0.001);
    }

    @Test
    void categoriaERoupa() {
        assertEquals(Categoria.ROUPA, semDesconto.getCategoria());
    }

    @Test
    void eEntregavel() {
        assertInstanceOf(Entregavel.class, semDesconto);
    }

    @Test
    void valorZeroLancaValorInvalidoException() {
        assertThrows(ValorInvalidoException.class,
                () -> new PedidoRoupa(3, "Carla", 0.0, StatusPedido.PENDENTE));
    }
}
