package api.med.fisio.domain.paciente;

import api.med.fisio.domain.endereco.DadosEndereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    @DisplayName("Deveria cadastrar paciente com sucesso")
    void cadastrarPaciente() {
        var dadosCadastro = new DadosCadastroPaciente(
                "Maria",
                "maria@email.com",
                "61999999999",
                "529.982.247.25",
                dadosEndereco()
        );

        when(repository.save(any())).thenAnswer(invocation -> {
            var paciente = invocation.getArgument(0, Paciente.class);
            return paciente;
        });

        var resultado = pacienteService.cadastrar(dadosCadastro);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Maria");
        assertThat(resultado.email()).isEqualTo("maria@email.com");
    }

    @Test
    @DisplayName("Deveria retornar paciente ao detalhar por ID")
    void detalharPaciente() {
        var paciente = new Paciente(
                1L,
                "Maria",
                "maria@email.com",
                "61999999999",
                "529.982.247.25",
                dadosEndereco().toEndereco(),
                true
        );

        when(repository.getReferenceById(1L)).thenReturn(paciente);

        var resultado = pacienteService.detalhar(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Maria");
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
