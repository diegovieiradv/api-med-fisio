package api.med.fisio.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
        @NotBlank
        String login,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha
) {
}
