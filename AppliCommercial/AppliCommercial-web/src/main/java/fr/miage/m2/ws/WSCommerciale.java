/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.ws;

import fr.miage.m2.expo.ExpoCommercialeLocal;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * Lien WS Flo : http://desktop-5u8nqcn:8080/AppliCommercial-web/WSCommerciale?wsdl
 * @author Flo
 */
@WebService(serviceName = "WSCommerciale")
public class WSCommerciale {

    @EJB
    private ExpoCommercialeLocal expoCommerciale;

    /**
     * Permet de créer une commande rattachée à une affaire
     * @param idAffaire Id de l'affaire devant exister en base
     * @param refCatalogue
     * @param cotes
     * @param montant
     * @return 
     */
    @WebMethod(operationName = "creerCommande")
    public String creerCommande(@WebParam(name = "idAffaire") Long idAffaire, @WebParam(name = "refCatalogue") Long refCatalogue, @WebParam(name = "cotes") String cotes, @WebParam(name = "montant") double montant) throws Exception {
        // Vérifier que l'affaire existe en base
        // Si 404 alors paramètre côté REST est incorrect
        if (this.expoCommerciale.getAffaireByIdAffaire(idAffaire) == null
            || this.expoCommerciale.getAffaireByIdAffaire(idAffaire).contains("404")){
            throw new Exception("ERREUR : AFFAIRE INEXISTANTE.");
        }
        this.expoCommerciale.creerCommande(idAffaire, refCatalogue, cotes, montant);
        return "OK : Commande créée.";

    }

    
    
}
