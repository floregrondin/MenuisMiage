/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Affaire;
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
            
    public String getAllDispoPoseurs();
    
    public Affaire getAffaire(Long idAffaire);
    
    public String cloturerAffaire(Long idAffaire);
    
    public void updateEtatAffaireByIdAffaire(Long idAffaire, String etatAffaire);
    
    public void associerCmdAffaire(Long refCatalogue, String cotes, double montant, Long idAffaire);
 
    public void setEtatDispoCommerciaux(String idCommande, String idDispo);
    
    public void setEtatDispoPoseurs(String idCommande, String idDispo);

    public void updateDispoCommercial(String idDispo);
    
    public void updateDispoPoseur(String idDispo);

    public Affaire getAffaireByIdCommande(Long idCmd) throws Exception;

    public Affaire getAffaireByIdAffaire(Long idAffaire) throws Exception;

}
