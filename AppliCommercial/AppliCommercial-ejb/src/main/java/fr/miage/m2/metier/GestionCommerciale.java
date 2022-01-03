/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.metier;

import fr.miage.m2.menuismiageshared.Commande;
import fr.miage.m2.menuismiageshared.Disponibilite;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
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
public class GestionCommerciale implements GestionCommercialeLocal {

    @Resource(mappedName = "MenuisMiage")
    private ConnectionFactory menuisMiage;

    @Resource(mappedName = "InfosProduits")
    private Topic infosProduits;

    private ArrayList<Disponibilite> listeDisponibilites = null;

    @Override
    public Commande creerCommande(Long idAffaire, Long refCatalogue, String cotes, double montant) {
        // Création de l'objet cmd qu'on passera au MDB
        Commande cmd = new Commande(refCatalogue, cotes, montant, idAffaire);
        try {
            // Création du MDB + Envoi
            sendJMSMessageToInfosProduits(cmd);
        } catch (JMSException ex) {
            Logger.getLogger(GestionCommerciale.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cmd;
    }

    private Message createJMSMessageForinfosProduits(Session session, Commande cmd) throws JMSException {
        ObjectMessage tm = session.createObjectMessage();
        // Envoi de l'objet commande
        tm.setObject(cmd);
        return tm;
    }

    private void sendJMSMessageToInfosProduits(Commande cmd) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = menuisMiage.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(infosProduits);
            messageProducer.send(createJMSMessageForinfosProduits(session, cmd));
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
    public ArrayList<Disponibilite> getListeDisponibilites() {
        // Création d'une liste de dispo par défaut
        if (this.listeDisponibilites == null) {
            this.listeDisponibilites = new ArrayList<>();
            this.listeDisponibilites.add(Disponibilite.disponibiliteCommercial(1L, new Timestamp(1639065600000L)));
            this.listeDisponibilites.add(Disponibilite.disponibiliteCommercial(2L, new Timestamp(1639036800000L)));
            return this.listeDisponibilites;
        } else {
            ArrayList<Disponibilite> listeDispos = new ArrayList<>();
            for (Disponibilite d : this.listeDisponibilites) {
                if (d.isEstDispo() == true) {
                    listeDispos.add(d);
                }
            }
            return listeDispos;
        }
    }

    @Override
    public void setListeDisponibilites(ArrayList<Disponibilite> listeDisponibilites) {
        this.listeDisponibilites = listeDisponibilites;
    }
    
    @Override
    public void updateDisponibilite (Long idDispo){
        System.out.println("ID Param requete : "+idDispo);
        if (this.listeDisponibilites != null){
            System.out.println("Liste non null");
            for(Disponibilite d : this.listeDisponibilites){
                System.out.println("ID Dispo liste : "+d.getIdDisponibilite());
                if (d.getIdDisponibilite().equals(idDispo)){
                    
                    d.setEstDispo(false);
                }
            }
        } else {
            //EXCEPTION LISTE INEXSITANTE
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
        return json;
    }

}
