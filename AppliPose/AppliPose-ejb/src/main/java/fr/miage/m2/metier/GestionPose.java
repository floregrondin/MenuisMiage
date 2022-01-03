/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Disponibilite;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import javax.naming.NamingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Valentin
 */
@Stateless
public class GestionPose implements GestionPoseLocal {

    @Resource(mappedName = "EtatCommande")
    private Queue etatCommande;

    @Resource(mappedName = "MenuisMiage")
    private ConnectionFactory menuisMiage;

    private ArrayList<Disponibilite> listeDisponibilites= null;

    
    /**
     * Envoi JMS pour mettre à jour l'état de l'affaire
     * @param idAffaire
     */
    private void sendJMSMessageToEtatCommande(Object messageData) throws NamingException, JMSException {
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

    /**
     * Création de la map à envoyer en ObjectMessage pour mettre à jour l'état de l'affaire
     * @param idAffaire
     */
    @Override
    public void validerPose(Long idAffaire) {
        Map<Long, String> majEtatAffaire = new HashMap<>();
        majEtatAffaire.put(idAffaire, "POSEE");
        try {
            sendJMSMessageToEtatCommande(majEtatAffaire);
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(GestionPose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Message createJMSMessageForEtatCommande(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }
       
    @Override
    public ArrayList<Disponibilite> getListeDisponibilites() {
        if(this.listeDisponibilites == null){
            this.listeDisponibilites = new ArrayList<>();
            this.listeDisponibilites.add(Disponibilite.disponibilitePose(1L, new Timestamp(1639065600000L)));
            this.listeDisponibilites.add(Disponibilite.disponibilitePose(2L, new Timestamp(1639036800000L)));
        return this.listeDisponibilites;
        }else {
            ArrayList<Disponibilite> listeDispos = new ArrayList<>();
            for (Disponibilite d : this.listeDisponibilites) {
                if (d.isEstDispo() == true) {
                    listeDispos.add(d);
                }
            }
            return listeDispos;
        }
    }
    
    public void setListeDisponibilites(ArrayList<Disponibilite> listeDisponibilites){
        this.listeDisponibilites = listeDisponibilites;
    }
    
    @Override
    public void updateDisponibilite (Long idDispo){
        if (this.listeDisponibilites != null){
            if (this.listeDisponibilites.isEmpty()){
                try {
                    throw new Exception("ERREUR : PAS DE DISPONIBILITES.");
                } catch (Exception ex) {
                    Logger.getLogger(GestionPose.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for(Disponibilite d : this.listeDisponibilites){
                if (d.getIdDisponibilite().equals(idDispo)){
                    d.setEstDispo(false);
                }
            }
        }
    }

    @Override
    public String getAffaireByIdAffaire(Long idAffaire) {
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target("http://localhost:8080/AppliCA-web/webresources/ExpoCA/affaires/"+idAffaire.toString());
        Response response = wt
                .request(MediaType.APPLICATION_JSON)
                .get();

        String json = response.readEntity(String.class);
        return json;    }
    
}
