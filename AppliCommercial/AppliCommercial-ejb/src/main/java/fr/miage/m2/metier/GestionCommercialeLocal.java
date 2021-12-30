/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.menuismiageshared.Disponibilite;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * WSDL : http://desktop-5u8nqcn:8080/AppliCommercial-web/WSCommerciale?wsdl
 * 
 * @author Flo
 */
@Local
public interface GestionCommercialeLocal {
    
    public Commande creerCommande(Long idAffaire, Long refCatalogue, String cotes, double montant);
    
    public ArrayList<Disponibilite> getListeDisponibilites();
    
    public void setListeDisponibilites(ArrayList<Disponibilite> listeDisponibilites);
    
    public void updateDisponibilite (Long idDispo);
}
