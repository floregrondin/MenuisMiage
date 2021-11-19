/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.menuismiageshared;

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
    private Long refCommande;
    private String cotesCommande;
    private double montantCommande;
    // Id à renvoyer à l'utilisateur lors d'une demande de création
    private Long idDemandeCreation;

    /**
     * Constructeur permet au CA de déclarer un objet de type Affaire
     * @param prenomClient
     * @param nomClient
     * @param adresseClient
     * @param mailClient
     * @param telClient 
     */
    public Affaire(String prenomClient, String nomClient, String adresseClient, String mailClient, String telClient) {
        this.idAffaire = Long.parseLong(UUID.randomUUID().toString());
        this.prenomClient = prenomClient;
        this.nomClient = nomClient;
        this.adresseClient = adresseClient;
        this.mailClient = mailClient;
        this.telClient = telClient;
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
     * Récupérer la référence d'une commande passée par un client
     * @return 
     */
    public Long getRefCommande() {
        return refCommande;
    }

    /**
     * Mettre à jour la référence d'une commande passée par un client
     * @param refCommande 
     */
    public void setRefCommande(Long refCommande) {
        this.refCommande = refCommande;
    }

    /**
     * Récupérer les côtes du produit commandé par un client
     * @return 
     */
    public String getCotesCommande() {
        return cotesCommande;
    }

    /**
     * Mettre à jour les côtes du produit commandé par un client
     * @param cotesCommande 
     */
    public void setCotesCommande(String cotesCommande) {
        this.cotesCommande = cotesCommande;
    }

    /**
     * Récupérer le montant de la commande passée par le client
     * @return 
     */
    public double getMontantCommande() {
        return montantCommande;
    }

    /**
     * Mettre à jour le montant de la commande passée par le client
     * @param montantCommande 
     */
    public void setMontantCommande(double montantCommande) {
        this.montantCommande = montantCommande;
    }

    /**
     * Récupérer l'id de demande de création sur un objet de type Affaire
     * @return 
     */
    public Long getIdDemandeCreation() {
        return idDemandeCreation;
    }

    /**
     * Mettre à jour l'id de demande de création sur un objet de type Affaire
     * @param idDemandeCreation 
     */
    public void setIdDemandeCreation(Long idDemandeCreation) {
        this.idDemandeCreation = idDemandeCreation;
    }
    
}

/**
 * Permet de déclarer de nouveaux états pour une affaire
 * @author Flo
 */
enum EtatAffaire{
    COMMANDEE,
    EN_ATTENTE,
    RECEPTIONNEE,
    POSEE,
    CLOTUREE
}