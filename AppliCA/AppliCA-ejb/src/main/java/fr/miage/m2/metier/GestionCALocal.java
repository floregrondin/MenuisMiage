/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Affaire;
import fr.miage.m2.menuismiageshared.Disponibilite;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.Local;

/**
 *
 * @author Flo
 */
@Local
public interface GestionCALocal {
    
    public Long creerAffaire(String nomClient, String prenomClient, String adresseClient, String mailClient, String telClient, String geolocalisationClient);

    public HashMap<Long, Affaire> getAllAffaires();
    
    public String getAllDispoCommerciaux();
    
    public void setDispoCommerciaux(ArrayList<Disponibilite> listeDispo);
        
    public String getAllDispoPoseurs();
    
    public Affaire getAffaire(Long idAffaire);
    
    public String cloturerAffaire(Long idAffaire);
    
    public void updateEtatAffaireByIdAffaire(Long idAffaire, String etatAffaire);
    
    public void associerCmdAffaire(Long refCatalogue, String cotes, double montant, Long idAffaire);
 
    public void setEtatDispoCommerciaux(String idCommande, String idDispo);
    
    public void updateDispoCommercial(String idDispo);
    
}
