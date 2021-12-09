/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import javax.ejb.Local;

/**
 *
 * @author Flo
 */
@Local
public interface ExpoAchatLocal {
    
    public void validerCommandePassee(Long idAffaire);
}
