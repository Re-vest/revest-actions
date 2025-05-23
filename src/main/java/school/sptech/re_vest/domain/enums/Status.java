package school.sptech.re_vest.domain.enums;

public enum Status {
    OCULTO ("Oculto"),
    DISPONIVEL ("Disponível"),
    VENDIDO ("Vendido");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
