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
     * @param idAffaire
     * @return Msg "OK"
     * @throws java.lang.Exception Si l'id commande ne correspond à aucune commande répertoriée
     */
    @WebMethod(operationName = "validerCommandePassee")
    public String validerCommandePassee(@WebParam(name = "idAffaire") Long idAffaire) throws Exception {
        // Vérifier l'existence de l'id commande indiqué
        String aff = this.expoAchat.getAffaireByIdAffaire(idAffaire);
        System.out.println("test cmd : " + aff);
        if (aff == null
                || aff.contains("Not Found")){
            throw new Exception("ERREUR : AFFAIRE INEXISTANTE.");
        }
        
        // Vérifier l'état de la commande
        if (aff.contains("CREEE")
                && aff.contains("rdvCommercial")){
            // Maj l'état de l'affaire
            this.expoAchat.validerCommandePassee(idAffaire);
        } else {
            throw new Exception("ERREUR : AFFAIRE NE DOIT PAS ÊTRE VALIDEE");
        }
        
        return "OK";
    }
    
    /**
     * Permet de valider que la commande a été réceptionnée. L'état de la commande passe à "RECEPTIONNEE".
     * @param idAffaire
     * @return Msg "OK"
     * @throws java.lang.Exception Si l'id commande ne correspond à aucune commande répertoriée
     */
    @WebMethod(operationName = "validerReceptionCommande")
    public String validerReceptionCommande(@WebParam(name = "idAffaire") Long idAffaire) throws Exception {
        String aff = this.expoAchat.getAffaireByIdAffaire(idAffaire);
        System.out.println("affaire : " + aff);
        if (aff == null
                || aff.contains("Not Found")){
            throw new Exception("ERREUR : AFFAIRE INEXISTANTE.");
        }
        // Vérifier l'état de la commande
        if (aff.contains("COMMANDEE")
                && aff.contains("rdvCommercial")){
            // Maj l'état de l'affaire
            this.expoAchat.validerReceptionCommande(idAffaire);
        } else {
            throw new Exception("ERREUR : AFFAIRE NE DOIT PAS ÊTRE RECEPTIONNEE");
        }
        return "OK";
    }
}
