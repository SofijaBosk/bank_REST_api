/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fje;

import entities.Komitent;
import entities.Mesto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author sofij
 */
public class KomitentServis {
    @PersistenceContext(unitName = "Podsistem1PU")
    private static EntityManager em;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "myKomitenti")
    private static Queue myKomitenti;
    
    public static void main(String[] args) {
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myKomitenti);
        
         while(true){
            try {
                ObjectMessage objMsg=(ObjectMessage)consumer.receive();
                Komitent komitent=(Komitent)objMsg.getObject();
                System.out.println(komitent);
                Komitent k=em.find(Komitent.class,6);
                Komitent kom=new Komitent();
                kom.setIdK(7);
                kom.setAdresa("Adresa10");
                kom.setNaziv("KomitentTest");
                //em.persist(kom);
                Mesto mesto=new Mesto(15, "Mesto15", 678901);
                
                em.persist(mesto);
                //em.flush();
                
                System.out.println(mesto.toString());
                System.out.println("Uspesno prenesena poruka do podsistema!");
            } catch (JMSException ex) {
                Logger.getLogger(KomitentServis.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
