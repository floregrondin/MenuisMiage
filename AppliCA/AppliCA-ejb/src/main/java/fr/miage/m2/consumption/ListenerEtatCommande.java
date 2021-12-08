/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.consumption;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
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
    
    public ListenerEtatCommande() {
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("mon message : " + message);
        if (message instanceof TextMessage) {
                    TextMessage text = (TextMessage) message;
            try {
                System.out.println("Received: " + text.getText());
            } catch (JMSException ex) {
                Logger.getLogger(ListenerInfosProduits.class.getName()).log(Level.SEVERE, null, ex);
            }
                } else if (message != null) {
                    System.out.println("Received non text message");
                }
    }
    
}
