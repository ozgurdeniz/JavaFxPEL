package views;

import controlleur.Controleur;
import facade.exceptions.InformationsSaisiesIncoherentesException;
import facade.exceptions.UtilisateurDejaConnecteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;

public class Connexion {


    public BorderPane connexionRoot;
    public TextField id;
    public TextField password;
    public Label msgErreur;
    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }

    public static Connexion creerInstance(Controleur controleur, Stage stage) {
        URL location = views.Connexion.class.getResource("/views/connexion.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connexion view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        return view;
    }

    public void show() {
        stage.setTitle("Connexion");
        Scene scene = new Scene(connexionRoot, connexionRoot.getPrefWidth(), connexionRoot.getPrefHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void seConnecter(ActionEvent actionEvent) throws InformationsSaisiesIncoherentesException, UtilisateurDejaConnecteException {
        try {
            controleur.seConnecter(id.getText(),password.getText());
        } catch (InformationsSaisiesIncoherentesException e){
            msgErreur.setText("Informations saisies incorrectes !");
        } catch (UtilisateurDejaConnecteException e) {

        }
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

    public void close() {
        stage.close();
    }
}
