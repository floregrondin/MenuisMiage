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
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    
    @Override
    public String getAllDispoCommerciaux(){
        Client client = ClientBuilder.newClient();
        WebTarget wt  = client.target("http://localhost:8080/AppliCommercial-web/webresources/ExpoDispoCommerciaux");
        //Invocation.Builder builder = wt.request();
        Response response = wt
        .request(MediaType.APPLICATION_JSON)
        .get();

    String json = response.readEntity(String.class);
    //json = json.replace("\\", "");
    //json = json.replace("\"", "");
    //json = json.replace("[", "");
    //json = json.replace("]", "");
    return json;
    //return String.format("Liste des disponibilités : %s", json);
    
    }
    
    @Override
    public String getAllDispoPoseurs(){
        return null;
    }

}
