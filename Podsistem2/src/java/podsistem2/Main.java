/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entities.Filijala;
import entities.Isplata;
import entities.Komitent;
import entities.Mesto;
import entities.Prenos;
import entities.Racun;
import entities.Transakcija;
import entities.Uplata;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;

/**
 *
 * @author sofij
 */
public class Main {
    @PersistenceUnit(unitName = "Podsistem2PU")
    private static EntityManagerFactory emf;
    
    @PersistenceContext(unitName = "Podsistem2PU")
    private static EntityManager em;
    
    @Resource
    private static UserTransaction utx;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "MyRacun")
    private static Queue MyRacun;
    @Resource(lookup = "myPrenos")
    private static Queue myPrenos;
    @Resource(lookup = "myUplata")
    private  static Queue myUplata;
    @Resource(lookup = "myIsplata")
    private  static Queue myIsplata;
    
    @Resource(lookup = "MyRacunZatvori")
    private static Queue MyRacunZatvori;
    
    @Resource(lookup = "racuniZaKomitentaZahtev")
    private static Queue racuniZaKomitentaZahtev;
    @Resource(lookup = "racuniZaKomitenta")
    private static Queue racuniZaKomitenta;
   
    
    @Resource(lookup = "transakcijeZaRacunZahtev")
    private static Queue transakcijeZaRacunZahtev;
    @Resource(lookup = "transakcijeZaRacun")
    private static Queue transakcijeZaRacun;
    
//    @Resource(lookup = "myKomitentRequest")
//    private static Queue myKomitentRequest;
//    @Resource(lookup = "myKomitentGetAll")
//    private static Queue myKomitentGetAll;
    
    /**
     * @param args the command line arguments
     */
    
    @Transactional
    public void otvoriRacun(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(MyRacun);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                Racun r=(Racun)objMsg.getObject();
                
                Racun racun=em.find(Racun.class, r.getIdR());
                utx.begin();
                em.joinTransaction();
                Date vreme=new Date();
                racun.setDatumOtvaranja(vreme);
                racun.setStatus("aktivan");
                racun.setBrTransakcija(0);
                racun.setIdK(r.getIdK());
                em.flush();
                utx.commit();
//                
//                em.joinTransaction();
//                em.flush();
                
                System.out.println(racun.toString());
                System.out.println("Uspesno otvoren racun!");
                
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
    private void zatvoriRacun() {
            
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(MyRacunZatvori);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                Racun r=(Racun)objMsg.getObject();
                
                Racun racun=em.find(Racun.class, r.getIdR());
                utx.begin();
                em.joinTransaction();
                racun.setDatumOtvaranja(null);
                racun.setStatus("neaktivan");
                racun.setBrTransakcija(0);
                racun.setIdK(null);
                em.flush();
                utx.commit();
                
                System.out.println("Uspesno zatvoren racun " + racun.toString());
                
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
     public void createPrenos() {
            
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myPrenos);
        
        while(true){
            try {
                Racun racunOd=null,racunNa=null;
                Transakcija trans=null;
//                ObjectMessage objMsg=(ObjectMessage) consumer.receive();
//                if(objMsg.getStringProperty("racun").equals("RacunOd"))
//                     racunOd=(Racun)objMsg.getObject();
//                else if(objMsg.getStringProperty("racun").equals("RacunNa"))
//                     racunNa=(Racun)objMsg.getObject();  
//                else 
//                    trans=(Transakcija)objMsg.getObject();
                
                ObjectMessage objMsg2=(ObjectMessage) consumer.receive();
                if(objMsg2.getStringProperty("racun").equals("RacunNa"))
                     racunNa=(Racun)objMsg2.getObject(); 
                else 
                    trans=(Transakcija)objMsg2.getObject();
                
                ObjectMessage objMsg3=(ObjectMessage) consumer.receive();
                if(objMsg3.getStringProperty("racun").equals("RacunNa"))
                     racunNa=(Racun)objMsg3.getObject(); 
                else 
                    trans=(Transakcija)objMsg3.getObject();
                

                System.out.println(racunNa);
                System.out.println(trans);

                racunOd=em.find(Racun.class, trans.getIdRacun().getIdR());
                racunNa=em.find(Racun.class, racunNa.getIdR());
                
            //Prenos sredstava sa racunOd do racunNa
            utx.begin();
            em.joinTransaction();
            
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
                
              em.flush();
              utx.commit();  
                

                System.out.println("Uspesno prenete pare");      
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                    
        }
    }
    
     
     
   @Transactional
    private void uplataNaRacun() {
            
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myUplata);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                int idF=objMsg.getIntProperty("filijala");
                Transakcija trans=(Transakcija)objMsg.getObject();
                
                Filijala fil=em.find(Filijala.class, idF);
                System.out.println("Filijala"+fil);
                
                Racun racun=em.find(Racun.class, trans.getIdRacun().getIdR());
                
                utx.begin();
                em.joinTransaction();
                racun.setStanje(racun.getStanje()+trans.getIznos());
                
                
                if(racun.getStanje()>= -racun.getDozvoljeniMinus()) {
                    racun.setStatus("aktivan");
                }
                racun.setBrTransakcija(racun.getBrTransakcija()+1);
                em.flush();
                Transakcija novaTransakcija=new Transakcija();
                novaTransakcija.setDatumObavljanja(new Date());
                novaTransakcija.setRedBr(racun.getBrTransakcija());
                novaTransakcija.setIdRacun(racun);
                novaTransakcija.setIdT(trans.getIdT());
                novaTransakcija.setSvrha("uplata");
                novaTransakcija.setIznos(trans.getIznos());
                em.persist(novaTransakcija); 
                em.flush();
                utx.commit();


               utx.begin();
               em.joinTransaction();
                Uplata uplata=new Uplata();
                uplata.setIdUplata(novaTransakcija.getIdT());
                uplata.setIdFilijala(fil);
                System.out.println(uplata);
                em.persist(uplata);

                em.flush();
                utx.commit();
                
                System.out.println("Uspesna uplata na racun " + trans.getIdRacun());
                
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
    private void isplataSaRacun() {
            
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(myIsplata);
        
        consumer.setMessageListener((message) -> {
            try {
                ObjectMessage objMsg=(ObjectMessage)message;
                int idF=objMsg.getIntProperty("filijala");
                Transakcija trans=(Transakcija)objMsg.getObject();
                
                Filijala fil=em.find(Filijala.class, idF);
                System.out.println("Filijala"+fil);
                
                Racun racun=em.find(Racun.class, trans.getIdRacun().getIdR());
                
                utx.begin();
                em.joinTransaction();
                
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

                Isplata isplata=new Isplata();
                isplata.setIdIsplata(novaTransakcija.getIdT());
                isplata.setIdFilijala(fil);
                System.out.println(isplata);
                em.persist(isplata);
                
                
                em.flush();
                utx.commit();
                
                System.out.println("Uspesna isplata sa racuna " + trans.getIdRacun());
                
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
     
    
    
    public void getAllRacunZaKomitenta(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(racuniZaKomitentaZahtev);
        JMSProducer producer=context.createProducer();

        consumer.setMessageListener((message) -> {
            try {
                TextMessage txtMsg=(TextMessage)message;
                int idK=txtMsg.getIntProperty("komitent");
                String poruka=(String)txtMsg.getText();
                System.out.println("Stigla");
                if(poruka.equals("getall")){
                    System.out.println("Uspesno prenesena poruka za dohvatanje transakcija do podsistema!");
                    
                    Komitent komitent=em.find(Komitent.class, idK);
                    List<Racun> lista=new LinkedList<Racun>();
                    lista=komitent.getRacunList();

                    for(int i=0;i<lista.size();i++){
                        Racun rac=lista.get(i);
                        ObjectMessage objMsg1=context.createObjectMessage();
                        objMsg1.setObject(rac);
                        producer.send(racuniZaKomitenta, objMsg1);
                    }
                    TextMessage objMsg=context.createTextMessage();
                    objMsg.setText("kraj");
                    producer.send(racuniZaKomitenta, objMsg);


                    System.out.println("Uspesno prenete sve transakcije za korisnika" + komitent.getNaziv());
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
    
    
    public void getAllTransakcijeZaRacun(){
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(transakcijeZaRacunZahtev);
        JMSProducer producer=context.createProducer();

        consumer.setMessageListener((message) -> {
            try {
                TextMessage txtMsg=(TextMessage)message;
                int idR=txtMsg.getIntProperty("racun");
                String poruka=(String)txtMsg.getText();
                System.out.println("Stigla");
                if(poruka.equals("getall")){
                    System.out.println("Uspesno prenesena poruka za dohvatanje transakcija do podsistema!");
                    
                    Racun racun=em.find(Racun.class, idR);
                    List<Transakcija> lista=new LinkedList<Transakcija>();
                    lista=racun.getTransakcijaList();

                    for(int i=0;i<lista.size();i++){
                        Transakcija rac=lista.get(i);
                        ObjectMessage objMsg1=context.createObjectMessage();
                        objMsg1.setObject(rac);
                        producer.send(transakcijeZaRacun, objMsg1);
                    }
                    TextMessage objMsg=context.createTextMessage();
                    objMsg.setText("kraj");
                    producer.send(transakcijeZaRacun, objMsg);

                    System.out.println("Uspesno prenete sve transakcije za racun" + racun.toString());
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
        m.otvoriRacun();
        m.zatvoriRacun();
        m.uplataNaRacun();
        m.isplataSaRacun();
        m.getAllRacunZaKomitenta();
        m.getAllTransakcijeZaRacun();

        m.createPrenos();
    }
    
}
