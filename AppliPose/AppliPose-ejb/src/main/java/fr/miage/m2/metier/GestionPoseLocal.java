/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Disponibilite;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author Valentin
 */
@Local
public interface GestionPoseLocal {
    
    public ArrayList<Disponibilite> getListeDisponibilites();
    
}
