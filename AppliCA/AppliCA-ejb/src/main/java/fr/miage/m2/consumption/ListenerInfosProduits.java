/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.consumption;

import fr.miage.m2.menuismiageshared.Commande;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author Flo
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "InfosProduits")
    ,
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "InfosProduits")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "InfosProduits")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class ListenerInfosProduits implements MessageListener {
    
    public ListenerInfosProduits() {
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("mon message : " + message);
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage om =(ObjectMessage) message;
                System.out.println("toString " + om.toString());
                Commande cmd = (Commande) om.getObject();
                System.out.println("toujours mon message : " +  cmd.toString());
            } catch (JMSException ex) {
                Logger.getLogger(ListenerInfosProduits.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
