package generated.org.springframework.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;

@Approximate(NotBlankValidator.class)
public class NotBlankValidatorImpl {
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        Engine.assume(charSequence != null);
        Engine.assume(!charSequence.isEmpty());
        return true;
    }
}
