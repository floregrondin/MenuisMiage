/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Commande;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Flo
 */
@Stateless
public class GestionAchat implements GestionAchatLocal {

    @Resource(mappedName = "EtatCommande")
    private Queue etatCommande;

    @Resource(mappedName = "MenuisMiage")
    private ConnectionFactory menuisMiage;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void validerCommandePassee(Long idAffaire) {
        Map<Long, String> majEtatAffaire = new HashMap<>();
        majEtatAffaire.put(idAffaire, "COMMANDEE");
        try {
            sendJMSMessageToEtatCommande(majEtatAffaire);
        } catch (JMSException ex) {
            Logger.getLogger(GestionAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Message createJMSMessageForEtatCommande(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

    private void sendJMSMessageToEtatCommande(Object messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = menuisMiage.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(etatCommande);
            messageProducer.send(createJMSMessageForEtatCommande(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void validerReceptionCommande(Long idAffaire) {
        Map<Long, String> majEtatAffaire = new HashMap<>();
        majEtatAffaire.put(idAffaire, "RECEPTIONNEE");
        try {
            sendJMSMessageToEtatCommande(majEtatAffaire);
        } catch (JMSException ex) {
            Logger.getLogger(GestionAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void passerCommande(Commande cmd) {
        //Hors domaine --> Communication avec fournisseur
        System.out.println("ENVOI DE LA COMMANDE AU FOURNISSEUR");
    }
    
    @Override
    public String getAffaireByIdCommande(Long idCommande) {
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target("http://localhost:8080/AppliCA-web/webresources/ExpoCA/commandes/"+idCommande.toString());
        Response response = wt
                .request(MediaType.APPLICATION_JSON)
                .get();

        String json = response.readEntity(String.class);
        return json;
    }
    
}
