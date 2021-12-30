/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.menuismiageshared;

import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author Flo
 * Classe qui permet de gérer un objet de type Disponibilité lorsqu'on souhaite MAJ un RDV commercial ou avec une équipe de pose
 */
public class Disponibilite {
    private Long idDisponibilite;
    private Long idCommande;
    private Long idCommercial;
    private Long idEquipePose;
    private boolean estDispo;
    private Timestamp dateRdv;

    /**
     * Constructeur vide pour un objet de type Disponibilite
     */
    public Disponibilite() {
    }
    
    /**
     * Constructeur pour déclarer la disponibilité d'un commercial
     * @param idCommercial
     * @param dateRdv
     * @return La disponibilité créée
     */
    public static Disponibilite disponibiliteCommercial(Long idCommercial, Timestamp dateRdv) {
        Disponibilite d = new Disponibilite();
        // Génération d'un id unique
        Long idDisponibilite = -1L;
        do {
            idDisponibilite = UUID.randomUUID().getMostSignificantBits();
        } while (idDisponibilite < 0);
        d.setIdDisponibilite(idDisponibilite);
        d.setIdCommercial(idCommercial);
        d.setDateRdv(dateRdv);
        d.setEstDispo(true);
        return d;
    }
    
    public static Disponibilite disponibiliteCommercialMAJ(Long idDisponibilite, Long idCommercial, Timestamp dateRdv) {
        Disponibilite d = new Disponibilite();
        d.setIdDisponibilite(idDisponibilite);
        d.setIdCommercial(idCommercial);
        d.setDateRdv(dateRdv);
        d.setEstDispo(true);
        return d;
    }

    /**
     * Constructeur pour déclarer la disponibilité d'une équipe de pose
     * @param idEquipePose
     * @param dateRdv 
     * @return La disponibilité créée
     */
    public static Disponibilite disponibilitePose(Long idEquipePose, Timestamp dateRdv) {
        Disponibilite d = new Disponibilite();
        // Génération d'un id unique
        Long idDisponibilite = -1L;
        do {
            idDisponibilite = UUID.randomUUID().getMostSignificantBits();
        } while (idDisponibilite < 0);
        d.setIdDisponibilite(idDisponibilite);
        d.setIdEquipePose(idEquipePose);
        d.setDateRdv(dateRdv);
        d.setEstDispo(true);
        return d;
    }

    /**
     * Récupérer l'id d'une disponibilité
     * @return 
     */
    public Long getIdDisponibilite() {
        return idDisponibilite;
    }

    /**
     * Mettre à jour l'id d'une disponibilité
     * @param idDisponibilite 
     */
    public void setIdDisponibilite(Long idDisponibilite) {
        this.idDisponibilite = idDisponibilite;
    }

    /**
     * Récupérer l'id d'un commercial
     * @return 
     */
    public Long getIdCommercial() {
        return idCommercial;
    }

    /**
     * Mettre à jour l'id d'un commercial
     * @param idCommercial 
     */
    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
    }

    /**
     * Récupérer l'id d'une équipe de pose
     * @return 
     */
    public Long getIdEquipePose() {
        return idEquipePose;
    }

    /**
     * Mettre à jour l'id d'une équipe de pose
     * @param idEquipePose 
     */
    public void setIdEquipePose(Long idEquipePose) {
        this.idEquipePose = idEquipePose;
    }

    /**
     * Retoune si la disponibilité est encore valide
     * @return 
     * Si true, la disponibilité n'a pas encore été prise par un CA
     * Si false, la disponibilité n'est plus valide (prise par un CA)
     */
    public boolean isEstDispo() {
        return estDispo;
    }

    /**
     * Met à jour la validité de la disponibilité
     * @param estDispo 
     */
    public void setEstDispo(boolean estDispo) {
        this.estDispo = estDispo;
    }

    /**
     * Récupérer la date d'un RDV
     * @return 
     */
    public Timestamp getDateRdv() {
        return dateRdv;
    }

    /**
     * Met à jour la date d'un RDV
     * @param dateRdv 
     */
    public void setDateRdv(Timestamp dateRdv) {
        this.dateRdv = dateRdv;
    }

    public Long getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(Long idCommande) {
        this.idCommande = idCommande;
    }

    @Override
    public String toString() {
        return "Disponibilite{" + "idDisponibilite=" + idDisponibilite + ", idCommande=" + idCommande + ", idCommercial=" + idCommercial + ", idEquipePose=" + idEquipePose + ", estDispo=" + estDispo + ", dateRdv=" + dateRdv + '}';
    }
    
    
}
