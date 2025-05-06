package generated.org.springframework.boot.databases.samples;

import generated.org.springframework.boot.databases.basetables.BaseTableManager;
import generated.org.springframework.boot.databases.basetables.NoIdTableManager;
import jakarta.validation.ConstraintValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.number.sign.PositiveValidatorForInteger;

public class SpringDatabases {

    // template to generate
    // plz do not delete
    public static BaseTableManager<Integer> _blank = new BaseTableManager<>(
            1,
            new Class<?>[]{String.class, Integer.class},
            new ConstraintValidator<?, ?>[][]{
                    new ConstraintValidator<?, ?>[]{new NotBlankValidator()},
                    new ConstraintValidator<?, ?>[]{
                            new PositiveValidatorForInteger(),
                            new DigitsValidatorForNumber()
                    }
            }
    );

    public static NoIdTableManager _blankAdd = new NoIdTableManager(Integer.class, Integer.class);
}
