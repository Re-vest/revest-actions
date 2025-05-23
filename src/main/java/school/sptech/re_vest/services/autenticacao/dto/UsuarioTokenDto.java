package school.sptech.re_vest.services.autenticacao.dto;

import school.sptech.re_vest.domain.enums.PerfilUsuario;

public class UsuarioTokenDto {

  private Integer userId;
  private String nome;
  private String email;
  private PerfilUsuario perfilUsuario;
  private String token;


    public PerfilUsuario getPerfilUsuario() {
    return perfilUsuario;
  }

  public void setPerfilUsuario(PerfilUsuario perfilUsuario) {
    this.perfilUsuario = perfilUsuario;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
