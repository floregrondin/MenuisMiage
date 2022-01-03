/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Affaire;
import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.menuismiageshared.Disponibilite;
import fr.miage.m2.menuismiageshared.EtatAffaire;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

    public GestionCA() {
        listeAffaires = new HashMap<>();
    }

    /**
     * Permet de créer une affaire
     * @param prenomClient
     * @param nomClient
     * @param adresseClient
     * @param mailClient
     * @param telClient
     * @param geolocalisationClient
     * @return id de l'affaire
     */
    @Override
    public Long creerAffaire(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient, String geolocalisationClient) {
        Affaire newAffaire = new Affaire(prenomClient, nomClient, adresseClient, mailClient, telClient, geolocalisationClient);
        listeAffaires.put(newAffaire.getIdAffaire(), newAffaire);
        return newAffaire.getIdAffaire();
    }

    /**
     * Récupérer toutes les affaires créées
     * @return la liste des affaires
     */
    @Override
    public HashMap<Long, Affaire> getAllAffaires() {
        //System.out.print("PRINT ALL AFFAIRES : " + getAllAffaires());
        return this.listeAffaires;
    }

    /**
     * Récupérer toutes les disponibilités des commerciaux
     * @return la liste des disponibilités des commerciaux en json
     */
    @Override
    public String getAllDispoCommerciaux() {
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target("http://localhost:8080/AppliCommercial-web/webresources/ExpoDispoCommerciaux");
        //Invocation.Builder builder = wt.request();
        Response response = wt
                .request(MediaType.APPLICATION_JSON)
                .get();

        String json = response.readEntity(String.class);
        return json;

    }

    /**
     * Récupérer toutes les disponibilités des poseurs
     * @return la liste des disponibilités des poseurs en json
     */
    @Override
    public String getAllDispoPoseurs() {
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target("http://localhost:8080/AppliPose-web/webresources/ExpoDispoPoseurs");
        Response response = wt
                .request(MediaType.APPLICATION_JSON)
                .get();

        String json = response.readEntity(String.class);
        return json;
    }

    /**
     * Récupérer une affaire selon son id
     * @param idAffaire
     * @return 
     */
    @Override
    public Affaire getAffaire(Long idAffaire) {
        return getAllAffaires().get(idAffaire);
    }

    /**
     * Permet de mettre à jour l'état d'une affaire après réception JMS
     *
     * @param idAffaire Id de l'affaire pour pouvoir faire un get dessus
     * @param etatAffaire Nouvel etat de l'affaire
     */
    @Override
    public void updateEtatAffaireByIdAffaire(Long idAffaire, String etatAffaire) {
        Affaire a = getAffaire(idAffaire);

        System.out.println("Récupération de l'affaire : " + a);
        //TODO: Gérer directement dans l'expo WS/REST si id fourni n'existe pas en bd (dans notre liste)
        if (a == null) {
            //throw new Error("ERREUR : Affaire inexistante");
        }

        // Mise à jour de l'état de l'affaire avec l'enum 
        // TODO: Essayer de faire avant l'envoi dans JMS
        if (etatAffaire.equals(EtatAffaire.COMMANDEE.toString())) {
            a.setEtatAffaire(EtatAffaire.COMMANDEE);
        } else if (etatAffaire.equals(EtatAffaire.RECEPTIONNEE.toString())) {
            a.setEtatAffaire(EtatAffaire.RECEPTIONNEE);
        } else if (etatAffaire.equals(EtatAffaire.POSEE.toString())) {
            a.setEtatAffaire(EtatAffaire.POSEE);
        }else if(etatAffaire.equals(EtatAffaire.CLOTUREE.toString())){
            a.setEtatAffaire(EtatAffaire.CLOTUREE);
        } 
    }

    /**
     * Mettre à jour l'affaire à l'état cloturée
     * @param idAffaire
     * @return OK 
     * On aurait pu appelé une méthode pour encaisser le deuxième chèque ici 
     * si on avait implémenté la gestion compta
     */
    @Override
    public String cloturerAffaire(Long idAffaire) {
        this.updateEtatAffaireByIdAffaire(idAffaire, "CLOTUREE");
        return "OK : Affaire cloturée.";
    }
    
    /*        
     * Permet de créer une commande et de la rattacher à une affaire
     * @param refCatalogue La référence du produit commandé
     * @param cotes Les cotes du produit commandé
     * @param montant Le montant de la commande
     * @param idAffaire L'id de l'affaire
     */
    @Override
    public void associerCmdAffaire(Long refCatalogue, String cotes, double montant, Long idAffaire) {
        // Récupérer l'affaire
        if (getAffaire(idAffaire) != null){
            Affaire affaire = getAffaire(idAffaire);
            Commande cmd = new Commande(refCatalogue, cotes, montant, idAffaire);
            List<Commande> listeCmd = affaire.getListeCommandes();
            // Ajouter la commande à la liste des commandes en "bd"
            if (listeCmd == null){
                // S'il n'y a pas de commande pour une affaire, on créé une liste
                listeCmd = new ArrayList<>();
            }
            // Et on rajoute la commande
            listeCmd.add(cmd);
            // Ajouter la commande à la liste des commandes de l'affaire
            affaire.setListeCommandes(listeCmd);
        } else {
            System.out.println("ERREUR : Affaire introuvable.");
        }
        
    }

    @Override
    public void setDispoCommerciaux(ArrayList<Disponibilite> listeDispo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEtatDispoCommerciaux(String idCommande, String idDispo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDispoCommercial(String idDispo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
