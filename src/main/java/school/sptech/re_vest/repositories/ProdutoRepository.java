package school.sptech.re_vest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.dto.Produto.VendidosPorDiaDTO;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query(
            value = "SELECT SUM(p.preco) AS contagem_produtos_vendidos FROM produto p JOIN produtocarrinho vc ON p.id = vc.fkProduto JOIN venda v ON vc.fkVenda = v.id JOIN evento e ON e.id = :eventoId WHERE p.statusProduto = 'VENDIDO' AND v.dataVenda BETWEEN e.dataInicio AND e.dataFim",
            nativeQuery = true
    )
    Integer buscarQuantidadeVendidaNoEvento(@Param("eventoId") Integer eventoId);

    @Query(
            value = "SELECT v.dataVenda AS dataVenda, COUNT(DISTINCT p.id) AS contagemProdutosVendidos " +
                    "FROM produto p " +
                    "JOIN produtocarrinho pc ON p.id = pc.fkProduto " +
                    "JOIN venda v ON pc.fkVenda = v.id " +
                    "JOIN evento e ON v.fkEvento = e.id " +
                    "WHERE p.statusProduto = 'VENDIDO' " +
                    "AND v.dataVenda BETWEEN e.dataInicio AND e.dataFim " +
                    "AND e.id = :eventoId " +
                    "GROUP BY v.dataVenda " +
                    "ORDER BY v.dataVenda",
            nativeQuery = true
    )
    List<Object[]> buscarVendasPorDia(@Param("eventoId") Integer eventoId);

}