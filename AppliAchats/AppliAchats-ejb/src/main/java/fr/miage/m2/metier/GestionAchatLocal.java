/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Commande;
import javax.ejb.Local;

/**
 *
 * @author Flo
 */
@Local
public interface GestionAchatLocal {
    
    public void validerCommandePassee(Long idAffaire);

    public void validerReceptionCommande(Long idAffaire);

    public void passerCommande(Commande cmd);

    public String getAffaireByIdCommande(Long idCommande);
}
