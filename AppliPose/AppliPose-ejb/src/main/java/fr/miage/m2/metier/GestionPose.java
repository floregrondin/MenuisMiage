/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Disponibilite;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.ejb.Stateless;

/**
 *
 * @author Valentin
 */
@Stateless
public class GestionPose implements GestionPoseLocal {

    private ArrayList<Disponibilite> listeDisponibilites= null;
    
    @Override
    public ArrayList<Disponibilite> getListeDisponibilites() {
        if(this.listeDisponibilites == null){
            this.listeDisponibilites = new ArrayList<>();
            this.listeDisponibilites.add(Disponibilite.disponibilitePose(1L, new Timestamp(1639065600000L)));
            this.listeDisponibilites.add(Disponibilite.disponibilitePose(2L, new Timestamp(1639036800000L)));
        }
        return this.listeDisponibilites;
    }
    
    public void setListeDisponibilites(ArrayList<Disponibilite> listeDisponibilites){
        this.listeDisponibilites = listeDisponibilites;
    }
}
