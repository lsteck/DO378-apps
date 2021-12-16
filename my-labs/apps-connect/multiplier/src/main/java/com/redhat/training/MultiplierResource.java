package com.redhat.training;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.redhat.training.service.MultiplierService;
import com.redhat.training.service.SolverService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/multiplier")
public class MultiplierResource implements MultiplierService{

    @Inject
    @RestClient
    SolverService solverService;

    final Logger log = LoggerFactory.getLogger(MultiplierResource.class);

    @GET
    @Path("/{lhs}/{rhs}")
    @Produces(MediaType.TEXT_PLAIN)
    public Float multiply(@PathParam("lhs") String lhs, @PathParam("rhs") String rhs){
        log.info("Multiplying {} to {}" ,lhs, rhs);
        return solverService.solve(lhs)*solverService.solve(rhs);
    }
}