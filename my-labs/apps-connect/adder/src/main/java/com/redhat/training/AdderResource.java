package com.redhat.training;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.redhat.training.service.AdderService;
import com.redhat.training.service.SolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/adder")
public class AdderResource implements AdderService{

    @Inject
    @RestClient
    SolverService solverService;

    final Logger log = LoggerFactory.getLogger(AdderResource.class);

    @GET
    @Path("/{lhs}/{rhs}")
    @Produces(MediaType.TEXT_PLAIN)
    public Float add(@PathParam("lhs") String lhs, @PathParam("rhs") String rhs){
        log.info("Adding {} to {}" ,lhs, rhs);
        return solverService.solve(lhs)+solverService.solve(rhs);
    }
}