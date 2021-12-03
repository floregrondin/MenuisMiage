/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.menuismiageshared;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Flo
 */
public class Commande implements Serializable {
    private Long idCommande;
    private Long refCatalogue;
    private String cotes;
    private double montant;
    private Long idAffaire;

    public Commande(Long refCatalogue, String cotes, double montant, Long idAffaire) {
        this.idCommande = -1L;
        do {
            this.idCommande = UUID.randomUUID().getMostSignificantBits();
        } while (idCommande < 0);
        this.refCatalogue = refCatalogue;
        this.cotes = cotes;
        this.montant = montant;
        this.idAffaire = idAffaire;
    }

    /**
     * Récupérer l'id d'une commande
     * @return 
     */
    public Long getIdCommande() {
        return idCommande;
    }

    /**
     * Mettre à jour l'id d'une commande
     * @param idCommande L'id de la commande
     */
    public void setIdCommande(Long idCommande) {
        this.idCommande = idCommande;
    }

    /**
     * Récupérer la référence catalogue d'un produit
     * @return 
     */
    public Long getRefCatalogue() {
        return refCatalogue;
    }

    /**
     * Mettre à jour la référence catalogue d'un produit
     * @param refCatalogue La référence catalogue du produit
     */
    public void setRefCatalogue(Long refCatalogue) {
        this.refCatalogue = refCatalogue;
    }

    /**
     * Récupérer les côtes du produit
     * @return 
     */
    public String getCotes() {
        return cotes;
    }

    /**
     * Mettre à jour les côtes du produit
     * @param cotes 
     */
    public void setCotes(String cotes) {
        this.cotes = cotes;
    }

    /**
     * Récupérer le montant de la commande
     * @return 
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Mettre à jour le montant de la commande
     * @param montant 
     */
    public void setMontant(double montant) {
        this.montant = montant;
    }

    /**
     * Récupérer l'id d'une affaire
     * @return 
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

    @Override
    public String toString() {
        return "Commande{" + "idCommande=" + idCommande + ", refCatalogue=" + refCatalogue + ", cotes=" + cotes + ", montant=" + montant + ", idAffaire=" + idAffaire + '}';
    }
    
}
