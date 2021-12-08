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
    @WebMethod(operationName = "validerPoseAffaire")
    public String validerPoseAffaire(@WebParam(name = "idAffaire") Long idAffaire) {
        this.expoAchat.validerPoseAffaire(idAffaire);
        //GESTION D'ERREUR A FAIRE Si id affaire n'existe pas
        return "OK";
    }
}
