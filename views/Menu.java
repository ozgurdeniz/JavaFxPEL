package views;

import controlleur.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import modele.Utilisateur;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;

public class Menu implements Observateur {

    public Label msgBienVenue;
    public BorderPane menuRoot;
    public GridPane admin;
    private Controleur controleur;
    private Stage stage;

    public static Menu creerInstance(Controleur controleur, Stage stage) {
        URL location = views.Menu.class.getResource("/views/menu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Menu view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.actualiserPages();
        view.afficherMsg();
        if (controleur.getUtilisateur().isAdmin) {
            view.afficherAdmin();
        }
        return view;
    }

    public void afficherAdmin() {
        Hyperlink afficherMatchFini = new Hyperlink("Afficher Matchs Finis");
        admin.getRowConstraints().add(new RowConstraints());
        admin.addRow(4,afficherMatchFini);
        afficherMatchFini.setOnAction(e -> afficherMatchsFinis(e));
    }

    public void afficherMatchsFinis(ActionEvent actionEvent){
        controleur.afficherMatchsFinis();
    }

    private void actualiserPages() {
        //TODO
    }

    private void afficherMsg(){
        Utilisateur user = controleur.getUtilisateur();
        msgBienVenue.setText("Bienvenue " + user.getLogin());
    }

    public void show() {
        stage.setTitle("Menu");
        stage.setScene(new Scene(menuRoot, menuRoot.getPrefWidth(), menuRoot.getPrefHeight()));
        stage.show();
    }

    @Override
    public void notifier(Evenement event) {
        actualiserPages();
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

    public void ouvreMatchAVenir(ActionEvent actionEvent) {
        controleur.ouvreMatchAVenir();
    }

    public void ouvreMesParis(ActionEvent actionEvent) {
        controleur.ouvreMesParis();
    }

    public void seDeconnecter(ActionEvent actionEvent) {
        controleur.seDeconnecter();
    }

    public void close() {
        stage.close();
    }
}
