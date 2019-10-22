package views;

import controlleur.Controleur;
import facade.exceptions.ResultatImpossibleException;
import facade.exceptions.UtilisateurNonAdminException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Match;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class MesMatchs implements Observateur {
    public TableView<Match> tableView;
    public VBox vBox;
    public TextField sport;
    public TextField eq1;
    public TextField eq2;
    public TextField date;

    public TableColumn<Match,Long> id;
    public TableColumn<Match,String> sportMatch;
    public TableColumn<Match,String> eq2Match;
    public TableColumn<Match,String> eq1Match;
    public TableColumn<Match,LocalDateTime> dateMatch;

    private ObservableList<Match> listMatchs;

    private Controleur controleur;
    private Stage stage;


    public static MesMatchs CreerInstance(Controleur controleur, Stage stage) {
        URL location = views.MesMatchs.class.getResource("/views/mesMatchs.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MesMatchs view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.remplireList();
        view.afficherMatchsPasCommencer();
        return view;
    }

    private void remplireList() {
        listMatchs = observableArrayList();
        ArrayList<Match> matchsPasCommences = (ArrayList<Match>) controleur.getListMatch();
        for (Match m:  matchsPasCommences) {
            listMatchs.add(m);
        }
    }

    public void show() {
        stage.setTitle("Matchs à venir !");
        stage.setScene(new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefHeight()));
        stage.show();
    }

    public void afficherMatchsPasCommencer(){
        id.setCellValueFactory(new PropertyValueFactory<>("idMatch"));
        sportMatch.setCellValueFactory(new PropertyValueFactory<>("sport"));
        eq1Match.setCellValueFactory(new PropertyValueFactory<>("equipe1"));
        eq2Match.setCellValueFactory(new PropertyValueFactory<>("equipe2"));
        dateMatch.setCellValueFactory(new PropertyValueFactory<>("quand"));
        tableView.setItems(listMatchs);
    }

    @Override
    public void notifier(Evenement event) {
        remplireList();
        afficherMatchsPasCommencer();
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void ajouterMatch(ActionEvent actionEvent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime horaire = null;
        try {
            horaire = LocalDateTime.parse(date.getText(),formatter);
            controleur.ajouterMatch(sport.getText(),eq1.getText(),eq2.getText(),horaire,this);


        } catch (DateTimeParseException e){
            controleur.messageErreur("Le format attendu est : yyyy-MM-dd HH:mm \n Exemple : 2019-12-25 19:30");
        } catch (UtilisateurNonAdminException e) {

        }
    }

    public void parier(ActionEvent actionEvent) {
        try {
            controleur.goParier(tableView.getSelectionModel().getSelectedItem().getIdMatch());
        } catch (NullPointerException e){
            controleur.messageErreur("Choisissez un match");
        }
    }
}
