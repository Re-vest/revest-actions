package school.sptech.re_vest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.re_vest.domain.Historico;
import school.sptech.re_vest.repositories.HistoricoRepository;
import school.sptech.re_vest.utils.FilaObj;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricoService {
    private final FilaObj<Historico> filaHistorico;

    @Autowired
    private HistoricoRepository historicoRepository;

    public HistoricoService() {
        this.filaHistorico = new FilaObj<>(100);
    }

    public void registrarHistorico(Integer idUsuario, String acao){
        Historico historico = new Historico();
        historico.setIdUsuario(idUsuario);
        historico.setAcao(acao);
        historico.setDataHora(LocalDateTime.now());

        filaHistorico.insert(historico);
        historicoRepository.save(historico);
    }

    public List<Historico> listarTodos() {
        return historicoRepository.listarTodos();
    }

    public FilaObj<Historico> getFilaHistorico() {
        return filaHistorico;
    }
}