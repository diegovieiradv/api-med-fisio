package api.med.fisio.domain.paciente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpfValidatorTest {

    private final CpfValidator validator = new CpfValidator();

    @Test
    @DisplayName("Deveria retornar true para CPF válido")
    void validarCpfValido() {
        assertThat(validator.isValid("529.982.247.25", null)).isTrue();
    }

    @Test
    @DisplayName("Deveria retornar true para CPF válido sem formatação")
    void validarCpfValidoSemFormatacao() {
        assertThat(validator.isValid("52998224725", null)).isTrue();
    }

    @Test
    @DisplayName("Deveria retornar false para CPF inválido")
    void validarCpfInvalido() {
        assertThat(validator.isValid("529.982.247.20", null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para CPF com todos dígitos iguais")
    void validarCpfComTodosDigitosIguais() {
        assertThat(validator.isValid("111.111.111-11", null)).isFalse();
        assertThat(validator.isValid("000.000.000-00", null)).isFalse();
        assertThat(validator.isValid("999.999.999-99", null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para CPF nulo")
    void validarCpfNulo() {
        assertThat(validator.isValid(null, null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para CPF vazio")
    void validarCpfVazio() {
        assertThat(validator.isValid("", null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para CPF com menos de 11 dígitos")
    void validarCpfComMenosDe11Digitos() {
        assertThat(validator.isValid("123.456.789-0", null)).isFalse();
        assertThat(validator.isValid("1234567890", null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para CPF com mais de 11 dígitos")
    void validarCpfComMaisDe11Digitos() {
        assertThat(validator.isValid("123.456.789-012", null)).isFalse();
        assertThat(validator.isValid("123456789012", null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para primeiro dígito verificador inválido")
    void validarCpfPrimeiroDigitoInvalido() {
        assertThat(validator.isValid("529.982.247.35", null)).isFalse();
    }

    @Test
    @DisplayName("Deveria retornar false para segundo dígito verificador inválido")
    void validarCpfSegundoDigitoInvalido() {
        assertThat(validator.isValid("529.982.247.20", null)).isFalse();
    }
}
