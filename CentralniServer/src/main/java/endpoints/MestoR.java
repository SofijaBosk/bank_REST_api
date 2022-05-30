/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Mesto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author sofij
 */
@Path("mesta")
@Stateless
public class MestoR {
    
    @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @POST
    @Path("createmesto")
    public Response createMesto(Mesto mesto){
        Mesto m=em.find(Mesto.class, mesto.getIdM());
        if(m!=null) return Response.status(Response.Status.CONFLICT).entity("Mesto vec postoji").build();
        em.persist(mesto);
        return Response.status(Response.Status.CREATED).entity("Uspesno kreirano mesto "+mesto.getNaziv()).build();
    }
    
    @GET
    @Path("getall")
    public Response getAllMesto(){
        List<Mesto> listaMesta=em.createNamedQuery("Mesto.findAll",Mesto.class).getResultList();
        System.out.println(listaMesta);
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Mesto>>(listaMesta){}).build();
    }
    
}
