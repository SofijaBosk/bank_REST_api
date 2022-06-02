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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
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
    
     @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
     
    @Resource(lookup = "MyRacun")
    private Queue MyRacun;
    @Resource(lookup = "MyRacunZatvori")
    private Queue MyRacunZatvori;
    
    @Resource(lookup = "racuniZaKomitentaZahtev")
    private Queue racuniZaKomitentaZahtev;
    @Resource(lookup = "racuniZaKomitenta")
    private Queue racuniZaKomitenta;
    
    
    @Resource(lookup = "transakcijeZaRacunZahtev")
    private Queue transakcijeZaRacunZahtev;
    @Resource(lookup = "transakcijeZaRacun")
    private Queue transakcijeZaRacun;
    
    
     
    @PUT
    @Path("createracun")
    public Response createRacun(Racun racun){
        
         try {
             Racun r=em.find(Racun.class, racun.getIdR());
             if(r==null) return Response.status(Response.Status.CONFLICT).entity("Racun ne postoji").build();
             if(!r.getStatus().equals("neaktivan")) return Response.status(Response.Status.CONFLICT).entity("Racun je vec aktivan").build();
             
             if(racun.getIdF()==null)return Response.status(Response.Status.CONFLICT).entity("Filijala nije uneta").build();
             Filijala f=em.find(Filijala.class, racun.getIdF().getIdF());
             if(f==null) return Response.status(Response.Status.CONFLICT).entity("Filijala ne postoji").build();
             if(racun.getIdK()==null)return Response.status(Response.Status.CONFLICT).entity("Komitent nije unet").build();
             Komitent k=em.find(Komitent.class, racun.getIdK().getIdK());
             if(k==null) return Response.status(Response.Status.CONFLICT).entity("Komitent ne postoji").build();
             
                          
             JMSContext context=connectionFactory.createContext();
             JMSProducer producer=context.createProducer();
             
             ObjectMessage objMsg=context.createObjectMessage();
             objMsg.setObject(racun);
             producer.send(MyRacun, objMsg);
             
             return Response.status(Response.Status.ACCEPTED).entity("Uspesno otvoren racun "+racun.toString()).build();
         } catch (JMSException ex) {
             Logger.getLogger(RacunR.class.getName()).log(Level.SEVERE, null, ex);
         }
         return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska kod otvaranja racuna").build();
    }
    
    @PUT
    @Path("zatvori/{idR}")
    public Response zatvaranjeRacuna(@PathParam("idR")int idR){
            
        try {
            Racun r=em.find(Racun.class, idR);
            if(r==null) return Response.status(Response.Status.CONFLICT).entity("Racun ne postoji").build();
            if(r.getStatus().equals("neaktivan")) return Response.status(Response.Status.CONFLICT).entity("Racun je vec neaktivan").build();

             
             JMSContext context=connectionFactory.createContext();
             JMSProducer producer=context.createProducer();
             
             ObjectMessage objMsg=context.createObjectMessage();
             objMsg.setObject(r);
             producer.send(MyRacunZatvori, objMsg);
             
             return Response.status(Response.Status.ACCEPTED).entity("Uspesno zatvoren racun "+r.toString()).build();
         } catch (JMSException ex) {
             Logger.getLogger(RacunR.class.getName()).log(Level.SEVERE, null, ex);
         }
         return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska kod zatvaranja racuna").build();
    }
    
    
    @GET
    @Path("getracuni/{idK}")
    public Response getAllRacuniZaKomitenta(@PathParam("idK")int idK){
         try {
             Komitent komitent=em.find(Komitent.class, idK);
             if(komitent==null) return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ovaj komitent ne postoji").build();
             
             
             
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(racuniZaKomitenta);
            
            TextMessage txtMsg=context.createTextMessage();
            txtMsg.setText("getall");
            txtMsg.setIntProperty("komitent", idK);
            //txtMsg.setStringProperty("klasa", "Filijala");     
            producer.send(racuniZaKomitentaZahtev, txtMsg);
            
            List<Racun> racuni=new LinkedList<>();
            while(true){
                Message objMsg=consumer.receive(5000);
                if(objMsg instanceof ObjectMessage){
                    ObjectMessage obj=(ObjectMessage)objMsg;
                    Racun r=(Racun) obj.getObject();
                    racuni.add(r);
                }
                else break;
            }
            System.out.println(racuni);


        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Racun>>(racuni){}).build();
         } catch (JMSException ex) {
             Logger.getLogger(RacunR.class.getName()).log(Level.SEVERE, null, ex);
         }
         return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska").build();
    }
   
    @GET
    @Path("transakcija/{idR}")
    public Response getAllTransakcijeZaracun(@PathParam("idR")int idR){
         try {
             Racun racun=em.find(Racun.class, idR);
             if(racun==null) return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ovaj racun ne postoji").build();
             
             
             
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(transakcijeZaRacun);
            
            TextMessage txtMsg=context.createTextMessage();
            txtMsg.setText("getall");
            txtMsg.setIntProperty("racun", idR);
            //txtMsg.setStringProperty("klasa", "Filijala");     
            producer.send(transakcijeZaRacunZahtev, txtMsg);
            
            List<Transakcija> transakcije=new LinkedList<>();
            while(true){
                Message objMsg=consumer.receive(5000);
                if(objMsg instanceof ObjectMessage){
                    ObjectMessage obj=(ObjectMessage)objMsg;
                    Transakcija r=(Transakcija) obj.getObject();
                    transakcije.add(r);
                }
                else break;
            }
            System.out.println(transakcije);

        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Transakcija>>(transakcije){}).build();
         } catch (JMSException ex) {
             Logger.getLogger(RacunR.class.getName()).log(Level.SEVERE, null, ex);
         }
         return Response.status(Response.Status.EXPECTATION_FAILED).entity("Greska").build();
        
    }
    
    //TEST METODA
    @GET
    @Path("test/{idR}")
    public Response getRacun(@PathParam("idR")int idR){
        Racun racun=em.find(Racun.class, idR);
        return Response.status(Response.Status.OK).entity(racun).build();
    }
   
    
}
