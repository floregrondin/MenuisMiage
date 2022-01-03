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
    private GestionAchatLocal gestionAchat;

    @Override
    public void validerCommandePassee(Long idAffaire) {
        this.gestionAchat.validerCommandePassee(idAffaire);
    }
    @Override
    public void validerReceptionCommande(Long idAffaire) {
        this.gestionAchat.validerReceptionCommande(idAffaire);
    }
    
    @Override
    public String getAffaireByIdCommande(Long idCommande){
        return this.gestionAchat.getAffaireByIdCommande(idCommande);
    }
}
