package generated.org.springframework.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForCharSequence;
import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;


@Approximate(DigitsValidatorForCharSequence.class)
public class DigitsValidatorForCharSequenceImpl {
    public static final int MAX_LENGTH = 3;

    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        int length = charSequence.length();
        for (int i = 0; i < MAX_LENGTH; i++) {
            if (i >= length)
                return true;

            char c = charSequence.charAt(i);
            Engine.assume(Character.isDigit(c));
        }
        return true;
    }
}
