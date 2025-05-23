package school.sptech.re_vest.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.re_vest.domain.Historico;
import school.sptech.re_vest.services.HistoricoService;
import school.sptech.re_vest.utils.FilaObj;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/historico")
public class HistoricoController {
    private final HistoricoService historicoService;

    @GetMapping
    @Operation(
            summary = "Listar histórico de ações",
            description = "Endpoint GET que retorna o histórico de ações realizadas no sistema"
    )
    public ResponseEntity<List<Historico>> listarHistorico() {
        List<Historico> historicos = historicoService.listarTodos();
        System.out.println("Históricos retornados: " + historicos.size());

        if (historicos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(historicos);
    }
}
