package school.sptech.re_vest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.re_vest.domain.Imagem;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
}
