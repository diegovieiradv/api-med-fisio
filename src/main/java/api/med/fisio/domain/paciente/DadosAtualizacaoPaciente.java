package api.med.fisio.domain.paciente;

import jakarta.validation.constraints.NotNull;
import api.med.fisio.domain.endereco.DadosEndereco;

public record DadosAtualizacaoPaciente(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
