package org.redhat.currency;

import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
public class CurrencyReadinessCheck implements HealthCheck{
    
    @Inject
    CurrencyService currencyService;

    String name = "Currency Rediness Check";

    @Override
    public HealthCheckResponse call() {
        try{
            currencyService.list();
        } catch(Exception e){
            return HealthCheckResponse.down(name);
        }
        return HealthCheckResponse.up(name);
    }
    
}
