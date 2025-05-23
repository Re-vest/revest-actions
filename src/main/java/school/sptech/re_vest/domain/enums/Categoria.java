package school.sptech.re_vest.domain.enums;

public enum Categoria {
    ROUPA ("Roupa"),
    ACESSORIO ("Acessório");

    private final String categoria;

    Categoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}
