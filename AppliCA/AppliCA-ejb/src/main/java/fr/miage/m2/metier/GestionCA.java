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
     * Permet de récupérer toutes les disponibilités des commerciaux
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
     * Permet de récupérer toutes les disponibilités des poseurs
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
     * Permet de récupérer une affaire selon son id
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
     * @param idAffaire Id de l'affaire
     * @param etatAffaire Nouvel etat de l'affaire
     */
    @Override
    public void updateEtatAffaireByIdAffaire(Long idAffaire, String etatAffaire) {
        try {
            Affaire a = getAffaireByIdAffaire(idAffaire);
            
            System.out.println("Récupération de l'affaire : " + a);
            //TODO: Gérer directement dans l'expo WS/REST si id fourni n'existe pas en bd (dans notre liste)
            if (a == null) {
                try {
                    throw new Exception("ERREUR : Affaire inexistante");
                } catch (Exception ex) {
                    Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // Mise à jour de l'état de l'affaire avec l'enum
            // TODO: Envoyer enum EtatAffaire directement pendant la production du msg JMS
            if (etatAffaire.equals(EtatAffaire.COMMANDEE.toString())) {
                a.setEtatAffaire(EtatAffaire.COMMANDEE);
            } else if (etatAffaire.equals(EtatAffaire.RECEPTIONNEE.toString())) {
                a.setEtatAffaire(EtatAffaire.RECEPTIONNEE);
            } else if (etatAffaire.equals(EtatAffaire.POSEE.toString())) {
                a.setEtatAffaire(EtatAffaire.POSEE);
            }else if(etatAffaire.equals(EtatAffaire.CLOTUREE.toString())){
                a.setEtatAffaire(EtatAffaire.CLOTUREE);
            }
        } catch (Exception ex) {
            Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            System.out.println("mon affaire : " + getAffaire(idAffaire));
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
                throw new Exception("ERREUR : AFFAIRE INEXISTANTE.");  
            }
        } catch (Exception ex) {
            try {  
                throw new Exception("ERREUR : AFFAIRE INEXISTANTE.");
            } catch (Exception ex1) {
                Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void setEtatDispoCommerciaux(String idAffaire, String idDispo) {
        // Parcourir les dispo commerciaux
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> mapDispoC;
        Disponibilite dispoRecherchee = new Disponibilite();
        
        try {
            mapDispoC = mapper.readValue(getAllDispoCommerciaux(), new TypeReference<List<Map<String, String>>>() {});
            // Pour chaque disponibilité
            for (Map<String, String> liste : mapDispoC){
                // Si elle contient l'attribut recherché
                if (liste.get("idDisponibilite").equals(idDispo) && "true".equals(liste.get("estDispo"))){
                    // Récupérer la disponibilité
                    dispoRecherchee.setIdDisponibilite(Long.valueOf(liste.get("idDisponibilite")));
                    dispoRecherchee.setIdCommercial(Long.valueOf(liste.get("idCommercial")));
                    dispoRecherchee.setEstDispo(false);
                    dispoRecherchee.setIdAffaire(Long.valueOf(idAffaire));
                    // Formattage de la date String depuis Json vers Timestamp
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US);
                    Date parsedDate = dateFormat.parse(liste.get("dateRdv"));
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    dispoRecherchee.setDateRdv(timestamp);  
                    // MAJ l'état de la dispo dans la liste
                    liste.put("estDispo", "false");
                    updateDispoCommercial(liste.get("idDisponibilite"));
                }
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
        // Parcourir les affaires
        for (Affaire a : getAllAffaires().values()){
            // Récupérer l'affaire dont l'id a été fourni par l'utilisateur
            if (idAffaire.equals(a.getIdAffaire().toString())){;
                getAffaire(Long.valueOf(idAffaire)).setRdvCommercial(dispoRecherchee);
            }
        }
    }
    
    @Override
    public void updateDispoCommercial(String idDispo){
        Client client = ClientBuilder.newClient();
        //client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        WebTarget wt = client.target("http://localhost:8080/AppliCommercial-web/webresources/ExpoDispoCommerciaux/"+idDispo);
        Response response = wt
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(""));
    }

    @Override
    public void updateDispoPoseur(String idDispo){
        Client client = ClientBuilder.newClient();
        //client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        WebTarget wt = client.target("http://localhost:8080/AppliPose-web/webresources/ExpoDispoPoseurs/"+idDispo);
        Response response = wt
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(""));
    }

    @Override
    public void setEtatDispoPoseurs(String idAffaire, String idDispo){
        // Parcourir les dispo poseurs
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> mapDispoP;
        Disponibilite dispoRecherchee = new Disponibilite();
        
        try {
            mapDispoP = mapper.readValue(getAllDispoPoseurs(), new TypeReference<List<Map<String, String>>>() {});
            // Pour chaque disponibilité
            for (Map<String, String> liste : mapDispoP){
                // Si elle contient l'attribut recherché
                if (liste.get("idDisponibilite").equals(idDispo) && "true".equals(liste.get("estDispo"))){
                    // Récupérer la disponibilité
                    dispoRecherchee.setIdDisponibilite(Long.valueOf(liste.get("idDisponibilite")));
                    dispoRecherchee.setIdEquipePose(Long.valueOf(liste.get("idEquipePose")));
                    dispoRecherchee.setEstDispo(false);
                    dispoRecherchee.setIdAffaire(Long.valueOf(idAffaire));
                    // Formattage de la date String depuis Json vers Timestamp
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US);
                    Date parsedDate = dateFormat.parse(liste.get("dateRdv"));
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    dispoRecherchee.setDateRdv(timestamp);  
                    // MAJ l'état de la dispo dans la liste
                    liste.put("estDispo", "false");
                    // Récupérer l'index de la dispo pour supprimer dans l'appli pose
                    updateDispoPoseur(liste.get("idDisponibilite"));
                }
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(GestionCA.class.getName()).log(Level.SEVERE, null, ex);
        }
          
        // Parcourir les affaires
        for (Affaire a : getAllAffaires().values()){
            // Récupérer l'affaire dont l'id a été fourni par l'utilisateur
            if (idAffaire.equals(a.getIdAffaire().toString())){
                System.out.println("POSE : mon affaire : " + getAffaire(Long.valueOf(idAffaire)));
                getAffaire(Long.valueOf(idAffaire)).setRdvPose(dispoRecherchee);
            }
        } 
    }
    
    /**
     * Permet de récupérer une affaire grâce à l'id commande 
     * Utilisée dans la première version lorsqu'on mettait à jour etatAffaire selon l'id commande (cf vidéo démo)
     * @param idCmd L'id de la commande
     * @return objet de type affaire contenant la commande dans sa liste de commandes associée
     * @throws Exception 
     */
    @Deprecated
    @Override
    public Affaire getAffaireByIdCommande(Long idCmd) throws Exception{
        if (getAllAffaires().isEmpty()){
            System.out.println("LISTE VIDE");
            throw new Exception("ERREUR : COMMANDE INEXISTANTE.");
        }
        for (Affaire aff : getAllAffaires().values()){
            for (Commande cmd : aff.getListeCommandes()){
                if(cmd.getIdCommande().toString().equals(idCmd.toString())){
                    System.out.println("CMD EXISTANTE");
                    return aff;
                } 
            }
        }
        System.out.println("PAS DE CMD");
        throw new Exception("ERREUR : COMMANDE INEXISTANTE.");
    }
    
    /**
     * Permet de récupérer l'affaire grâce à son id
     * @param idAffaire
     * @return Affaire
     * @throws Exception Si affaire inexistante 
     */
    @Override
    public Affaire getAffaireByIdAffaire(Long idAffaire) throws Exception{
        // Check si liste d'affaires vide
        if(getAllAffaires().isEmpty()){
            throw new Exception("ERREUR : AFFAIRE INEXISTANTE");
        }
        
        // Sinon pour chaque affaire répertoriée dans la liste des affaires
        for (Affaire aff : getAllAffaires().values()){
            // Si l'id d'une affaire correspond à celui en param
            if (aff.getIdAffaire().toString().equals(idAffaire.toString())){
                return aff;
            }
        }
        // Si pas de match alors erreur
        throw new Exception("ERREUR : AFFAIRE INEXISTANTE");
    }
}
