package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  /**
   * Método responsável por gerar o token JWT, que será utilizado para armazenar a sessão do usuário.
   *
   * @param usuario
   *  Usuário que está solicitando a criação do token JWT.
   *
   * @return
   *  Um {@code token} que será utilizado para identificar a sessão do usuário.
   */
  public String gerarToken(Usuario usuario) {
    try {
      Algorithm algoritmo = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer("API Voll.med")  // Identifica a API que gerou o Token.
          .withSubject(usuario.getLogin())  // Identifica qual usuário disparou a requisição.
          .withExpiresAt(dataExpiracao())  // Data na qual o token irá expirar. (2 horas para expirar)
          .sign(algoritmo);
    } catch (JWTCreationException ex) {
      throw new RuntimeException("Erro ao gerar token JWT", ex);
    }
  }

  /**
   * Método responsável por validar o token JWT informado no cabeçalho da requisição.
   *
   * @param tokenJWT
   *  {@code Token} informado no cabeçalho da requisição.
   *
   * @return
   *  O {@code Subject} contido no token JWT.
   */
  public String getSubject(String tokenJWT) {
    try {
      Algorithm algoritmo = Algorithm.HMAC256(secret);
      return JWT.require(algoritmo)
          .withIssuer("API Voll.med")
          .build()
          .verify(tokenJWT)
          .getSubject();
    } catch (JWTVerificationException ex){
      throw new RuntimeException("Token JWT inválido ou expirado.");
    }
  }

  /**
   * Método que define a data de expiração do token.
   *
   * @return
   *  O {@link Instant} em que será expirado o token.
   */
  private Instant dataExpiracao() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
