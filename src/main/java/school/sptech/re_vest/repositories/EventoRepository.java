package school.sptech.re_vest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.re_vest.domain.Evento;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    @Query(
            value = "SELECT * FROM evento WHERE CURDATE() BETWEEN dataInicio AND dataFim;",
            nativeQuery = true
    )
    List<Evento> eventosAtivosNoMomento();
}
