package generated.org.springframework.boot.databases.validation;

import jakarta.validation.ClockProvider;

import java.time.Clock;

public class ClockProviderImpl implements ClockProvider {
    @Override
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
