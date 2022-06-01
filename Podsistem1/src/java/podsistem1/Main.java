/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import java.io.Serializable;
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
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
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
    @Resource(lookup = "myKomitentRequest")
    private static Queue myKomitentRequest;
    @Resource(lookup = "myKomitentGetAll")
    private static Queue myKomitentGetAll;
    
    @Resource(lookup = "myMesta")
    private static Queue myMesta;
    @Resource(lookup = "myMestoRequest")
    private static Queue myMestoRequest;
    @Resource(lookup = "myMestoGetAll")
    private static Queue myMestoGetAll;
    
    @Resource(lookup = "myFilijale")
    private static Queue myFilijale;
    @Resource(lookup = "myFilijaleRequest")
    private static Queue myFilijaleRequest;
    @Resource(lookup = "myFilijaleGetAll")
    private static Queue myFilijaleGetAll;
    
    
    
    
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
                System.out.println("Uspesno prenesena poruka za mesto do podsistema!");
                
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

    @Transactional
    public void createFilijala(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myFilijale);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                Filijala fil=(Filijala)objMsg.getObject();
                Filijala novaFilijala=new Filijala();
                novaFilijala.setIdF(fil.getIdF());
                novaFilijala.setAdresa(fil.getAdresa());
                novaFilijala.setNaziv(fil.getNaziv());
                novaFilijala.setIdM(fil.getIdM());
                
                //em.persist(kom);
                
                //em.joinTransaction();
                utx.begin();
                em.joinTransaction();
                em.persist(novaFilijala);
                em.flush();
                utx.commit();
                System.out.println(novaFilijala.toString());
                System.out.println("Uspesno prenesena poruka za kreiranje filijale do podsistema!");
                
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
    
      
    //@Transactional
    public void getAllFilijala(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myFilijaleRequest);
        JMSProducer producer=context.createProducer();
        
//        em.clear();
//        em=emf.createEntityManager();
        consumer.setMessageListener((message) -> {
            try {
                TextMessage txtMsg=(TextMessage)message;
                String poruka=(String)txtMsg.getText();
                System.out.println("Stigla");
                if(poruka.equals("getall")){
                    System.out.println("Uspesno prenesena poruka za kreiranje filijale do podsistema!");
//                    utx.begin();
//                    em.joinTransaction();
                    List<Filijala> lista=em.createNamedQuery("Filijala.findAll",Filijala.class).getResultList(); 
                    //utx.commit();
                    
                    
//                    Filijala fil=lista.get(1);
//                    System.out.println(fil);
//                    for(int i=0;i<lista.size();i++){
//                        ObjectMessage objMsg=context.createObjectMessage();
//                        objMsg.setObject((Filijala)lista.get(i));
//                        producer.send(myFilijaleGetAll, objMsg);
//                    }
                    for(int i=0;i<lista.size();i++){
                        Filijala fil=lista.get(i);
                        ObjectMessage objMsg1=context.createObjectMessage();
                        objMsg1.setObject(fil);
                        producer.send(myFilijaleGetAll, objMsg1);
                    }
                    TextMessage objMsg=context.createTextMessage();
                    objMsg.setText("kraj");
                    producer.send(myFilijaleGetAll, objMsg);

                    //producer.send(myFilijale, objMsg);
                    //utx.commit();
                    System.out.println("Uspesno prenete sve filijale");
                }
            } catch (JMSException ex) {
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
         m.createFilijala();
         m.getAllFilijala();

         m.getAllKomitent();
         m.getAllMesto();
         
         while(true);
        
        
    }
    
    
    public void getAllKomitent(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myKomitentRequest);
        JMSProducer producer=context.createProducer();
        
//        em.clear();
//        em=emf.createEntityManager();
        consumer.setMessageListener((message) -> {
            try {
                TextMessage txtMsg=(TextMessage)message;
                String poruka=(String)txtMsg.getText();
                System.out.println("Stigla");
                if(poruka.equals("getall")){
                    System.out.println("Uspesno prenesena poruka za kreiranje komitente do podsistema!");
//                    utx.begin();
//                    em.joinTransaction();
                    List<Komitent> lista=em.createNamedQuery("Komitent.findAll",Komitent.class).getResultList(); 
                    //utx.commit();
                    
                    
//                    Filijala fil=lista.get(1);
//                    System.out.println(fil);
//                    for(int i=0;i<lista.size();i++){
//                        ObjectMessage objMsg=context.createObjectMessage();
//                        objMsg.setObject((Filijala)lista.get(i));
//                        producer.send(myFilijaleGetAll, objMsg);
//                    }
                    for(int i=0;i<lista.size();i++){
                        Komitent kom=lista.get(i);
                        ObjectMessage objMsg1=context.createObjectMessage();
                        objMsg1.setObject(kom);
                        producer.send(myKomitentGetAll, objMsg1);
                    }
                    TextMessage objMsg=context.createTextMessage();
                    objMsg.setText("kraj");
                    producer.send(myKomitentGetAll, objMsg);

                    //producer.send(myFilijale, objMsg);
                    //utx.commit();
                    System.out.println(lista);
                    System.out.println("Uspesno prenete sve komitente");
                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
   

    public void getAllMesto(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myMestoRequest);
        JMSProducer producer=context.createProducer();
        
//        em.clear();
//        em=emf.createEntityManager();
        consumer.setMessageListener((message) -> {
            try {
                TextMessage txtMsg=(TextMessage)message;
                String poruka=(String)txtMsg.getText();
                if(poruka.equals("getall")){
                    System.out.println("Uspesno prenesena poruka za dohvatanje mesta do podsistema!");
        
                    List<Mesto> lista=em.createNamedQuery("Mesto.findAll",Mesto.class).getResultList(); 
                    
                    for(int i=0;i<lista.size();i++){
                        Mesto mesto=lista.get(i);
                        ObjectMessage objMsg1=context.createObjectMessage();
                        objMsg1.setObject(mesto);
                        producer.send(myMestoGetAll, objMsg1);
                    }
                    TextMessage objMsg=context.createTextMessage();
                    objMsg.setText("kraj");
                    producer.send(myMestoGetAll, objMsg);

                    //producer.send(myFilijale, objMsg);
                    //utx.commit();
                    System.out.println(lista);
                    System.out.println("Uspesno prenete sve komitente");
                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    
    
}
