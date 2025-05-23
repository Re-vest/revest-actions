package school.sptech.re_vest.dto.Produto;

import java.time.LocalDate;

public class VendidosPorDiaDTO {
    private LocalDate dataVenda;
    private Integer contagemProdutosVendidos;

    public VendidosPorDiaDTO(LocalDate dataVenda, Integer contagemProdutosVendidos) {
        this.dataVenda = dataVenda;
        this.contagemProdutosVendidos = contagemProdutosVendidos;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Integer getContagemProdutosVendidos() {
        return contagemProdutosVendidos;
    }

    public void setContagemProdutosVendidos(Integer contagemProdutosVendidos) {
        this.contagemProdutosVendidos = contagemProdutosVendidos;
    }
}
