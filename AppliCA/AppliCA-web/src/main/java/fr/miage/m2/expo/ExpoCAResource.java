/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.m2.expo;

import com.google.gson.Gson;
import fr.miage.m2.menuismiageshared.Affaire;
import fr.miage.m2.metier.GestionCALocal;
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
        System.out.println("dispo commerciaux : " + this.gestionCA.getAllDispoCommerciaux());
        String response = this.gson.toJson(this.gestionCA.getAllDispoCommerciaux());
        response = response.replace("\\", "");
        response = response.substring( 1, response.length() - 1 );
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
        try {
            String response = this.gson.toJson(this.gestionCA.getAffaireByIdCommande(Long.valueOf(idCommande)));
            if (response == null){
                return Response.status(404).build();
            }
            return Response.ok(response).build();
        } catch (Exception ex){
            return Response.status(404).build(); 
        }  
    }
    
    @GET
    @Path("affaires/{idAffaire}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAffaireByIdAffaire(@PathParam("idAffaire") String idAffaire) throws Exception {
        try {
            String response = this.gson.toJson(this.gestionCA.getAffaireByIdAffaire(Long.valueOf(idAffaire)));

            if (response == null){
                return Response.status(404).build();
            }
            return Response.ok(response).build();
        } catch (Exception ex){
            return Response.status(404).build();
        }
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
    public Response majRDVCommercial(@PathParam("idDispo") String idDispo, @QueryParam("idAffaire") String idAffaire) throws Exception {
        Affaire aff = this.gestionCA.getAffaireByIdAffaire(Long.valueOf(idAffaire));
        
        System.out.println("affaire : " + aff);
        if (aff == null
                || aff.toString().contains("Not Found")){
            System.out.println("ERREUR : AFFAIRE INEXISTANTE.");
            return Response.status(400).build();
        }
        
        System.out.println("dispo commerciaux : " + this.gestionCA.getAllDispoCommerciaux());
        System.out.println("req : " + !this.gestionCA.getAllDispoCommerciaux().contains("\"idDisponibilite\":"+idDispo));
        if (this.gestionCA.getAllDispoCommerciaux().isEmpty()
                || (!this.gestionCA.getAllDispoCommerciaux().contains("\"idDisponibilite\":"+idDispo))){
            System.out.println("ERREUR : DISPONIBILITE INDIQUEE INCORRECTE.");
            return Response.status(400).build();
        }
                
        if ("CREEE".equals(aff.getEtatAffaire().toString())){
            // Maj l'état de l'affaire
            this.gestionCA.setEtatDispoCommerciaux(idAffaire, idDispo);
        } else {
            System.out.println("ERREUR : AFFAIRE NE NECESSITE PAS UN RDV COMMERCIAL");
            return Response.status(401).build();
        }
        return Response.ok("RDV Commercial pris.").build();
    }
    
    @PUT
    @Path("RDVPoseurs/{idDispo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response majRDVPoseur(@PathParam("idDispo") String idDispo, @QueryParam("idAffaire") String idAffaire) throws Exception {
        System.out.println("ma valeur idAffaire : " + idAffaire);
        Affaire aff = this.gestionCA.getAffaireByIdAffaire(Long.valueOf(idAffaire));
        
        if (aff == null
                || aff.toString().contains("Not Found")){
            System.out.println("ERREUR : AFFAIRE INEXISTANTE.");
            return Response.status(400).build();
        }
        
        if (!aff.toString().contains("idCommande")){
            System.out.println("ERREUR : PAS DE COMMANDE ASSOCIEE.");
            return Response.status(404).build();
        }
        
        if ("RECEPTIONNEE".equals(aff.getEtatAffaire().toString())){
            // Maj l'état de l'affaire
            this.gestionCA.setEtatDispoPoseurs(idAffaire, idDispo);
        } else {
            System.out.println("ERREUR : AFFAIRE NE NECESSITE PAS UN RDV DE POSE");
            return Response.status(401).build();
        }
        return Response.ok("RDV Poseur pris.").build();
    }

}
