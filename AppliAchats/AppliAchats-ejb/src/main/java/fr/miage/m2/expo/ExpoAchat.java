/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import fr.miage.m2.metier.GestionAchatLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Flo
 */
@Stateless
public class ExpoAchat implements ExpoAchatLocal {

    @EJB
    public GestionAchatLocal gestionAchat;
    
    @Override
    public void validerPoseAffaire(Long idAffaire) {
        this.gestionAchat.validerPoseAffaire(idAffaire);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
