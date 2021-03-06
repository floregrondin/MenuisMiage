/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.menuismiageshared.Disponibilite;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author Flo
 */
@Local
public interface ExpoCommercialeLocal {
    
    public Commande creerCommande(Long idAffaire, Long refCatalogue, String cotes, double montant);
    
    public void setListeDisponibilites(ArrayList<Disponibilite> listeDisponibilites);

    public String getAffaireByIdAffaire(Long idAffaire);

}
