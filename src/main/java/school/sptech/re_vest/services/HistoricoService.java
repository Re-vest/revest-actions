package school.sptech.re_vest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import school.sptech.re_vest.domain.Historico;
import school.sptech.re_vest.repositories.HistoricoRepository;
import school.sptech.re_vest.utils.FilaObj;

import java.time.LocalDate;
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

    public void registrarHistorico(Integer idUsuario, String nomeUsuario, String acao){
        Historico historico = new Historico();
        historico.setIdUsuario(idUsuario);
        historico.setNomeUsuario(nomeUsuario);
        historico.setAcao(acao);
        historico.setDataHora(LocalDateTime.now());

        filaHistorico.insert(historico);
        historicoRepository.save(historico);
    }

    @Scheduled(cron = "0 0 0 1 1,3,5,7,9,11 ?")
    public void limparHistoricoAntigo() {
        LocalDate limite = LocalDate.now().minusMonths(2);
        historicoRepository.deletarPorDataAntesDe(limite);
        System.out.println("Histórico anterior a " + limite + " foi limpo.");
    }


    public List<Historico> listarTodos() {
        return historicoRepository.listarTodos();
    }

    public FilaObj<Historico> getFilaHistorico() {
        return filaHistorico;
    }
}