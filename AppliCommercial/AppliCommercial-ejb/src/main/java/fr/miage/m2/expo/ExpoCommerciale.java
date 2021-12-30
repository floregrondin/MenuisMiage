/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.menuismiageshared.Disponibilite;
import fr.miage.m2.metier.GestionCommercialeLocal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Flo
 */
@Stateless
public class ExpoCommerciale implements ExpoCommercialeLocal {

    @EJB
    private GestionCommercialeLocal gestionCommerciale;

    @Override
    public Commande creerCommande(Long idAffaire, Long refCatalogue, String cotes, double montant) {
        return this.gestionCommerciale.creerCommande(idAffaire, refCatalogue, cotes, montant);
    }

    @Override
    public void setListeDisponibilites(ArrayList<Disponibilite> listeDisponibilites) {
        this.gestionCommerciale.setListeDisponibilites(listeDisponibilites);    
    }

    
    
}
