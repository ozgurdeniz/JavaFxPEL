package views;

import controlleur.Controleur;
import facade.exceptions.OperationNonAuthoriseeException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Pari;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class MesParis implements Observateur {

    public BorderPane mesParisRoot;
    public ListView listeMesParis;
    public Label titreMesParis;
    private ArrayList<Pari> mesParis = new ArrayList<>();
    private long idPariSelection;

    private Controleur controleur;
    private Stage stage;

    public void setMesParis(Collection<Pari> mesParis) {
        this.mesParis = (ArrayList<Pari>) mesParis;
    }

    public static MesParis CreerInstance(Controleur controleur, Stage stage, Collection<Pari> paris) {
        URL location = views.MesParis.class.getResource("/views/mesParis.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MesParis view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.setMesParis(paris);
        view.afficherMesParis();
        return view;
    }

    private void afficherMesParis() {
        listeMesParis.getItems().clear();
        for (Pari p: mesParis) {
            listeMesParis.getItems().add("Vous avez parié " + p.getMontant() + "€ pour le vainqueur " + p.getVainqueur());
        }
        listeMesParis.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue) -> {
            if ((int) newValue >= 0){
                idPariSelection = mesParis.get((int) newValue).getIdPari();
            }
        });
    }

    public void show() {
        stage.setTitle("Mes paris");
        stage.setScene(new Scene(mesParisRoot, mesParisRoot.getPrefWidth(), mesParisRoot.getPrefHeight()));
        stage.show();
    }

    @Override
    public void notifier(Evenement event) {
        setMesParis(controleur.getMesParis(controleur.getUtilisateur().getLogin()));
        afficherMesParis();

    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void annulerPari(ActionEvent actionEvent) throws OperationNonAuthoriseeException {
       try {
           controleur.annulerPari(idPariSelection,controleur.getUtilisateur().getLogin(),this);
       } catch (OperationNonAuthoriseeException e){
           controleur.messageErreur("Pari perimé");
       } catch (NullPointerException e){
           controleur.messageErreur("Selectionnez un pari");
       }
    }
}
