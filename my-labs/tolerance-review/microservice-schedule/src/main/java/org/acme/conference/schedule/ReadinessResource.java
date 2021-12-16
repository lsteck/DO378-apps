package org.acme.conference.schedule;

import javax.enterprise.context.ApplicationScoped;


import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class ReadinessResource implements HealthCheck{
    
    @Override
    public HealthCheckResponse call(){
        return HealthCheckResponse.up("Service is ready");
    }
}
