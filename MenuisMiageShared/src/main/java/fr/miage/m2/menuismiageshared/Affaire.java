/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.menuismiageshared;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Flo
 * Classe qui permet de gérer un objet de type Affaire lorsqu'on souhaite MAJ une affaire créée par un CA
 */
public class Affaire {
    private Long idAffaire;
    private EtatAffaire etatAffaire;
    private String prenomClient;
    private String nomClient;
    private String adresseClient;
    private String mailClient;
    private String telClient;
    private String geolocalisationClient;
    private Disponibilite rdvCommercial;
    private Disponibilite rdvPose;
    private List<Commande> listeCommandes;

    /**
     * Constructeur permet au CA de déclarer un objet de type Affaire
     * @param prenomClient
     * @param nomClient
     * @param adresseClient
     * @param mailClient
     * @param telClient 
     * @param geolocalisationClient
     */
    public Affaire(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient, String geolocalisationClient) {
        this.idAffaire = -1L;
        do {
            this.idAffaire = UUID.randomUUID().getMostSignificantBits();
        } while (idAffaire < 0);
        this.prenomClient = prenomClient;
        this.nomClient = nomClient;
        this.adresseClient = adresseClient;
        this.mailClient = mailClient;
        this.telClient = telClient;
        this.geolocalisationClient = geolocalisationClient;

    }

    /**
     * Récupérer l'id d'une affaire
     * @return l'id d'une affaire
     */
    public Long getIdAffaire() {
        return idAffaire;
    }

    /**
     * Mettre à jour l'id d'une affaire
     * @param idAffaire 
     */
    public void setIdAffaire(Long idAffaire) {
        this.idAffaire = idAffaire;
    }

    /**
     * Récupérer l'état d'une affaire
     * @return enum de type EtatAffaire
     */
    public EtatAffaire getEtatAffaire() {
        return etatAffaire;
    }

    /**
     * Mettre à jour l'état d'une affaire
     * @param etatAffaire enum de type EtatAffaire
     */
    public void setEtatAffaire(EtatAffaire etatAffaire) {
        this.etatAffaire = etatAffaire;
    }

    /**
     * Récupérer le prénom du client
     * @return 
     */
    public String getPrenomClient() {
        return prenomClient;
    }

    /**
     * Mettre à jour le prénom d'un client
     * @param prenomClient 
     */
    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    /**
     * Récupérer le nom d'un client
     * @return 
     */
    public String getNomClient() {
        return nomClient;
    }

    /**
     * Mettre à jour le nom d'un client
     * @param nomClient 
     */
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    /**
     * Récupérer l'adresse d'un client
     * @return 
     */
    public String getAdresseClient() {
        return adresseClient;
    }

    /**
     * Mettre à jour l'adresse d'un client
     * @param adresseClient 
     */
    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    /**
     * Récupérer le mail d'un client
     * @return 
     */
    public String getMailClient() {
        return mailClient;
    }

    /**
     * Mettre à jour le mail d'un client
     * @param mailClient 
     */
    public void setMailClient(String mailClient) {
        this.mailClient = mailClient;
    }

    /**
     * Récupérer le numéro de téléphone d'un client
     * @return 
     */
    public String getTelClient() {
        return telClient;
    }

    /**
     * Mettre à jour le numéro de téléphone d'un client
     * @param telClient 
     */
    public void setTelClient(String telClient) {
        this.telClient = telClient;
    }

    /**
     * Mettre à jour la géolocalisation d'un client
     * @return Adresse pour les futurs RDV
     */
    public String getGeolocalisationClient() {
        return geolocalisationClient;
    }

    /**
     * Récupérer la géolocalisation d'un client 
     * @param geolocalisationClient Adresse pour les futurs RDV
     */
    public void setGeolocalisationClient(String geolocalisationClient) {
        this.geolocalisationClient = geolocalisationClient;
    }

    /**
     * Récupérer le RDV commercial fixé avec un client
     * @return objet de type Disponibilite
     */
    public Disponibilite getRdvCommercial() {
        return rdvCommercial;
    }

    /**
     * Mettre à jour le RDV commercial fixé avec un client
     * @param rdvCommercial Objet de type Disponibilite
     */
    public void setRdvCommercial(Disponibilite rdvCommercial) {
        this.rdvCommercial = rdvCommercial;
    }

    /**
     * Récupérer le RDV d'une équipe de pose fixé avec un client
     * @return Objet de type Disponibilite
     */
    public Disponibilite getRdvPose() {
        return rdvPose;
    }

    /**
     * Mettre à jour le RDV d'une équipe de pose fixé avec un client
     * @param rdvPose Objet de type Disponibilite
     */
    public void setRdvPose(Disponibilite rdvPose) {
        this.rdvPose = rdvPose;
    }

    /**
     * Récupérer la liste des commandes d'un client
     * @return 
     */
    public List<Commande> getListeCommandes() {
        return listeCommandes;
    }

    /**
     * Mettre à jour la liste des commandes d'un client
     * @param listeCommandes 
     */
    public void setListeCommandes(List<Commande> listeCommandes) {
        this.listeCommandes = listeCommandes;
    }

}

/**
 * Permet de déclarer de nouveaux états pour une affaire
 */
enum EtatAffaire{
    COMMANDEE,
    EN_ATTENTE,
    RECEPTIONNEE,
    POSEE,
    CLOTUREE
}