package views;

import controlleur.Controleur;
import facade.exceptions.ResultatImpossibleException;
import facade.exceptions.UtilisateurNonAdminException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Match;
import observer.Evenement;
import observer.Observateur;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static javafx.collections.FXCollections.observableArrayList;

public class MatchsFinis implements Observateur {

    public TableView<Match> tableView;
    public VBox matchsFinisRoot;
    public TableColumn id;
    public TableColumn sportMatch;
    public TableColumn eq2Match;
    public TableColumn eq1Match;
    public TableColumn dateMatch;
    public TableColumn resultat;
    public TextField res;
    private ObservableList<Match> listMatchs;
    private Controleur controleur;
    private Stage stage;

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static MatchsFinis CreerInstance(Controleur controleur, Stage stage) {
        URL location = views.MatchsFinis.class.getResource("/views/matchsFinis.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MatchsFinis view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.setMatchsFinis();
        view.afficherLesMatchsFinis();
        return view;
    }

    public void show() {
        stage.setTitle("Saisir Resultat");
        stage.setScene(new Scene(matchsFinisRoot, matchsFinisRoot.getPrefWidth(), matchsFinisRoot.getPrefHeight()));
        stage.show();
    }

    @Override
    public void notifier(Evenement event) {
        setMatchsFinis();
        afficherLesMatchsFinis();
    }

    private void afficherLesMatchsFinis() {
        id.setCellValueFactory(new PropertyValueFactory<>("idMatch"));
        sportMatch.setCellValueFactory(new PropertyValueFactory<>("sport"));
        eq2Match.setCellValueFactory(new PropertyValueFactory<>("equipe2"));
        resultat.setCellValueFactory(new PropertyValueFactory<>("resultat"));
        dateMatch.setCellValueFactory(new PropertyValueFactory<>("quand"));
        eq1Match.setCellValueFactory(new PropertyValueFactory<>("equipe1"));
        tableView.setItems(listMatchs);
    }

    private void setMatchsFinis() {
        listMatchs = observableArrayList();
        Collection<Match> matchsFinis = controleur.getMatchsFinis();
        for (Match m:  matchsFinis) {
            listMatchs.add(m);
        }
    }

    public void saisirResultat(ActionEvent actionEvent) throws UtilisateurNonAdminException{
        try {
            controleur.saisirResultat(tableView.getSelectionModel().getSelectedItem().getIdMatch(),res.getText(),this);
        } catch (ResultatImpossibleException e){
            controleur.messageErreur("Invalide Verdict");
        } catch (NullPointerException e) {
            controleur.messageErreur("Selectionnez un match");
        }
    }
}

