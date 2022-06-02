/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
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
@Path("mesta")
@Stateless
public class MestoR {
    
    @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "myMesta")
    private Queue myMesta;
    @Resource(lookup = "myMestoRequest")
    private Queue myMestoRequest;
    @Resource(lookup = "myMestoGetAll")
    private Queue myMestoGetAll;
    
    @POST
    @Path("createmesto")
    public Response createMesto(Mesto mesto){
        try {
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            
            Mesto m=em.find(Mesto.class, mesto.getIdM());
            if(m!=null) return Response.status(Response.Status.CONFLICT).entity("Mesto vec postoji").build();
            //em.persist(mesto);

            ObjectMessage objMsg=context.createObjectMessage();
            objMsg.setObject(mesto);
            producer.send(myMesta, objMsg);
            
            return Response.status(Response.Status.CREATED).entity("Uspesno kreirano mesto "+mesto.getNaziv()).build();
        } catch (JMSException ex) {
            Logger.getLogger(MestoR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska kod prenosa mesta").build();
    }
    
    @GET
    @Path("getall")
    public Response getAllMesto(){
         try {
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myMestoGetAll);
            
            TextMessage txtMsg=context.createTextMessage();
            txtMsg.setText("getall");
            //txtMsg.setStringProperty("klasa", "Filijala");     
            producer.send(myMestoRequest, txtMsg);
            
            List<Mesto> mesta=new LinkedList<>();
            while(true){
                Message objMsg=consumer.receive(5000);
                if(objMsg instanceof ObjectMessage){
                    ObjectMessage obj=(ObjectMessage)objMsg;
                    Mesto mes=(Mesto) obj.getObject();
                    mesta.add(mes);
                }
                else break;
            }
            System.out.println(mesta);
            
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Mesto>>(mesta){}).build();
        } catch (JMSException ex) {
            Logger.getLogger(FilijalaR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.NOT_FOUND).entity("Greska kod dohvatanja filijala").build();
    }
             
}
