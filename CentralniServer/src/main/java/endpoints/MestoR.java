/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Mesto;
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
        List<Mesto> listaMesta=em.createNamedQuery("Mesto.findAll",Mesto.class).getResultList();
        System.out.println(listaMesta);
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Mesto>>(listaMesta){}).build();
    }
    
}
