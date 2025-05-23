package school.sptech.re_vest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.EventoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;

    public List<Evento> listar() {
        return eventoRepository.findAll();
    }

    public Evento buscarPorId(Integer id) {return eventoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Evento"));}

    public Evento criar (Evento novoEvento) {
        novoEvento.setId(null);
        return eventoRepository.save(novoEvento);
    }

    public Evento editar(Integer id, Evento eventoAtualizado) {
        return eventoRepository.findById(id)
                .map(evento -> {
                    evento.setTitulo(eventoAtualizado.getTitulo());
                    evento.setDescricao(eventoAtualizado.getDescricao());
                    evento.setCor(eventoAtualizado.getCor());
                    evento.setDataInicio(eventoAtualizado.getDataInicio());
                    evento.setDataFim(eventoAtualizado.getDataFim());
                    return eventoRepository.save(evento);
                })
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Evento com ID " + id));
    }


    public void deletar(Integer id) {
        if (!eventoRepository.existsById(id)) throw new EntidadeNaoEncontradaException("Evento");
        eventoRepository.deleteById(id);
    }

    public List<Evento> eventosAtivos(){
        return eventoRepository.eventosAtivosNoMomento();
    }
}
