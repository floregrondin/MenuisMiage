/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.consumption;


import fr.miage.m2.metier.GestionCALocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author Valentin
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "EtatCommande")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ListenerEtatCommande implements MessageListener {

    @EJB
    private GestionCALocal gestionCA;
    
    public ListenerEtatCommande() {
    }
    
    @Override
    public void onMessage(Message message) {

        try {
            System.out.println("mon message string " + message.getBody(String.class));
            String[] argsMess = message.getBody(String.class).replace("{", "").replace("}","").split("=");
            System.out.println("id affaire : " + argsMess[0]);
            System.out.println("etat affaire : " + argsMess[1]);
            this.gestionCA.updateEtatAffaireByIdAffaire(Long.valueOf(argsMess[0]), argsMess[1]);
        } catch (JMSException ex) {
            Logger.getLogger(ListenerEtatCommande.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}