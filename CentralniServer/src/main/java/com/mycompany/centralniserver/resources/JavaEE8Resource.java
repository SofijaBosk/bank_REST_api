package com.mycompany.centralniserver.resources;

import entities.Racun;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("javaee8")
public class JavaEE8Resource {
    
    @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @GET
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
    
    @POST
    @Path("test")
    public Response test(){
        Racun racun=em.find(Racun.class, 1);
        return Response.status(Response.Status.OK).entity(racun).build();
    }
}
