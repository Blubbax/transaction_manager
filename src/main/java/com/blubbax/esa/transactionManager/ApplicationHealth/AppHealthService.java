package com.blubbax.esa.transactionManager.ApplicationHealth;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class AppHealthService {

    private final ApplicationEventPublisher eventPublisher;
    ApplicationAvailability availability;

    public AppHealthService(ApplicationEventPublisher eventPublisher, ApplicationAvailability availability) {
        this.eventPublisher = eventPublisher;
        this.availability = availability;
    }

    public void setReadinessState(boolean readiness) {
        if (readiness) {
            AvailabilityChangeEvent.publish(this.eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
        } else {
            AvailabilityChangeEvent.publish(this.eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
        }
    }

    public void setLivenessState(boolean liveness) {
        if (liveness) {
            AvailabilityChangeEvent.publish(this.eventPublisher, this, LivenessState.CORRECT);
        } else {
            AvailabilityChangeEvent.publish(this.eventPublisher, this, LivenessState.BROKEN);
        }

    }

    public LivenessState getLivenessState() {
        return availability.getLivenessState();
    }

    public ReadinessState getReadinessState() {
        return availability.getReadinessState();
    }
}
