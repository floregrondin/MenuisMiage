/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;


import fr.miage.m2.menuismiageshared.Affaire;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *
 * @author Alexis Bournavaud
 */
@Stateless
public class GestionPose implements GestionPoseLocal {

    @Resource(mappedName = "EtatCommande")
    private Queue etatCommande;

    @Resource(mappedName = "MenuisMiage")
    private ConnectionFactory MenuisMiage;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private void sendJMSMessageToEtatCommande(String messageData) throws NamingException, JMSException {
        Connection connection = null;
        Session session = null;

            connection = MenuisMiage.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(etatCommande);
            messageProducer.send(session.createTextMessage(messageData));
            
       // context.createProducer().send(etatCommande, messageData);
    }

    @Override
    public void validerPose(Long idAffaire) {
        try {
            sendJMSMessageToEtatCommande("POSEE");
        } catch (NamingException ex) {
            Logger.getLogger(GestionPose.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(GestionPose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
