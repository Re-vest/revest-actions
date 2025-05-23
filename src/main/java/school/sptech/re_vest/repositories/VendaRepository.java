package school.sptech.re_vest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Venda;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
    @Query(
            value = "SELECT SUM(v.valorTotal) " +
                    "FROM venda v " +
                    "WHERE DATE(v.dataVenda) = CURDATE() " +
                    "AND v.fkEvento = :eventoId",
            nativeQuery = true
    )
    Double buscarSomaValorTotalVendasNoDia(@Param("eventoId") Integer eventoId);

    @Query(
            value = "SELECT SUM(v.valorTotal) " +
                    "FROM venda v " +
                    "WHERE v.fkEvento = :eventoId ",
            nativeQuery = true
    )
    Double buscarSomaValorTotalVendasEvento(@Param("eventoId") Integer eventoId);

    @Query(
            value = "SELECT " +
                    "COUNT(v.id) " +
                    "FROM venda v " +
                    "JOIN evento e ON v.fkEvento = e.id " +
                    "JOIN usuario u ON v.fkUsuario = u.id " +
                    "WHERE DATE(v.dataVenda) = CURDATE() " +
                    "AND e.id = :eventoId ",
            nativeQuery = true
    )
    Integer buscarQuantidadeVendasNoDia(@Param("eventoId") Integer eventoId);

    @Query(
            value = "SELECT " +
                    "COUNT(v.id) " +
                    "FROM venda v " +
                    "JOIN evento e ON v.fkEvento = e.id " +
                    "JOIN usuario u ON v.fkUsuario = u.id " +
                    "WHERE e.id = :eventoId ",
            nativeQuery = true
    )
    Integer buscarQuantidadeVendasNoEvento(@Param("eventoId") Integer eventoId);

    @Query(value = """
    SELECT p.id, p.nome, p.categoria, p.tipo
    FROM produto p
    JOIN produtocarrinho pc ON p.id = pc.fkProduto
    JOIN venda v ON pc.fkVenda = v.id
    WHERE v.fkEvento = :eventoId
""", nativeQuery = true)
    List<Object[]> buscarProdutosVendidosNoEvento(@Param("eventoId") Integer eventoId);

}
