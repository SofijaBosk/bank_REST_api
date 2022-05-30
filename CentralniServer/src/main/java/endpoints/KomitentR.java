/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
import entities.ImaSediste;
import entities.Komitent;
import entities.Mesto;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author sofij
 */
@Path("komitenti")
@Stateless
public class KomitentR {
     @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @POST
    @Path("createkomitent")
    public Response createKomitent(Komitent komitent){
        
        Komitent k=em.find(Komitent.class, komitent.getIdK());
        if(k!=null) return Response.status(Response.Status.CONFLICT).entity("Komitent vec postoji").build();
         
        em.persist(komitent);
        return Response.status(Response.Status.CREATED).entity("Uspesno kreiran komitent "+komitent.getNaziv()).build();
    }
    
    
    @PUT
    @Path("promenasedista/{idM}")
    public Response promenaSedistaZaKomitenta(Komitent kom, @PathParam("idM")int idM){
        ImaSediste is=em.find(ImaSediste.class, kom.getIdK());
        if(is==null) Response.status(Response.Status.CONFLICT).entity("Komitent nema sediste").build();
        
        Mesto mesto=em.find(Mesto.class,idM);
        if(mesto==null) return Response.status(Response.Status.CONFLICT).entity("Mesto ne postoji").build();
        
        is.setIdMesta(mesto);
        return Response.status(Response.Status.ACCEPTED).entity("Promenjeno sediste za komitenta "+kom.getNaziv()+" na "+mesto.getNaziv()).build();
        
        
    }
    
    
    
    @GET
    @Path("getall")
    public Response getAllKomitent(){
        List<Komitent> listaKomitent=em.createNamedQuery("Komitent.findAll",Komitent.class).getResultList();
//        List<Komitent> k=new LinkedList<Komitent>();
//        for(int i=0;i<listaKomitent.size();i++) k.add(listaKomitent.get(i));
//        
//                
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Komitent>>(listaKomitent){}).build();
    }
}
