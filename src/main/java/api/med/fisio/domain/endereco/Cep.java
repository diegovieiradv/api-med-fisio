package api.med.fisio.domain.endereco;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cep {
    String message() default "CEP inválido ou não encontrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
