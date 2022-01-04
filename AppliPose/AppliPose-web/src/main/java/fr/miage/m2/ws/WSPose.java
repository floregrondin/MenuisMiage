/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.ws;

import fr.miage.m2.expo.ExpoPoseLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Lien WS AB : http://DESKTOP-KHK3AJE:8080/AppliPose-web/WSPose?wsdl
 * @author Alexis Bournavaud
 */
@WebService(serviceName = "WSPose")
public class WSPose {

    @EJB
    private ExpoPoseLocal expoPose;
    
    
    
    /**
     * Permet de valider la pose pour une affaire
     * @param idAffaire
     * @return 
     */
    @WebMethod(operationName = "validerPose")
    public String validerPose(@WebParam(name = "idAffaire") Long idAffaire) throws Exception {
        String aff = this.expoPose.getAffaireByIdAffaire(idAffaire);
        if (aff == null
                || aff.contains("Not Found")){
            throw new Exception("ERREUR : AFFAIRE INEXISTANTE.");
        }
        if (!aff.contains("\"etatAffaire\":\"RECEPTIONNEE\"")){
            throw new Exception("ERREUR : AFFAIRE NE DOIT PAS ETRE POSEE");
        }
        this.expoPose.validerPose(idAffaire);
        return "OK : Affaire mise à jour.";
    }
}
