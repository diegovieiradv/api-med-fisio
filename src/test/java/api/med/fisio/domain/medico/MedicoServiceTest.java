package api.med.fisio.domain.medico;

import api.med.fisio.domain.endereco.DadosEndereco;
import api.med.fisio.domain.endereco.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository repository;

    @InjectMocks
    private MedicoService medicoService;

    @Test
    @DisplayName("Deveria cadastrar médico com sucesso")
    void cadastrarMedico() {
        var dadosCadastro = new DadosCadastroMedico(
                "Dr. João",
                "joao@email.com",
                "61999999999",
                "123456",
                Especialidade.CARDIOLOGIA,
                dadosEndereco()
        );

        when(repository.save(any())).thenAnswer(invocation -> {
            var medico = invocation.getArgument(0, Medico.class);
            return medico;
        });

        var resultado = medicoService.cadastrar(dadosCadastro);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Dr. João");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Deveria retornar médico ao detalhar por ID")
    void detalharMedico() {
        var medico = new Medico(
                1L,
                "Dr. João",
                "joao@email.com",
                "61999999999",
                "123456",
                Especialidade.CARDIOLOGIA,
                new Endereco(dadosEndereco()),
                true
        );

        when(repository.getReferenceById(1L)).thenReturn(medico);

        var resultado = medicoService.detalhar(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Dr. João");
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "Rua Teste",
                "Bairro Teste",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}
