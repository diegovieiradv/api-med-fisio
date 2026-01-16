package api.med.fisio.domain.medico;

import jakarta.validation.constraints.NotNull;
import api.med.fisio.domain.endereco.DadosEndereco;

public record DadosAtualizacaoMedico(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
