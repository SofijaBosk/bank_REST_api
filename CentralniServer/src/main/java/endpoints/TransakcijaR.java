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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
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
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "myPrenos")
    private Queue myPrenos;
    @Resource(lookup = "myUplata")
    private Queue myUplata;
    @Resource(lookup = "myIsplata")
    private Queue myIsplata;
    
    @POST
    @Path("{idRacunNa}")
    public Response createPrenost(Transakcija trans,@PathParam("idRacunNa") int idRacunNa){
        try {
            Racun racunNa=em.find(Racun.class, idRacunNa);
            if(racunNa==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
            
            Racun racunOd=em.find(Racun.class, trans.getIdRacun().getIdR());
            if(racunOd==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun sa kog saljete novac ne postoji").build();
            
            if(racunOd.getStatus().equals("blokiran")) return Response.status(Response.Status.BAD_REQUEST).entity("Racun sa kog saljete novac je blokiran").build();
            
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            
            
            ObjectMessage objMsg2=context.createObjectMessage();
            objMsg2.setObject(racunNa);
            objMsg2.setStringProperty("racun", "RacunNa");
            producer.send(myPrenos, objMsg2);
            
            ObjectMessage objTrans=context.createObjectMessage();
            objTrans.setObject(trans);
            objTrans.setStringProperty("racun", "trans");
            producer.send(myPrenos, objTrans);
            
            
            return Response.status(Response.Status.CREATED).entity("Kreirana transakcija prenosa sa racuna: "+racunOd.toString()+"na racun: "+racunNa.toString()).build();
        } catch (JMSException ex) {
            Logger.getLogger(TransakcijaR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska").build();
    }

    @GET
    @Path("getall/{idT}")
    public Response getTransakcije(@PathParam("idT")int idT){
        Transakcija trans=em.find(Transakcija.class, idT);
        return Response.status(Response.Status.FOUND).entity(trans).build();
        
    }
    
    
    @POST
    @Path("uplata")
    public Response createUplata(Transakcija trans,@QueryParam("idF") int idF){
        try {
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            
            Racun racun=em.find(Racun.class, trans.getIdRacun().getIdR());
            if(racun==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
            Filijala fil=em.find(Filijala.class, idF);
            if(fil==null) return Response.status(Response.Status.NOT_FOUND).entity("Filijala ne postoji").build();

            ObjectMessage objMsg=context.createObjectMessage();
            objMsg.setObject(trans);
            objMsg.setIntProperty("filijala", idF);
            producer.send(myUplata, objMsg);


            return Response.status(Response.Status.CREATED).entity("Kreirana transakcija uplate na racuna: "+racun.toString()).build();
        } catch (JMSException ex) {
            Logger.getLogger(TransakcijaR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska").build();
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
        try {
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            
            Racun racun=em.find(Racun.class, trans.getIdRacun().getIdR());
            if(racun==null) return Response.status(Response.Status.NOT_FOUND).entity("Racun na koji saljete novac ne postoji").build();
            Filijala fil=em.find(Filijala.class, idF);
            if(fil==null) return Response.status(Response.Status.NOT_FOUND).entity("Filijala ne postoji").build();

            if(racun.getStatus().equals("blokiran")) return Response.status(Response.Status.BAD_REQUEST).entity("Racun sa kog uzimate novac je blokiran").build();

            
            ObjectMessage objMsg=context.createObjectMessage();
            objMsg.setObject(trans);
            objMsg.setIntProperty("filijala", idF);
            producer.send(myIsplata, objMsg);

        return Response.status(Response.Status.CREATED).entity("Kreirana transakcija isplata sa racuna: "+racun.toString()).build();
        
        } catch (JMSException ex) {
            Logger.getLogger(TransakcijaR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska").build();
    }
    
    
    
    @GET
    @Path("getisplate/{idT}")
    public Response getisplate(@PathParam("idT")int idT){
        Isplata t=em.find(Isplata.class, idT);
         return Response.status(Response.Status.FOUND).entity(t).build();
             
    }
    
    
}
