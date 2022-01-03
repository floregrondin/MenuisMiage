/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import javax.ejb.Local;

/**
 *
 * @author Alexis Bournavaud
 */
@Local
public interface ExpoPoseLocal {
    
    public void validerPose(Long idAffaire);
    
    public String getAffaireByIdAffaire(Long idAffaire);

}
