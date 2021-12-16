package org.redhat.currency;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class CurrencyService {
    
    public List<String> list(){
        return Currency.<Currency>streamAll()
            .map((c) -> c.name).collect(Collectors.toList());
    }

    public Currency getCurrency(String currency) {
        return Currency.<Currency>findByIdOptional(currency)
            .orElseThrow(() ->  new NotFoundException());
    }

	public void delete(String currency) {
        if(!Currency.deleteById(currency)) throw new NotFoundException();
	}

}
