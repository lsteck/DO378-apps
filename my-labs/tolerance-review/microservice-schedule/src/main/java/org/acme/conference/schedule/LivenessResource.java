package org.acme.conference.schedule;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class LivenessResource implements HealthCheck{
    
    @Override
    public HealthCheckResponse call(){
        return HealthCheckResponse.up("Service is alive");
    }
}
