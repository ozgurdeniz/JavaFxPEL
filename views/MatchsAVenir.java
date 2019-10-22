package views;

import controlleur.Controleur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.Match;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MatchsAVenir implements Observateur {
    public BorderPane matchsAVenirRoot;
    public ListView<String> listeMathcsAVenir;
    public Button boutonParier;

    private Controleur controleur;
    private Stage stage;
    private ArrayList<Match> matches = new ArrayList<>();
    private long idMatchSelection;

    public void setMatches() {
        this.matches = (ArrayList<Match>) controleur.getMatchPasCommences();
    }

    public static MatchsAVenir CreerInstance(Controleur controleur, Stage stage) {
        URL location = views.MatchsAVenir.class.getResource("/views/matchsAVenir.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MatchsAVenir view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.setMatches();
        view.afficherMatchsPasCommencer();
        return view;
    }


    public void show() {
        stage.setTitle("Matchs à venir !");
        stage.setScene(new Scene(matchsAVenirRoot, matchsAVenirRoot.getPrefWidth(), matchsAVenirRoot.getPrefHeight()));
        stage.show();
    }

    public void afficherMatchsPasCommencer(){
        listeMathcsAVenir.getItems().clear();
        for (Match m: matches) {
            listeMathcsAVenir.getItems().add(m.getEquipe1() + " Vs. " + m.getEquipe2() + " " + m.getSport() + " " + m.getQuand());
        }
        listeMathcsAVenir.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue) -> {
            if ((int) newValue >= 0){
                idMatchSelection = matches.get((int) newValue).getIdMatch();
            }
            System.out.println(idMatchSelection);
        });
    }

    @Override
    public void notifier(Evenement event) {
        setMatches();
        afficherMatchsPasCommencer();
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

    public void goParier(ActionEvent actionEvent) {
        try {
            controleur.goParier(idMatchSelection);
        } catch (NullPointerException e){
            controleur.messageErreur("Choisissez un match");
        }
    }
}
