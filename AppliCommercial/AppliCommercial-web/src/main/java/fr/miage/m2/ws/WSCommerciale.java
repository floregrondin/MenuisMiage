/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.ws;

import fr.miage.m2.expo.ExpoCommercialeLocal;
import fr.miage.m2.menuismiageshared.Commande;
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
     * 
     * @param idAffaire
     * @param refCatalogue
     * @param cotes
     * @param montant
     * @return 
     */
    @WebMethod(operationName = "creerCommande")
    public Commande creerCommande(@WebParam(name = "idAffaire") Long idAffaire, @WebParam(name = "refCatalogue") Long refCatalogue, @WebParam(name = "cotes") String cotes, @WebParam(name = "montant") double montant) {
        return this.expoCommerciale.creerCommande(idAffaire, refCatalogue, cotes, montant);
        //return "OK";
    }

    
    
}
