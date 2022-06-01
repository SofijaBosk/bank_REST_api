/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
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
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "myFilijale")
    private Queue myFilijale;
    @Resource(lookup = "myFilijaleRequest")
    private Queue myFilijaleRequest;
    @Resource(lookup = "myFilijaleGetAll")
    private Queue myFilijaleGetAll;
    
    
    @POST
    @Path("createfilijala")
    public Response createFilijala(Filijala fil){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        
        try {
            if(fil!=null) Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unesite filijalu za kreiranje").build();
                 
            
            Filijala f=em.find(Filijala.class, fil.getIdF());
            if(f!=null) return Response.status(Response.Status.CONFLICT).entity("Filijala vec postoji").build();

            Mesto mesto=em.find(Mesto.class,fil.getIdM().getIdM());
            if(mesto==null) return Response.status(Response.Status.CONFLICT).entity("Mesto ne postoji").build();
         
//        em.persist(fil);
            ObjectMessage objMsg=context.createObjectMessage();
            objMsg.setObject(fil);
            producer.send(myFilijale, objMsg); 

        return Response.status(Response.Status.CREATED).entity("Uspesno kreirana filijala "+fil.getNaziv()).build();
        } catch (JMSException ex) {
            Logger.getLogger(KomitentR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska tokom kreiranja filijale").build();
    }
    
    @GET
    @Path("getall") 
    public Response getAllFilijala(){
//        Filijala f=em.find(Filijala.class, 3);
//        return Response.status(Response.Status.OK).entity(f).build();
           

        try {
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myFilijaleGetAll);
            
            TextMessage txtMsg=context.createTextMessage();
            txtMsg.setText("getall");
            //txtMsg.setStringProperty("klasa", "Filijala");     
            producer.send(myFilijaleRequest, txtMsg);
            
            List<Filijala> filijale=new LinkedList<>();
            while(true){
                Message objMsg=consumer.receive(5000);
                if(objMsg instanceof ObjectMessage){
                    ObjectMessage obj=(ObjectMessage)objMsg;
                    Filijala fil=(Filijala) obj.getObject();
                    filijale.add(fil);
                }
                else break;
            }
            System.out.println(filijale);
            
//            List<Filijala> listaFilijala=em.createNamedQuery("Filijala.findAll",Filijala.class).getResultList();
//            System.out.println(listaFilijala);
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Filijala>>(filijale){}).build();
        } catch (JMSException ex) {
            Logger.getLogger(FilijalaR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.NOT_FOUND).entity("Greska kod dohvatanja filijala").build();
    }
}
