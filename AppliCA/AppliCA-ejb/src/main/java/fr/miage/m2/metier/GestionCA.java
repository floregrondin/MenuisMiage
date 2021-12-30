/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.m2.menuismiageshared.Affaire;
import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.menuismiageshared.Disponibilite;
import fr.miage.m2.menuismiageshared.EtatAffaire;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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
        System.out.println("idAffaire : " + idAffaire);
        System.out.println("etatAffaire : " + etatAffaire);

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
        return "OK. Affaire cloturée.";
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

    /**
     * Permet de rajouter un rdv commercial à une affaire et de retirer le rdv commercial à la liste des disponibilités des commerciaux
     * @param idCommande L'id de la commande associée à une affaire
     * @param idDispo L'id de l'objet disponibiltié pour un rdv commercial
     */
    @Override
    public void setEtatDispoCommerciaux(String idCommande, String idDispo) {
        System.out.println("dispo commerciaux : " + getAllDispoCommerciaux());
                
        // Parcourir les dispo commerciaux
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> mapDispoC;
        Disponibilite dispoRecherchee = new Disponibilite();
        int i = 0;
        int indexListe = 0;
        try {
            mapDispoC = mapper.readValue(getAllDispoCommerciaux(), new TypeReference<List<Map<String, String>>>() {});
            // Pour chaque disponibilité
            for (Map<String, String> liste : mapDispoC){
                i++;
                System.out.println("dispo dans la liste : " + liste.get("idDisponibilite"));
                // Si elle contient l'attribut recherché
                if (liste.get("idDisponibilite").equals(idDispo) && "true".equals(liste.get("estDispo"))){
                    // Récupérer la disponibilité
                    System.out.println("je rentre : " + liste);
                    dispoRecherchee.setIdDisponibilite(Long.valueOf(liste.get("idDisponibilite")));
                    dispoRecherchee.setIdCommercial(Long.valueOf(liste.get("idCommercial")));
                    dispoRecherchee.setEstDispo(false);
                    dispoRecherchee.setIdCommande(Long.valueOf(idCommande));
                    // Formattage de la date String depuis Json -> Timestamp
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US);
                    Date parsedDate = dateFormat.parse(liste.get("dateRdv"));
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    dispoRecherchee.setDateRdv(timestamp);  
                    // MAJ l'état de la dispo dans la liste
                    liste.put("estDispo", "false");
                    // Récupérer l'index de la map pour supprimer plus tard de la liste des dispo
                    indexListe = i-1;
                }
            }
            
            // Retirer le créneau de dispo de la liste des dispo commerciaux
            System.out.println("liste AVANT : " + mapDispoC.toString());
            mapDispoC.remove(indexListe);
            
            ArrayList<Disponibilite> listeMaj = new ArrayList<>();
            for (Map<String, String> liste : mapDispoC){
                if (!liste.get("idDisponibilite").equals(dispoRecherchee.getIdDisponibilite().toString())){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US);
                    Date parsedDate = dateFormat.parse(liste.get("dateRdv"));
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    Disponibilite d = Disponibilite.disponibiliteCommercialMAJ(Long.valueOf(liste.get("idDisponibilite")), Long.valueOf(liste.get(("idCommercial"))), timestamp);
                    listeMaj.add(d);
                }
            }
            System.out.println("liste MAJ : " + mapDispoC.toString());
            //TODO : maj la liste des dispo
            setDispoCommerciaux(listeMaj);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
        System.out.println("dispo recherchee :" + dispoRecherchee.toString());
                
        // Parcourir les affaires
        for (Map.Entry<Long, Affaire> a : getAllAffaires().entrySet()){
            //Parcourir la liste des commandes pour chaque affaire
            for (Commande cmd : a.getValue().getListeCommandes()){
                System.out.println("commandes dans la liste : " + cmd.toString());
                // Récupérer l'affaire qui a cet id de commande dans la liste de commandes
                if (cmd.getIdCommande().toString().equals(idCommande)){
                    // Mettre à jour l'affaire et set le rdv commercial
                    getAffaire(cmd.getIdAffaire()).setRdvCommercial(dispoRecherchee);
                    System.out.println("dispo recherchee :" + dispoRecherchee.toString());
                    System.out.println("affaire maj : " + getAffaire(cmd.getIdAffaire()).getRdvCommercial().toString());
                }
            }
        }
    }

    @Override
    public void setDispoCommerciaux(ArrayList<Disponibilite> listeDispo) {
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target("http://localhost:8080/AppliCommercial-web/webresources/ExpoDispoCommerciaux");
        wt.request().accept(MediaType.APPLICATION_JSON).put(Entity.json(listeDispo));
    }

}
