/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.consumption;

import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.metier.GestionAchatLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author Alexis Bournavaud
 */
@MessageDriven(activationConfig = {
//    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "InfosProduits")
//    ,
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "InfosProduits")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "InfosProduits")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class ListenerInfosProduits implements MessageListener {

    @EJB
    private GestionAchatLocal gestionAchat;
    
    
    
    public ListenerInfosProduits() {
    }
    
    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                // A la réception d'un message = commande
                ObjectMessage om =(ObjectMessage) message;
                Commande cmd = (Commande) om.getObject();
                // Appel au métier pour passer la commande
                // Méthode vide --> syso
                this.gestionAchat.passerCommande(cmd);
            } catch (JMSException ex) {
                Logger.getLogger(ListenerInfosProduits.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
