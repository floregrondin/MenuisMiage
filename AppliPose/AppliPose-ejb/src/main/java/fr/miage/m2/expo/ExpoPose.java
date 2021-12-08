/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import fr.miage.m2.metier.GestionPoseLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Alexis Bournavaud
 */
@Stateless
public class ExpoPose implements ExpoPoseLocal {

    @EJB
    private GestionPoseLocal gestionPose;

    
    
    @Override
    public void validerPose(Long idAffaire) {
        this.gestionPose.validerPose(idAffaire);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
