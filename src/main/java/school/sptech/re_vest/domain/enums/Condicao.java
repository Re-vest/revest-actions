package school.sptech.re_vest.domain.enums;

public enum Condicao {
    NOVO ("Novo"),
    SEMI_NOVO ("Semi novo"),
    USADO ("Usado");

    private final String condicao;

    Condicao(String condicao) {
        this.condicao = condicao;
    }

    public String getCondicao() {
        return condicao;
    }
}
