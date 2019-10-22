package views;

import controlleur.Controleur;
import facade.exceptions.MatchClosException;
import facade.exceptions.ResultatImpossibleException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.Match;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;

public class ParierMatch {

    public BorderPane parierMatchRoot;
    public TextField montantPari;
    public ChoiceBox vainqueur;

    private String vainqueurSelection;
    private Controleur controleur;
    private Stage stage;
    private long idMatch;

    public void setIdMatch(long idMatch) {
        this.idMatch = idMatch;
    }

    public void setVainqueurSelection(String vainqueurSelection) {
        this.vainqueurSelection = vainqueurSelection;
    }

    public static ParierMatch CreerInstance(Controleur controleur, Stage stage, Match match) {
        URL location = views.ParierMatch.class.getResource("/views/parierMatch.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ParierMatch view = fxmlLoader.getController();
        view.setStage(stage);
        view.afficherLesEquipes(match);
        view.setControleur(controleur);
        view.setIdMatch(match.getIdMatch());
        return view;
    }

    private void afficherLesEquipes(Match match) {
        vainqueur.getItems().clear();
        vainqueur.getItems().addAll(match.getEquipe1(),match.getEquipe2());
    }


    public void show() {
        stage.setTitle("Alors on pari ??");
        stage.setScene(new Scene(parierMatchRoot, parierMatchRoot.getPrefWidth(), parierMatchRoot.getPrefHeight()));
        stage.show();
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public Controleur getControleur() {
        return controleur;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void validerPari(ActionEvent actionEvent) throws MatchClosException, ResultatImpossibleException {
        setVainqueurSelection(vainqueur.getValue().toString());
        try {
            controleur.validerPari(montantPari.getText(), vainqueurSelection, controleur.getUtilisateur().getLogin(), idMatch,this);
        } catch (MatchClosException e) {

        } catch (ResultatImpossibleException e ){

        }
    }
}
