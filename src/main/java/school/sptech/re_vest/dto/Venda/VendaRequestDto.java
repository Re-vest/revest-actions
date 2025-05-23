package school.sptech.re_vest.dto.Venda;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class VendaRequestDto {
    private Integer idEvento;
    private List<Integer> produtosId;
    private Integer idVendedor;

    @Nullable
    private LocalDate date;

    public VendaRequestDto(Integer idEvento, List<Integer> produtosId, Integer idVendedor, LocalDate date) {
        this.idEvento = idEvento;
        this.produtosId = produtosId;
        this.idVendedor = idVendedor;
        this.date = date != null ? date : LocalDate.now();
    }

    public VendaRequestDto() {}
}
