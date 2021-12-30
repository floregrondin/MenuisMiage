/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.ws;

import com.google.gson.Gson;
import fr.miage.m2.menuismiageshared.Disponibilite;
import fr.miage.m2.metier.GestionCommercialeLocal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Valentin
 */
@Path("ExpoDispoCommerciaux")
@RequestScoped
public class ExpoDispoCommerciauxResource {

    GestionCommercialeLocal gestionCommerciale = lookupGestionCommercialeLocal();
    
    private Gson gson;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ExpoDispoCommerciauxResource
     */
    public ExpoDispoCommerciauxResource() {
        this.gson = new Gson();
    }

    /**
     * Retrieves representation of an instance of fr.miage.m2.ws.ExpoDispoCommerciauxResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getListeDispoCommerciaux() {
        return this.gson.toJson(this.gestionCommerciale.getListeDisponibilites());
    }
    
    /**
     * Permet de MAJ la liste des dispo des commerciaux (pour l'usage du CA)
     * @param listeDispo La liste de disponibilités MAJ
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setListeDispoCommerciaux(ArrayList<Disponibilite> listeDispo) {
        this.gestionCommerciale.setListeDisponibilites(listeDispo);
    }

    /**
     * PUT method for updating or creating an instance of ExpoDispoCommerciauxResource
     * @param content representation for the resource
     */
    //@PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    //public void putJson(String content) {
    //}

    private GestionCommercialeLocal lookupGestionCommercialeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (GestionCommercialeLocal) c.lookup("java:global/AppliCommercial-ear/AppliCommercial-ejb-1.0-SNAPSHOT/GestionCommerciale!fr.miage.m2.metier.GestionCommercialeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
