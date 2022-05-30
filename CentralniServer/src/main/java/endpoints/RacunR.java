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
import entities.Racun;
import entities.Transakcija;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author sofij
 */
@Path("racuni")
@Stateless
public class RacunR {
    
     @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @PUT
    @Path("createracun")
    public Response createRacun(Racun racun){
        
        Racun r=em.find(Racun.class, racun.getIdR());
        if(r==null) return Response.status(Response.Status.CONFLICT).entity("Racun ne postoji").build();
        if(!r.getStatus().equals("neaktivan")) return Response.status(Response.Status.CONFLICT).entity("Racun je vec aktivan").build();
        
        if(racun.getIdF()==null)return Response.status(Response.Status.CONFLICT).entity("Filijala nije uneta").build();
        Filijala f=em.find(Filijala.class, racun.getIdF().getIdF());
        if(f==null) return Response.status(Response.Status.CONFLICT).entity("Filijala ne postoji").build();
        if(racun.getIdK()==null)return Response.status(Response.Status.CONFLICT).entity("Komitent nije unet").build();
        Komitent k=em.find(Komitent.class, racun.getIdK().getIdK());
        if(k==null) return Response.status(Response.Status.CONFLICT).entity("Komitent ne postoji").build();
        
        
        //Date vreme=em.createQuery("SELECT CURRENT_TIMESTAMP",Date.class).getSingleResult();
        Date vreme=new Date();
        System.out.println(vreme);
        r.setDatumOtvaranja(vreme);
        r.setStatus("aktivan");
        r.setBrTransakcija(0);
        r.setIdK(racun.getIdK());
        return Response.status(Response.Status.ACCEPTED).entity("Uspesno otvoren racun "+racun.toString()).build();
    }
    
    @PUT
    @Path("zatvori/{idR}")
    public Response zatvaranjeRacuna(@PathParam("idR")int idR){
        
        Racun r=em.find(Racun.class, idR);
        if(r==null) return Response.status(Response.Status.CONFLICT).entity("Racun ne postoji").build();
        if(r.getStatus().equals("neaktivan")) return Response.status(Response.Status.CONFLICT).entity("Racun je vec neaktivan").build();
       
        r.setDatumOtvaranja(null);
        r.setStatus("neaktivan");
        r.setBrTransakcija(0);
        r.setIdK(null);
        return Response.status(Response.Status.ACCEPTED).entity("Uspesno zatvoren racun "+r.toString()).build();
    }
    
    
    @GET
    @Path("getracuni/{idK}")
    public Response getAllRacuniZaKomitenta(@PathParam("idK")int idK){
        Komitent komitent=em.find(Komitent.class, idK);
        List<Racun> r=new LinkedList<>();
        r=komitent.getRacunList();
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Racun>>(r){}).build();
    }
   
    @GET
    @Path("transakcija/{idR}")
    public Response getAllTransakcijeZaracun(@PathParam("idR")int idR){
        Racun racun=em.find(Racun.class, idR);
        List<Transakcija> tr=racun.getTransakcijaList();
        System.out.println(tr);
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Transakcija>>(tr){}).build();
    }
   
    
}
