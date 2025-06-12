package school.sptech.re_vest.controller;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Venda;
import school.sptech.re_vest.services.EventoService;
import school.sptech.re_vest.services.VendaService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioController {

    private final VendaService vendaService;
    private final EventoService eventoService;

    private static String formatarCategoria(String categoria) {
        return categoria.substring(0, 1).toUpperCase() + categoria.substring(1).toLowerCase();
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> gerarPdf() {
        try {
            // Setup
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
            LocalDate hoje = LocalDate.now();
            int mesAtual = hoje.getMonthValue();
            int anoAtual = hoje.getYear();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            DateTimeFormatter mesFormatter = DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR"));
            String mesFormatado = hoje.format(mesFormatter);
            mesFormatado = mesFormatado.substring(0, 1).toUpperCase() + mesFormatado.substring(1);
            List<Venda> vendasDoMes = vendaService.listar().stream()
                    .filter(v -> v.getDataVenda() != null &&
                            v.getDataVenda().getMonthValue() == mesAtual &&
                            v.getDataVenda().getYear() == anoAtual)
                    .collect(Collectors.toList());
            // Cálculos
            int quantidadeVendasMes = vendasDoMes.size();
            double valorTotalVendasMes = vendasDoMes.stream()
                    .mapToDouble(v -> v.getCarrinho().stream().mapToDouble(Produto::getPreco).sum())
                    .sum();
            Map<Evento, Long> vendasPorEvento = vendasDoMes.stream()
                    .collect(Collectors.groupingBy(Venda::getEvento, Collectors.counting()));
            Map.Entry<Evento, Long> eventoMaisVendidoEntry = vendasPorEvento.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .orElse(null);
            Evento eventoMaisVendido = eventoMaisVendidoEntry != null ? eventoMaisVendidoEntry.getKey() : null;
            double valorTotalEvento = eventoMaisVendido != null ? vendasDoMes.stream()
                    .filter(v -> v.getEvento().equals(eventoMaisVendido))
                    .mapToDouble(v -> v.getCarrinho().stream().mapToDouble(Produto::getPreco).sum())
                    .sum() : 0.0;
            Map<LocalDate, Long> vendasPorDia = vendasDoMes.stream()
                    .collect(Collectors.groupingBy(v -> v.getDataVenda().atStartOfDay().toLocalDate(), Collectors.counting()));
            Map.Entry<LocalDate, Long> diaComMaisVendasEntry = vendasPorDia.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .orElse(null);
            LocalDate diaMaisVendas = diaComMaisVendasEntry != null ? diaComMaisVendasEntry.getKey() : null;
            Long quantidadeVendasDia = diaComMaisVendasEntry != null ? diaComMaisVendasEntry.getValue() : 0;
            // Cabeçalho
            document.add(new Paragraph("Bazar Batista Betel").setFont(boldFont).setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Avenida Souza Ramos, 513 - Guaianases").setFontSize(11).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Contato: (11)97836-4836 | deaninialexandra@gmail.com").setFontSize(11).setTextAlignment(TextAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine(1)));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Relatório de Vendas e Eventos - " + mesFormatado)
                    .setFont(boldFont).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Relatório gerado em: " + LocalDateTime.now().format(dataHoraFormatter))
                    .setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("\n"));
            // Eventos
            document.add(new Paragraph(":performing_arts: Eventos do Mês").setFont(boldFont).setFontSize(14));
            Table tabelaEventos = new Table(UnitValue.createPercentArray(new float[]{3, 5, 2})).useAllAvailableWidth();
            String[] headersEventos = {"Nome", "Descrição", "Data"};
            for (String h : headersEventos) {
                tabelaEventos.addHeaderCell(new Cell().add(new Paragraph(h).setFont(boldFont)));
            }
            List<Evento> eventosDoMes = eventoService.listar().stream()
                    .filter(e -> e.getDataInicio() != null &&
                            e.getDataInicio().getMonthValue() == mesAtual &&
                            e.getDataInicio().getYear() == anoAtual)
                    .collect(Collectors.toList());
            for (Evento evento : eventosDoMes) {
                tabelaEventos.addCell(evento.getTitulo());
                tabelaEventos.addCell(evento.getDescricao());
                tabelaEventos.addCell(evento.getDataInicio().format(dateFormatter));
            }
            document.add(tabelaEventos);
            document.add(new Paragraph("\n"));
            // Vendas
            document.add(new Paragraph(":bar_chart: Vendas do Mês").setFont(boldFont).setFontSize(14));
            Table tabelaVendas = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2, 2})).useAllAvailableWidth();
            String[] headers = {"Produto", "Categoria", "Preço", "Vendedor", "Data Venda", "Evento"};
            for (String h : headers) {
                tabelaVendas.addHeaderCell(new Cell().add(new Paragraph(h).setFont(boldFont)));
            }
            for (Venda venda : vendasDoMes) {
                for (Produto produto : venda.getCarrinho()) {
                    tabelaVendas.addCell(produto.getNome());
                    tabelaVendas.addCell(formatarCategoria(produto.getCategoria().name()));
                    tabelaVendas.addCell(String.format("R$ %.2f", produto.getPreco()));
                    tabelaVendas.addCell(venda.getVendedor().getNome());
                    tabelaVendas.addCell(venda.getDataVenda().format(dateFormatter));
                    tabelaVendas.addCell(venda.getEvento().getTitulo());
                }
            }
            document.add(tabelaVendas);
            // Resumo
            document.add(new Paragraph("\n:pushpin: Resumo do Mês").setFont(boldFont).setFontSize(14));
            document.add(new Paragraph("Total de vendas no mês: " + quantidadeVendasMes));
            document.add(new Paragraph("Valor total arrecadado: R$ " + String.format("%.2f", valorTotalVendasMes)));
            document.add(new Paragraph("Evento com mais vendas: " + (eventoMaisVendido != null
                    ? eventoMaisVendido.getTitulo() + " (R$ " + String.format("%.2f", valorTotalEvento) + ")"
                    : "Nenhum evento registrado")));
            document.add(new Paragraph("Dia com mais vendas: " + (diaMaisVendas != null
                    ? diaMaisVendas.format(dateFormatter) + " (Total: " + quantidadeVendasDia + " vendas)"
                    : "Nenhum dia registrado")));
            document.close();
            byte[] pdfBytes = outputStream.toByteArray();
            HttpHeaders headersResp = new HttpHeaders();
            headersResp.setContentType(MediaType.APPLICATION_PDF);
            headersResp.setContentDisposition(ContentDisposition.builder("attachment").filename("Relatorio_" + mesFormatado + ".pdf").build());
            return new ResponseEntity<>(pdfBytes, headersResp, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar o PDF: " + e.getMessage()).getBytes());
        }
    }
    @GetMapping("/excel")
    public ResponseEntity<byte[]> gerarExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            List<Venda> todasVendas = vendaService.listar();
            List<Evento> todosEventos = eventoService.listar();
            Sheet sheetEventos = workbook.createSheet("Eventos");
            Row headerEvento = sheetEventos.createRow(0);
            String[] headersEventos = {"Nome", "Descrição", "Data Início", "Data Fim"};
            for (int i = 0; i < headersEventos.length; i++) {
                headerEvento.createCell(i).setCellValue(headersEventos[i]);
            }
            int rowEvento = 1;
            for (Evento evento : todosEventos) {
                Row row = sheetEventos.createRow(rowEvento++);
                row.createCell(0).setCellValue(evento.getTitulo());
                row.createCell(1).setCellValue(evento.getDescricao());
                row.createCell(2).setCellValue(evento.getDataInicio().format(dateFormatter));
                row.createCell(3).setCellValue(evento.getDataFim().format(dateFormatter));
            }
            Sheet sheetVendas = workbook.createSheet("Vendas");
            Row headerVenda = sheetVendas.createRow(0);
            String[] headersVendas = {"Produto", "Categoria", "Preço", "Vendedor", "Data Venda", "Evento"};
            for (int i = 0; i < headersVendas.length; i++) {
                headerVenda.createCell(i).setCellValue(headersVendas[i]);
            }
            int rowVenda = 1;
            for (Venda venda : todasVendas) {
                for (Produto produto : venda.getCarrinho()) {
                    Row row = sheetVendas.createRow(rowVenda++);
                    row.createCell(0).setCellValue(produto.getNome());
                    row.createCell(1).setCellValue(produto.getCategoria().name());
                    row.createCell(2).setCellValue(produto.getPreco());
                    row.createCell(3).setCellValue(venda.getVendedor().getNome());
                    row.createCell(4).setCellValue(venda.getDataVenda().format(dateFormatter));
                    row.createCell(5).setCellValue(venda.getEvento().getTitulo());
                }
            }
            Sheet sheetResumo = workbook.createSheet("Resumo");
            int rowResumo = 0;
            double totalArrecadado = todasVendas.stream()
                    .mapToDouble(v -> v.getCarrinho().stream().mapToDouble(Produto::getPreco).sum())
                    .sum();
            sheetResumo.createRow(rowResumo++).createCell(0).setCellValue("Total de vendas: " + todasVendas.size());
            sheetResumo.createRow(rowResumo).createCell(0).setCellValue("Valor total arrecadado: R$ " + String.format("%.2f", totalArrecadado));
            workbook.write(out);
            byte[] excelBytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Relatorio_Completo.xlsx").build());
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar o Excel: " + e.getMessage()).getBytes());
        }
    }
}