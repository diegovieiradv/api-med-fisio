package api.med.fisio.domain.endereco;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CepValidator implements ConstraintValidator<Cep, String> {

    @Autowired
    private ViaCepService viaCepService;

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.isBlank()) {
            return false;
        }

        String cepLimpo = cep.replaceAll("[^0-9]", "");

        if (cepLimpo.length() != 8) {
            return false;
        }

        var endereco = viaCepService.buscarEnderecoPorCep(cepLimpo);
        return endereco != null && endereco.erro() == null;
    }
}
