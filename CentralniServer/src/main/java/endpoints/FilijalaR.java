/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
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
@Path("filijale")
@Stateless
public class FilijalaR {
    @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @POST
    @Path("createfilijala")
    public Response createFilijala(Filijala fil){
        
        Filijala f=em.find(Filijala.class, fil.getIdF());
        if(f!=null) return Response.status(Response.Status.CONFLICT).entity("Filijala vec postoji").build();
        
        Mesto mesto=em.find(Mesto.class,fil.getIdM().getIdM());
        if(mesto==null) return Response.status(Response.Status.CONFLICT).entity("Mesto ne postoji").build();
         
        em.persist(fil);
        return Response.status(Response.Status.CREATED).entity("Uspesno kreirana filijala "+fil.getNaziv()).build();
    }
    
    @GET
    @Path("getall")
    public Response getAllFilijala(){
        List<Filijala> listaFilijala=em.createNamedQuery("Filijala.findAll",Filijala.class).getResultList();
        System.out.println(listaFilijala);
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Filijala>>(listaFilijala){}).build();
    }
}
