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
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.NamingException;

/**
 *
 * @author Valentin
 */
@Stateless
public class GestionPose implements GestionPoseLocal {

    @Resource(mappedName = "EtatCommande")
    private Queue etatCommande;

    @Resource(mappedName = "MenuisMiage")
    private ConnectionFactory MenuisMiage;

    
    /**
     * Envoi JMS pour mettre à jour l'état de l'affaire
     * @param idAffaire
     */
    private void sendJMSMessageToEtatCommande(Object messageData) throws NamingException, JMSException {
        Connection connection = null;
        Session session = null;

        connection = MenuisMiage.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(etatCommande);
        messageProducer.send(session.createTextMessage(messageData.toString()));

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
    
    
    private ArrayList<Disponibilite> listeDisponibilites= null;
    
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
    
}
