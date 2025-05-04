package generated.org.springframework.boot.databases.validation;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;

public class ConstraintValidatorContextImpl implements ConstraintValidatorContext {

    @Override
    public void disableDefaultConstraintViolation() {

    }

    @Override
    public String getDefaultConstraintMessageTemplate() {
        return "";
    }

    @Override
    public ClockProvider getClockProvider() { return new ClockProviderImpl(); }

    // TODO: need to approx?
    @Override
    public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String s) {
        return null;
    }

    // TODO: need to approx?
    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
