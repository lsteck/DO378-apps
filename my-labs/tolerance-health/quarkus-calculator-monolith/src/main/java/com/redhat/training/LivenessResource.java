package com.redhat.training;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/crash")
@ApplicationScoped
public class LivenessResource {
    private boolean alive=true;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String setCrash(){
        alive=false;

        return "Service not alive";
    }

    public boolean isAlive(){
        return alive;
    }
}
