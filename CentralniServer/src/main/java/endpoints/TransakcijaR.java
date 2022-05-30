/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
import entities.Isplata;
import entities.Prenos;
import entities.Racun;
import entities.Transakcija;
import entities.Uplata;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author sofij
 */

@Path("transakcije")
@Stateless
public class TransakcijaR {
    
    @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @POST
    @Path("{idRacunNa}")
    public Response createPrenost(Transakcija trans,@PathParam("idRacunNa") int idRacunNa){
        Racun racunNa=em.find(Racun.class, idRacunNa);
        if(racunNa==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
        
        Racun racunOd=em.find(Racun.class, trans.getIdRacun().getIdR());
        if(racunOd==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun sa kog saljete novac ne postoji").build();
        
        if(racunOd.getStatus().equals("blokiran")) return Response.status(Response.Status.BAD_REQUEST).entity("Racun sa kog saljete novac je blokiran").build();
        //Prenos sredstava sa racunOd do racunNa
        racunOd.setStanje(racunOd.getStanje()-trans.getIznos());   
        if(racunOd.getStanje()<-racunOd.getDozvoljeniMinus()) {
            racunOd.setStatus("blokiran");
        }
        
        racunNa.setStanje(racunNa.getStanje()+trans.getIznos());
        if(racunNa.getStanje()>= -racunNa.getDozvoljeniMinus()) {
            racunNa.setStatus("aktivan");
        }
        racunNa.setBrTransakcija(racunNa.getBrTransakcija()+1);
        em.flush();
        Transakcija novaTransakcija=new Transakcija();
        novaTransakcija.setDatumObavljanja(new Date());
        novaTransakcija.setRedBr(trans.getRedBr()+1);
        novaTransakcija.setIdRacun(racunOd);
        novaTransakcija.setIdT(trans.getIdT());
        novaTransakcija.setSvrha("prenos");
        novaTransakcija.setIznos(trans.getIznos());
        em.persist(novaTransakcija); em.flush();

        String query="SELECT MAX(p.idPrenos) from Prenos p";
        Integer idPrenos=em.createQuery(query,Integer.class).getFirstResult();
        System.out.println(idPrenos);
        Prenos prenos=new Prenos();
        prenos.setIdPrenos(novaTransakcija.getIdT());
        prenos.setIdRacunNa(racunNa);
        //prenos.setTransakcija(novaTransakcija);
        System.out.println(prenos);
        em.persist(prenos);
        
        return Response.status(Response.Status.CREATED).entity("Kreirana transakcija prenosa sa racuna: "+racunOd.toString()+"na racun: "+racunNa.toString()).build();
    }
    
/*    @POST
    public Response createPrenos(Prenos prenos){

        Racun racunNa=em.find(Racun.class, prenos.getIdRacunNa().getIdR());
        if(racunNa==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
        
        Racun racunOd=em.find(Racun.class, prenos.getTransakcija().getIdRacun().getIdR());
        if(racunOd==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun sa kog saljete novac ne postoji").build();
        
        if(racunOd.getStatus().equals("blokiran")) return Response.status(Response.Status.BAD_REQUEST).entity("Racun sa kog saljete novac je blokiran").build();
        //Prenos sredstava sa racunOd do racunNa
        racunOd.setStanje(racunOd.getStanje()-prenos.getTransakcija().getIznos());   
        if(racunOd.getStanje()<-racunOd.getDozvoljeniMinus()) {
            racunOd.setStatus("blokiran");
        }
        
        racunNa.setStanje(racunNa.getStanje()+prenos.getTransakcija().getIznos());
        if(racunNa.getStanje()>= -racunNa.getDozvoljeniMinus()) {
            racunNa.setStatus("aktivan");
        }
        racunNa.setBrTransakcija(racunNa.getBrTransakcija()+1);
        em.persist(prenos);
        
//        Transakcija novaTransakcija=new Transakcija();
//
//        novaTransakcija.setDatumObavljanja(new Date());
//        novaTransakcija.setRedBr(trans.getRedBr()+1);
//        novaTransakcija.setIdRacun(racunOd);
//        novaTransakcija.setIdT(trans.getIdT());
//        novaTransakcija.setSvrha("prenos");
//        novaTransakcija.setIznos(trans.getIznos());
//        em.persist(novaTransakcija);


//            prenos.getTransakcija().setDatumObavljanja(new Date());
//            prenos.getTransakcija().setRedBr(prenos.getTransakcija().getRedBr()+1);
//            prenos.getTransakcija().setIdRacun(racunOd);
//            prenos.getTransakcija().setSvrha("prenos");
            //em.persist(prenos);

//        Transakcija nova=em.find(Transakcija.class, 11);
//        Prenos prenos=new Prenos();
////        prenos.setIdRacunNa(racunNa);
//        System.out.println(prenos);
//        em.persist(prenos);
        
          
//        String query="SELECT MAX(p.idPrenos) from Prenos p";
//        Integer idPrenos=em.createQuery(query,Integer.class).getFirstResult();
//        System.out.println(idPrenos);
        
       

        
//        prenos.setTransakcija(novaTransakcija);
//        System.out.println(prenos);
//        em.persist(prenos);
       
        
        
        return Response.status(Response.Status.CREATED).entity("Kreirana transakcija prenosa sa racuna: "+racunOd.toString()+"na racun: "+racunNa.toString()).build();
    }
    
 */   
    @GET
    @Path("getall")
    public Response getTransakcije(){
 
        List<Transakcija> transList=em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();
        return Response.status(Response.Status.FOUND).entity(new GenericEntity<List<Transakcija>>(transList){}).build();
    }
    
    
    @POST
    @Path("uplata")
    public Response createUplata(Transakcija trans,@QueryParam("idF") int idF){

        Racun racun=em.find(Racun.class, trans.getIdRacun().getIdR());
        if(racun==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
        
        
        racun.setStanje(racun.getStanje()+trans.getIznos());
        if(racun.getStanje()>= -racun.getDozvoljeniMinus()) {
            racun.setStatus("aktivan");
        }
        racun.setBrTransakcija(racun.getBrTransakcija()+1);
        em.flush();
        Transakcija novaTransakcija=new Transakcija();
        novaTransakcija.setDatumObavljanja(new Date());
        novaTransakcija.setRedBr(trans.getRedBr()+1);
        novaTransakcija.setIdRacun(racun);
        novaTransakcija.setIdT(trans.getIdT());
        novaTransakcija.setSvrha("uplata");
        novaTransakcija.setIznos(trans.getIznos());
        em.persist(novaTransakcija); em.flush();

        Filijala fil=em.find(Filijala.class, idF);
        
        Uplata uplata=new Uplata();
        uplata.setIdUplata(novaTransakcija.getIdT());
        uplata.setIdFilijala(fil);
        System.out.println(uplata);
        em.persist(uplata);
        
        return Response.status(Response.Status.CREATED).entity("Kreirana transakcija uplate na racuna: "+racun.toString()).build();
    }
    
    
    
    @GET
    @Path("getuplate")
    public Response getUplate(@PathParam("idT")int idT){
             
            List<Uplata> transList=em.createNamedQuery("Uplata.findAll", Uplata.class).getResultList();
            return Response.status(Response.Status.FOUND).entity(new GenericEntity<List<Uplata>>(transList){}).build();
    }
    
    
    @POST
    @Path("isplata")
    public Response createIsplata(Transakcija trans,@QueryParam("idF") int idF){

        Racun racun=em.find(Racun.class, trans.getIdRacun().getIdR());
        if(racun==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
        
        
        racun.setStanje(racun.getStanje()-trans.getIznos());
        if(racun.getStanje()< -racun.getDozvoljeniMinus()) {
            racun.setStatus("blokiran");
        }
        racun.setBrTransakcija(racun.getBrTransakcija()+1);
        em.flush();
        Transakcija novaTransakcija=new Transakcija();
        novaTransakcija.setDatumObavljanja(new Date());
        novaTransakcija.setRedBr(trans.getRedBr()+1);
        novaTransakcija.setIdRacun(racun);
        novaTransakcija.setIdT(trans.getIdT());
        novaTransakcija.setSvrha("isplata");
        novaTransakcija.setIznos(trans.getIznos());
        em.persist(novaTransakcija); em.flush();

        Filijala fil=em.find(Filijala.class, idF);
        
        Isplata isplata=new Isplata();
        isplata.setIdIsplata(novaTransakcija.getIdT());
        isplata.setIdFilijala(fil);
        //prenos.setTransakcija(novaTransakcija);
        System.out.println(isplata);
        em.persist(isplata);
        
        return Response.status(Response.Status.CREATED).entity("Kreirana transakcija isplata sa racuna: "+racun.toString()).build();
    }
    
    
    
    @GET
    @Path("getisplate")
    public Response getisplate(@PathParam("idT")int idT){
             
            List<Isplata> transList=em.createNamedQuery("Isplata.findAll", Isplata.class).getResultList();
            return Response.status(Response.Status.FOUND).entity(new GenericEntity<List<Isplata>>(transList){}).build();
    }
    
    
}
