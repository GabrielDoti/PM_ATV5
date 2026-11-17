import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class GerenciadorPedidosTest {
    private GerenciadorPedidos gerenciador;
    private Pedido eletronico;
    private Pedido roupa;
    private Pedido alimento;

    @BeforeEach
    void preparar() {
        gerenciador = new GerenciadorPedidos();
        eletronico = new PedidoEletronico(1, "Ana", 150.0, StatusPedido.PENDENTE);
        roupa = new PedidoRoupa(2, "Bruno", 250.0, StatusPedido.PENDENTE);
        alimento = new PedidoAlimento(3, "Carla", 40.0, StatusPedido.PENDENTE);
        gerenciador.adicionar(eletronico);
        gerenciador.adicionar(roupa);
        gerenciador.adicionar(alimento);
    }

    @AfterEach
    void finalizar(TestInfo info) {
        System.out.println("Teste finalizado: " + info.getDisplayName());
    }

    @Test
    void buscaPorIdEncontraOPedidoCerto() {
        assertSame(roupa, gerenciador.buscarPorId(2));
    }

    @Test
    void buscaPorIdInexistenteDevolveNull() {
        assertNull(gerenciador.buscarPorId(99));
    }

    @Test
    void processarElegiveisMudaTodosParaProcessando() {
        assertEquals(3, gerenciador.processarElegiveis());
        for (Pedido p : gerenciador.getPedidos()) {
            assertEquals(StatusPedido.PROCESSANDO, p.getStatus());
        }
    }

    @Test
    void processarElegiveisIgnoraCanceladosEEntreguesSemInterromperOsDemais() throws PedidoInvalidoException {
        eletronico.setStatus(StatusPedido.CANCELADO);
        roupa.setStatus(StatusPedido.ENTREGUE);

        assertEquals(1, gerenciador.processarElegiveis());

        assertEquals(StatusPedido.CANCELADO, eletronico.getStatus());
        assertEquals(StatusPedido.ENTREGUE, roupa.getStatus());
        assertEquals(StatusPedido.PROCESSANDO, alimento.getStatus());
    }

    @Test
    void removerCanceladosTiraSoOsCancelados() throws PedidoInvalidoException {
        roupa.setStatus(StatusPedido.CANCELADO);
        assertEquals(1, gerenciador.removerCancelados());
        assertEquals(List.of(eletronico, alimento), gerenciador.getPedidos());
        assertNull(gerenciador.buscarPorId(2));
    }

    @Test
    void removerCanceladosSemNenhumNaoMudaNada() {
        assertEquals(0, gerenciador.removerCancelados());
        assertEquals(3, gerenciador.getPedidos().size());
    }

    @Test
    void listarAtivosNaoMostraCancelados() throws PedidoInvalidoException {
        alimento.setStatus(StatusPedido.CANCELADO);
        assertEquals(List.of(eletronico, roupa), gerenciador.listarAtivos());
    }

    @Test
    void listarEntregaveisTrazSoEletronicoERoupa() {
        List<Entregavel> entregaveis = gerenciador.listarEntregaveis();
        assertEquals(2, entregaveis.size());
        assertTrue(entregaveis.contains((Entregavel) eletronico));
        assertTrue(entregaveis.contains((Entregavel) roupa));
    }

    @Test
    void getPedidosDevolveCopia() {
        gerenciador.getPedidos().clear();
        assertEquals(3, gerenciador.getPedidos().size());
    }
}
