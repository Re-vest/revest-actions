package school.sptech.re_vest.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Venda;
import school.sptech.re_vest.domain.enums.Status;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.VendaRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private VendaService vendaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_deveRetornarTodasAsVendas() {
        List<Venda> vendas = List.of(new Venda(), new Venda());
        when(vendaRepository.findAll()).thenReturn(vendas);

        List<Venda> resultado = vendaService.listar();

        assertEquals(2, resultado.size());
        verify(vendaRepository).findAll();
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarVenda() {
        Venda venda = new Venda();
        venda.setId(1);
        when(vendaRepository.findById(1)).thenReturn(Optional.of(venda));

        Venda resultado = vendaService.buscarPorId(1);

        assertEquals(1, resultado.getId());
        verify(vendaRepository).findById(1);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(vendaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> vendaService.buscarPorId(1));
    }

    @Test
    void salvar_deveAtualizarStatusProdutosESalvarVenda() {
        Produto produto = new Produto();
        produto.setId(1);
        Venda venda = new Venda();
        venda.setCarrinho(List.of(produto));

        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);

        Venda resultado = vendaService.salvar(venda);

        verify(produtoService).atualizarStatus(1, Status.VENDIDO);
        verify(vendaRepository).save(venda);
        assertNotNull(resultado);
    }

    @Test
    void quantidadeVendasNoDia_deveRetornarQuantidade() {
        when(vendaRepository.buscarQuantidadeVendasNoDia(1)).thenReturn(5);

        Integer resultado = vendaService.quantidadeVendasNoDia(1);

        assertEquals(5, resultado);
    }

    @Test
    void totalVendasNoDia_quandoNaoNulo_deveRetornarValor() {
        when(vendaRepository.buscarSomaValorTotalVendasNoDia(1)).thenReturn(100.0);

        Double resultado = vendaService.totalVendasNoDia(1);

        assertEquals(100.0, resultado);
    }

    @Test
    void totalVendasNoDia_quandoNulo_deveRetornarZero() {
        when(vendaRepository.buscarSomaValorTotalVendasNoDia(1)).thenReturn(null);

        Double resultado = vendaService.totalVendasNoDia(1);

        assertEquals(0.0, resultado);
    }

    @Test
    void cincoCategoriaMaisVendida_deveRetornarTopCincoCategorias() {
        Object[] row = new Object[]{null, null, null, "ELETRONICO"};
        when(vendaRepository.buscarProdutosVendidosNoEvento(1)).thenReturn(List.of(row, row, row, row, row, row));

        List<Map<String, Object>> resultado = vendaService.cincoCategoriaMaisVendida(1);

        assertEquals(1, resultado.size());
        assertEquals("ELETRONICO", resultado.get(0).get("categoria"));
    }

    @Test
    void ticketMedioEvento_deveRetornarValorMedio() {
        when(vendaRepository.buscarSomaValorTotalVendasEvento(1)).thenReturn(200.0);
        when(vendaRepository.buscarQuantidadeVendasNoEvento(1)).thenReturn(4);

        Double resultado = vendaService.ticketMedioEvento(1);

        assertEquals(50.0, resultado);
    }

    @Test
    void ticketMedioDia_deveRetornarValorMedio() {
        when(vendaRepository.buscarSomaValorTotalVendasNoDia(1)).thenReturn(150.0);
        when(vendaRepository.buscarQuantidadeVendasNoDia(1)).thenReturn(3);

        Double resultado = vendaService.ticketMedioDia(1);

        assertEquals(50.0, resultado);
    }
}
