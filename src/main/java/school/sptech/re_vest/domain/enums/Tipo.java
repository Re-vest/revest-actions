package school.sptech.re_vest.domain.enums;

public enum Tipo {
    CALCADO ("Calçado"),
    CAMISETA ("Camiseta"),
    CALCA ("Calça"),
    BLUSA ("Blusa"),
    VESTIDO ("Vestido"),
    SHORTS ("Shorts"),
    BOLSA ("Bolsa"),
    CINTO ("Cinto"),
    RELOGIO ("Relógio"),
    OCULOS ("Óculos"),
    CHAPEU ("Chapéu"),
    OUTRO ("Outro");

    private final String tipo;

    Tipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
