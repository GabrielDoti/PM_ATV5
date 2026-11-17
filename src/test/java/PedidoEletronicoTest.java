import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoEletronicoTest {
    private PedidoEletronico semDesconto;
    private PedidoEletronico comDesconto;

    @BeforeEach
    void preparar() {
        semDesconto = new PedidoEletronico(1, "Ana", 100.0, StatusPedido.PENDENTE);
        comDesconto = new PedidoEletronico(2, "Bruno", 200.0, StatusPedido.PENDENTE);
    }

    @AfterEach
    void finalizar(org.junit.jupiter.api.TestInfo info) {
        System.out.println("Teste finalizado: " + info.getDisplayName());
    }

    @Test
    void freteEVinteMaisDezPorCentoDoValor() {
        assertEquals(30.0, semDesconto.calcularFrete(), 0.001);
        assertEquals(40.0, comDesconto.calcularFrete(), 0.001);
    }

    @Test
    void valorFinalNaoTemDescontoQuandoValorEhCemOuMenos() {
        // 100 + frete 30
        assertEquals(130.0, semDesconto.calcularValorFinal(), 0.001);
    }

    @Test
    void valorFinalTemDescontoDeDezPorCentoAcimaDeCem() {
        // 200 - 20 de desconto + frete 40
        assertEquals(220.0, comDesconto.calcularValorFinal(), 0.001);
    }

    @Test
    void descontoComecaDepoisDeCem() {
        PedidoEletronico logoAcima = new PedidoEletronico(3, "Carla", 100.01, StatusPedido.PENDENTE);
        double semDescontoEsperado = 100.01 + logoAcima.calcularFrete();
        assertTrue(logoAcima.calcularValorFinal() < semDescontoEsperado);
    }

    @Test
    void categoriaEEletronico() {
        assertEquals(Categoria.ELETRONICO, semDesconto.getCategoria());
    }

    @Test
    void eEntregavel() {
        assertInstanceOf(Entregavel.class, semDesconto);
    }

    @Test
    void valorZeroLancaValorInvalidoException() {
        ValorInvalidoException e = assertThrows(ValorInvalidoException.class,
                () -> new PedidoEletronico(4, "Dani", 0.0, StatusPedido.PENDENTE));
        assertEquals("O valor do pedido deve ser maior que zero.", e.getMessage());
    }

    @Test
    void valorNegativoLancaValorInvalidoException() {
        assertThrows(ValorInvalidoException.class,
                () -> new PedidoEletronico(5, "Edu", -10.0, StatusPedido.PENDENTE));
    }
}
