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
     * Permet de valider que la commande a été passée. L'état de la commande passe à "COMMANDEE".
     * @param idCommande
     * @return Msg "OK"
     * @throws java.lang.Exception Si l'id commande ne correspond à aucune commande répertoriée
     */
    @WebMethod(operationName = "validerCommandePassee")
    public String validerCommandePassee(@WebParam(name = "idCommande") Long idCommande) throws Exception {
        // Vérifier l'existence de l'id commande indiqué
        String cmd = this.expoAchat.getAffaireByIdCommande(idCommande);
        System.out.println("res : " + cmd);
        if (cmd == null){
            throw new Exception("ERREUR : Pas d'affaire associée à l'id commande indiqué.");
        }
        this.expoAchat.validerCommandePassee(idCommande);
        return "OK";
    }
    
    /**
     * Permet de valider que la commande a été réceptionnée. L'état de la commande passe à "RECEPTIONNEE".
     * @param idCommande
     * @return Msg "OK"
     * @throws java.lang.Exception Si l'id commande ne correspond à aucune commande répertoriée
     */
    @WebMethod(operationName = "validerReceptionCommande")
    public String validerReceptionCommande(@WebParam(name = "idCommande") Long idCommande) throws Exception {
        String cmd = this.expoAchat.getAffaireByIdCommande(idCommande);
        if (cmd == null){
            throw new Exception("ERREUR : Pas d'affaire associée à l'id commande indiqué.");
        }
        this.expoAchat.validerReceptionCommande(idCommande);
        return "OK";
    }
}
