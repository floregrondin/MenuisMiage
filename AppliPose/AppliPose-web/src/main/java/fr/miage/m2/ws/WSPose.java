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
     * 
     * @param idAffaire
     * @return 
     */
    @WebMethod(operationName = "validerPose")
    public String validerPose(@WebParam(name = "idAffaire") Long idAffaire) {
        this.expoPose.validerPose(idAffaire);
        return "Etat mis à jour";
    }
}
