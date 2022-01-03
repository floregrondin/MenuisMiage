/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import com.google.gson.Gson;
import fr.miage.m2.metier.GestionCALocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 *
 * Déployé sur url : http://localhost:8080/AppliCA-web/webresources/ExpoCA
 *
 * @author Flo
 */
@Path("ExpoCA")
@RequestScoped
public class ExpoCAResource {

    GestionCALocal gestionCA = lookupGestionCALocal();

    private Gson gson;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ExpoCAResource
     */
    public ExpoCAResource() {
        this.gson = new Gson();
    }

    /**
     * Retrieves representation of an instance of
     * fr.miage.m2.exposition.ExpoCAResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ExpoCAResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    private GestionCALocal lookupGestionCALocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (GestionCALocal) c.lookup("java:global/AppliCA-ear/AppliCA-ejb-1.0-SNAPSHOT/GestionCA!fr.miage.m2.metier.GestionCALocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @POST
    @Path("affaires")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAffaire(@QueryParam("prenomClient") String prenomClient, @QueryParam("nomClient") String nomClient, @QueryParam("adresseClient") String adresseClient, @QueryParam("mailClient") String mailClient, @QueryParam("telClient") String telClient, @QueryParam("geolocalisationClient") String geolocalisationClient) {
        Long idAffaire = this.gestionCA.creerAffaire(prenomClient, nomClient, adresseClient, mailClient, telClient, geolocalisationClient);
        return Response.ok(this.gson.toJson(idAffaire)).build();
    }
    
    @GET
    @Path("affaires")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAffaires() {
        return Response.ok(this.gson.toJson(this.gestionCA.getAllAffaires())).build();
    }

    @GET
    @Path("dispoCommerciaux")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDispoCommerciaux() {
        String response = this.gson.toJson(this.gestionCA.getAllDispoCommerciaux());
        response = response.replace("\\", "");
        response = response.substring( 1, response.length() - 1 );
        //System.out.println("ICI : " + response);
        return Response.ok(response).build();
    }

    @GET
    @Path("dispoPoseurs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDispoPoseurs() {
        String response = this.gson.toJson(this.gestionCA.getAllDispoPoseurs());
        response = response.replace("\\", "");
        response = response.substring( 1, response.length() - 1 );
        return Response.ok(response).build();
    }
    
    @GET
    @Path("commandes/{idCommande}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAffaireByIdCommande(@PathParam("idCommande") String idCommande) throws Exception {
        String response = this.gson.toJson(this.gestionCA.getAffaireByIdCommande(Long.valueOf(idCommande)));
        if (response == null){
            return Response.status(404).build();
        }
        return Response.ok(response).build();
    }
    
    
    /**
     * Permet de cloturer une affaire
     * @param idAffaire l'affaire
     * @return "ok. Affaire cloturée."
     */
    @PUT
    @Path("affaires/{idAffaire}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cloturerAffaire(@PathParam("idAffaire") String idAffaire) {
        Long idA = Long.valueOf(idAffaire);
        if (this.gestionCA.getAffaire(idA) == null){
            return Response.status(404).build();
        }
        return Response.ok(this.gson.toJson(this.gestionCA.cloturerAffaire(idA))).build();
    }
    
    @PUT
    @Path("RDVCommerciaux/{idDispo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response majRDVCommercial(@PathParam("idDispo") String idDispo, @QueryParam("idCommande") String idCommande) {
        System.out.println("mon id dispo : " + idDispo);
        this.gestionCA.setEtatDispoCommerciaux(idCommande, idDispo);
        return Response.ok("RDV Commercial pris.").build();
    }
    
    @PUT
    @Path("RDVPoseurs/{idDispo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response majRDVPoseur(@PathParam("idDispo") String idDispo, @QueryParam("idCommande") String idCommande) {
        this.gestionCA.setEtatDispoPoseurs(idCommande, idDispo);
        return Response.ok("RDV Poseur pris.").build();
    }

}
