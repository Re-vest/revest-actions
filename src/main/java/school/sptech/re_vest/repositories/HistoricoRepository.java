package school.sptech.re_vest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.re_vest.domain.Historico;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    @Query(
            value = "SELECT * FROM historico",
            nativeQuery = true
    )
    List<Historico> listarTodos();

}
