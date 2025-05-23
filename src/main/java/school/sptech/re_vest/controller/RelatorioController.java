package school.sptech.re_vest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Venda;
import school.sptech.re_vest.services.EventoService;
import school.sptech.re_vest.services.VendaService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioController {

    private final VendaService vendaService;
    private final EventoService eventoService;

    @GetMapping
    public void gerarCsv() {
        List<Venda> vendas = vendaService.listar();
        List<Evento> eventos = eventoService.listar();

        int quantidadeVendida = 0;
        double valorTotalVendido = 0.0;
        Map<String, Integer> categoriasContagem = new HashMap<>();

        String userHome = System.getProperty("user.home");

        String timestamp = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        File outputFile = new File(userHome + "/Downloads/relatorio_" + timestamp + ".csv");

        try (
                OutputStream file = new FileOutputStream(outputFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file));
        ) {
            writer.write("Relatório de Vendas e Eventos\n\n");

            writer.write("=== Vendas ===\n");
            writer.write("ID Venda, ID Produto, Categoria, Nome, Preço, Descrição, Evento, Vendedor\n");

            for (Venda venda : vendas) {
                for (Produto produto : venda.getCarrinho()) {
                    writer.write(String.format("%d, %d, %s, %s, %.2f, %s, %s, %s\n",
                            venda.getId(),
                            produto.getId(),
                            produto.getCategoria(),
                            produto.getNome(),
                            produto.getPreco(),
                            produto.getDescricao(),
                            venda.getEvento().getTitulo(),
                            venda.getVendedor().getNome()
                    ));

                    quantidadeVendida++;
                    valorTotalVendido += produto.getPreco();

                    categoriasContagem.put(String.valueOf(produto.getTipo()),
                            categoriasContagem.getOrDefault(produto.getCategoria(), 0) + 1);
                }
            }

            double ticketMedio = vendas.isEmpty() ? 0 : valorTotalVendido / vendas.size();
            List<Map.Entry<String, Integer>> categoriasMaisVendidas = categoriasContagem.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(5)
                    .collect(Collectors.toList());

            writer.write("\n=== Resumo ===\n");
            writer.write(String.format("Quantidade de Produtos Vendidos: %d\n", quantidadeVendida));
            writer.write(String.format("Valor Total Arrecadado: %.2f\n", valorTotalVendido));
            writer.write(String.format("Ticket Médio por Venda: %.2f\n", ticketMedio));
            writer.write("Top 5 Categorias Mais Vendidas:\n");
            for (Map.Entry<String, Integer> entry : categoriasMaisVendidas) {
                writer.write(String.format("- %s: %d\n", entry.getKey(), entry.getValue()));
            }

            writer.write("\n=== Eventos ===\n");
            writer.write("ID, Nome, Descrição, Data de Início, Data de Fim\n");

            for (Evento evento : eventos) {
                writer.write(String.format("%d, %s, %s, %s, %s\n",
                        evento.getId(),
                        evento.getTitulo(),
                        evento.getDescricao(),
                        evento.getDataInicio(),
                        evento.getDataFim()
                ));
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar o relatório CSV", e);
        }
    }
}