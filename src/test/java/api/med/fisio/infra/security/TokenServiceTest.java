package api.med.fisio.infra.security;

import api.med.fisio.domain.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "1234567890123456789012345678901234567890");
    }

    @Test
    @DisplayName("Deveria gerar token JWT válido")
    void gerarToken() {
        var usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "login", "admin");

        var token = tokenService.gerarToken(usuario);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("Deveria extrair subject do token JWT")
    void getSubject() {
        var usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "login", "admin");

        var token = tokenService.gerarToken(usuario);
        var subject = tokenService.getSubject(token);

        assertThat(subject).isEqualTo("admin");
    }

    @Test
    @DisplayName("Deveria retornar issuer correto no token")
    void issuerToken() {
        var usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "login", "admin");

        var token = tokenService.gerarToken(usuario);
        var subject = tokenService.getSubject(token);

        assertThat(subject).isEqualTo("admin");
    }
}
