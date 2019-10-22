package controlleur;

import facade.FacadeParis;
import facade.exceptions.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Match;
import modele.Pari;
import modele.Utilisateur;
import observer.*;
import views.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Controleur {
    private Stage fenetrePrincipale;
    private FacadeParis facade;

    //Pages
    private Connexion connexion;
    private Menu menu;
    private MatchsAVenir matchAVenir;
    private ParierMatch parierMatch;
    private MesParis mesParis;
    private MesMatchs mesMatchs;
    private MatchsFinis matchsFinis;

    private Collection<Observateur> observateurs = new ArrayList<>();
    private Utilisateur utilisateur;



    public Controleur(Stage stage, FacadeParis facadeParis) {
        facade = facadeParis;
        fenetrePrincipale = stage;
        this.connexion = Connexion.creerInstance(this,fenetrePrincipale);
        connexion.show();
    }

    public void ajouterObservateur(Observateur o){
        observateurs.add(o);
    }

    public void enleverObservateur(Observateur o){
        observateurs.remove(o);
    }

    public void notifierToutLeMonde(Evenement e){
        for(Observateur o : observateurs){
            o.notifier(e);
        }
    }

    public void seConnecter(String id, String password) throws InformationsSaisiesIncoherentesException, UtilisateurDejaConnecteException {
        utilisateur = facade.connexion(id,password);
        this.menu = Menu.creerInstance(this, new Stage());
        menu.show();
        connexion.close();
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void ouvreMatchAVenir() {
        if(utilisateur.isAdmin){
            this.mesMatchs = MesMatchs.CreerInstance(this,new Stage());
            ajouterObservateur(mesMatchs);
            mesMatchs.show();
        } else {
            this.matchAVenir = MatchsAVenir.CreerInstance(this, new Stage());
            ajouterObservateur(matchAVenir);
            matchAVenir.show();
        }
    }

    public void ouvreMesParis() {
        Collection<Pari> paris = facade.getMesParis(utilisateur.getLogin());
        this.mesParis = MesParis.CreerInstance(this, new Stage(),paris);
        ajouterObservateur(mesParis);
        mesParis.show();
    }

    public void seDeconnecter() {
        facade.deconnexion(utilisateur.getLogin());
        utilisateur = null;
        connexion.getStage().show();
        menu.close();
    }

    public void goParier(long idMatchSelection) {
        Match match = facade.getMatch(idMatchSelection);
        this.parierMatch = ParierMatch.CreerInstance(this, new Stage(),match);
        parierMatch.show();
    }

    public Collection<Match> getListMatch(){
        return facade.getMatchsPasCommences();
    }

    public void validerPari(String text, String vainqueurSelection, String login, long idMatch, Object source) throws MatchClosException, ResultatImpossibleException {
        facade.parier(login,idMatch,vainqueurSelection,Double.parseDouble(text));
        notifierToutLeMonde(new Evenement(source));
    }

    public void annulerPari(long idPariSelection, String login, Object source) throws OperationNonAuthoriseeException {
        facade.annulerPari(login,idPariSelection);
        facade.getPari(idPariSelection);
        notifierToutLeMonde(new Evenement(source));
    }

    public Collection<Pari> getMesParis(String login) {
        return facade.getMesParis(login);
    }

    public void ajouterMatch(String text, String text1, String text2, LocalDateTime horaire,Object source) throws UtilisateurNonAdminException {
        facade.ajouterMatch(utilisateur.getLogin(), text, text1, text2, horaire);
        notifierToutLeMonde(new Evenement(source));
    }

    public void saisirResultat(long idMatch,String text,Object source) throws UtilisateurNonAdminException, ResultatImpossibleException {
        facade.resultatMatch(utilisateur.getLogin(),idMatch,text);
        notifierToutLeMonde(new Evenement(source));
    }

    public void afficherMatchsFinis(){
        this.matchsFinis = MatchsFinis.CreerInstance(this, new Stage());
        ajouterObservateur(matchsFinis);
        matchsFinis.show();
    }

    public Collection<Match> getMatchsFinis() {
        Collection<Match> matchsEnCours =  facade.getMatchsPasCommences();
        Collection<Match> matchs = facade.getAllMatchs();
        for (Match m: matchsEnCours) {
            matchs.remove(m);
        }
        return matchs;
    }

    public Collection<Match> getMatchPasCommences() {
        return facade.getMatchsPasCommences();
    }

    public void messageErreur(String msg){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Erreur");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(msg);
        Button closeButton = new Button("Fermer !");
        closeButton.setOnAction(ex -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout,layout.getPrefWidth(),layout.getPrefHeight());
        window.setScene(scene);
        window.showAndWait();
    }
}
