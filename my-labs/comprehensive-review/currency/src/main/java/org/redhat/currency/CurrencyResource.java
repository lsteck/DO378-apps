package org.redhat.currency;

import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.security.Authenticated;

@Path("/")
// @Authenticated
public class CurrencyResource {

    @Inject
    CurrencyService currencyService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAll(){
        return currencyService.list();
    }

    @DELETE
    @RolesAllowed("confidential")
    @Path("{currency}")
    @Transactional
    public void delete(@PathParam("currency") String currency){
        currencyService.delete(currency);
    }

    @GET
    @Path("{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Currency getCurrency(@PathParam("currency") String currency) {
        return currencyService.getCurrency(currency);
    }

}