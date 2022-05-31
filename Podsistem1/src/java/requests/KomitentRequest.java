/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import java.io.Serializable;

/**
 *
 * @author sofij
 */
public class KomitentRequest extends Komitent implements Serializable {

    public KomitentRequest() {
    }

    public KomitentRequest(Integer idK) {
        super(idK);
    }

    public KomitentRequest(Integer idK, String naziv, String adresa) {
        super(idK, naziv, adresa);
    }
  
}
