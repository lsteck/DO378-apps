package org.redhat.currency;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class CurrencyLivenesCheck implements HealthCheck {

    @Inject
    CurrencyService currencyService;

    String name = "Currency Liveness Check";

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
