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
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import requests.KomitentRequest;

/**
 *
 * @author sofij
 */
@Path("komitenti")
@Stateless
public class KomitentR {
    @PersistenceContext(unitName = "my_project_persistence")
    EntityManager em; 
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "myKomitenti")
    private Queue myKomitenti;
    
    @POST
    @Path("createkomitent")
    public Response createKomitent(Komitent komitent){    
            JMSContext context=connectionFactory.createContext();
            JMSProducer producer=context.createProducer();
        try {
            Komitent k=em.find(Komitent.class, komitent.getIdK());
            if(k!=null) return Response.status(Response.Status.CONFLICT).entity("Komitent vec postoji").build();
  
            ObjectMessage objMsg=context.createObjectMessage();
            objMsg.setObject(komitent);
            producer.send(myKomitenti, objMsg);
            
//            em.persist(komitent);
            return Response.status(Response.Status.CREATED).entity("Uspesno kreiran komitent "+komitent.getNaziv()).build();
        } catch (JMSException ex) {
            Logger.getLogger(KomitentR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska tokom prenosa komitenta").build();
        
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
    @Path("getall/{idK}")
    public Response getAllKomitent(@PathParam("idK")int idK){
        Komitent komitent=em.find(Komitent.class, idK);
        return Response.status(Response.Status.OK).entity(komitent).build();
        
        
//        List<Komitent> listaKomitent=em.createNamedQuery("Komitent.findAll",Komitent.class).getResultList();             
//        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Komitent>>(listaKomitent){}).build();
    }
}
