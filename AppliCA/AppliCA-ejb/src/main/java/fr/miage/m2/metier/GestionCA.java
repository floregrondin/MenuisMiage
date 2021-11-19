/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Affaire;
import java.util.UUID;
import javax.ejb.Stateless;

/**
 *
 * @author Flo
 */
@Stateless
public class GestionCA implements GestionCALocal {

    @Override
    public Long creerAffaire(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient) {
        final Long idDemandeCrea = Long.parseLong(UUID.randomUUID().toString());
        Thread creationAffaire = new Thread(){
            public void run(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient){
                Affaire newAffaire = new Affaire(prenomClient, nomClient, adresseClient, mailClient, telClient);
                newAffaire.setIdDemandeCreation(idDemandeCrea);
            }
        };
        creationAffaire.start();
        creationAffaire.run();
        return idDemandeCrea;
    }

}
