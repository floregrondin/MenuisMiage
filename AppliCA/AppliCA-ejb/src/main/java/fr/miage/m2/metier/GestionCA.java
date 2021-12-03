/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Affaire;
import java.util.HashMap;
import javax.ejb.Stateless;

/**
 *
 * @author Flo
 */
@Stateless
public class GestionCA implements GestionCALocal {

    public HashMap<Long, Affaire> listeAffaires;
    
    public GestionCA(){
        listeAffaires = new HashMap<>();
    }
    
    @Override
    public Long creerAffaire(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient, String geolocalisationClient) {
        Affaire newAffaire = new Affaire(prenomClient, nomClient, adresseClient, mailClient, telClient, geolocalisationClient);
        listeAffaires.put(newAffaire.getIdAffaire(), newAffaire);
        System.out.println(listeAffaires);
        return newAffaire.getIdAffaire();
    }

    @Override
    public HashMap<Long, Affaire> getAllAffaires() {
        System.out.println(listeAffaires);
        return this.listeAffaires;
    }

}
