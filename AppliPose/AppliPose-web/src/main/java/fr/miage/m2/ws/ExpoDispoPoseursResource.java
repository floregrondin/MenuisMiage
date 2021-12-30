/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.ws;

import com.google.gson.Gson;
import fr.miage.m2.metier.GestionPoseLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Valentin
 */
@Path("ExpoDispoPoseurs")
@RequestScoped
public class ExpoDispoPoseursResource {

    GestionPoseLocal gestionPose = lookupGestionPoseLocal();

    private Gson gson;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ExpoDispoPoseursResource
     */
    public ExpoDispoPoseursResource() {
        this.gson = new Gson();
    }

    /**
     * Retrieves representation of an instance of
     * fr.miage.m2.ws.ExpoDispoPoseursResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getListeDispoPoseurs() {
        return this.gson.toJson(this.gestionPose.getListeDisponibilites());
    }
    
    @PUT
    @Path("{idDispo}")
    public void updateDispo(@PathParam("idDispo") String idDispo) {
        Long idD = Long.valueOf(idDispo);
        this.gestionPose.updateDisponibilite(idD);
    }

    /**
     * PUT method for updating or creating an instance of
     * ExpoDispoPoseursResource
     *
     * @param content representation for the resource
     */
    //@PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    //public void putJson(String content) {
    //}
    private GestionPoseLocal lookupGestionPoseLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (GestionPoseLocal) c.lookup("java:global/AppliPose-ear/AppliPose-ejb-1.0-SNAPSHOT/GestionPose!fr.miage.m2.metier.GestionPoseLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
