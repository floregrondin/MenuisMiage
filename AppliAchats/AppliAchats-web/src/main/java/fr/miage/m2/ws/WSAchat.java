/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.ws;

import fr.miage.m2.expo.ExpoAchatLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Flo
 */
@WebService(serviceName = "ExpoAchat")
public class WSAchat {

    @EJB
    public ExpoAchatLocal expoAchat;
    
    /**
     * 
     * @param idAffaire
     * @return 
     */
    @WebMethod(operationName = "validerCommandePassee")
    public String validerCommandePassee(@WebParam(name = "idAffaire") Long idAffaire) {
        this.expoAchat.validerCommandePassee(idAffaire);
        //GESTION D'ERREUR A FAIRE Si id affaire n'existe pas
        return "OK";
    }
    
    /**
     * 
     * @param idAffaire
     * @return 
     */
    @WebMethod(operationName = "validerReceptionCommande")
    public String validerReceptionCommande(@WebParam(name = "idAffaire") Long idAffaire) {
        this.expoAchat.validerReceptionCommande(idAffaire);
        //GESTION D'ERREUR A FAIRE Si id affaire n'existe pas
        return "OK";
    }
}
