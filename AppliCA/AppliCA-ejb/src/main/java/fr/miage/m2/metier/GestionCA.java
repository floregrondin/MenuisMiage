/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Affaire;
import javax.ejb.Stateless;

/**
 *
 * @author Flo
 */
@Stateless
public class GestionCA implements GestionCALocal {

    @Override
    public Long creerAffaire(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient) {
        Affaire newAffaire = new Affaire(prenomClient, nomClient, adresseClient, mailClient, telClient);
        return newAffaire.getIdAffaire();
    }

}
