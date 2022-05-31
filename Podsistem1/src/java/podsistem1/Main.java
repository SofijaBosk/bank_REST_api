/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entities.Komitent;
import entities.Mesto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;
import requests.KomitentRequest;

/**
 *
 * @author sofij
 */
//@Stateless

public class Main {
    @PersistenceUnit(unitName = "Podsistem1PU")
    private static EntityManagerFactory emf;
    
    @PersistenceContext(unitName = "Podsistem1PU")
    private static EntityManager em;
    
    @Resource
    private static UserTransaction utx;
    
    

//    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
//    private static EntityManager em=emf.createEntityManager();
        
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "myKomitenti")
    private static Queue myKomitenti;
    
    @Resource(lookup = "myMesta")
    private static Queue myMesta;
    
//    @Transactional
//    public void createKomitent(){
//        JMSContext context=connectionFactory.createContext();
//        JMSConsumer consumer=context.createConsumer(myKomitenti);
//        em=emf.createEntityManager();
//        while(true){
//            try {
//                ObjectMessage objMsg=(ObjectMessage)consumer.receive();
//                Komitent komitent=(Komitent)objMsg.getObject();
//                Komitent kom=new Komitent();
//                kom.setIdK(komitent.getIdK());
//                kom.setAdresa(komitent.getAdresa());
//                kom.setNaziv(komitent.getNaziv());
//                //em.persist(kom);
//                
//                //em.joinTransaction();
//                utx.begin();
//                em.joinTransaction();
//                em.persist(kom);
//                em.flush();
//                utx.commit();
//                System.out.println(kom.toString());
//                System.out.println("Uspesno prenesena poruka za komitenta do podsistema!");
//            } catch (JMSException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (NotSupportedException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SystemException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (RollbackException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (HeuristicMixedException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (HeuristicRollbackException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SecurityException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IllegalStateException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
    @Transactional
    public void createKomitent(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myKomitenti);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                Komitent komitent=(Komitent)objMsg.getObject();
                Komitent kom=new Komitent();
                kom.setIdK(komitent.getIdK());
                kom.setAdresa(komitent.getAdresa());
                kom.setNaziv(komitent.getNaziv());
                //em.persist(kom);
                
                //em.joinTransaction();
                utx.begin();
                em.joinTransaction();
                em.persist(kom);
                em.flush();
                utx.commit();
                System.out.println(kom.toString());
                System.out.println("Uspesno prenesena poruka za komitenta do podsistema!");
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }
    
    
    public void createMesto() {
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myMesta);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                Mesto mesto=(Mesto)objMsg.getObject();
                Mesto mes=new Mesto();
                mes.setIdM(mesto.getIdM());
                mes.setNaziv(mesto.getNaziv());
                mes.setPostanskiBroj(mesto.getPostanskiBroj());
                
                //em.joinTransaction();
                utx.begin();
                em.joinTransaction();
                em.persist(mes);
                em.flush();
                utx.commit();
                System.out.println(mes.toString());
                System.out.println("Uspesno prenesena poruka za komitenta do podsistema!");
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }
    
    public static void main(String[] args) {
        em=emf.createEntityManager();
        Main m=new Main();
        //m.createKomitent();
        //System.out.println(em.getTransaction());
         m.createKomitent();
         m.createMesto();
//         
         while(true);
        
        
    }

    
    
}
