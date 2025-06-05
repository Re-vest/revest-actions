package school.sptech.re_vest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.re_vest.domain.Historico;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    @Query(
            value = "SELECT * FROM historico",
            nativeQuery = true
    )
    List<Historico> listarTodos();

    @Modifying
    @Query("DELETE FROM Historico h WHERE h.dataHora < :dataLimite")
    void deletarPorDataAntesDe(@Param("dataLimite") LocalDate dataLimite);
}
