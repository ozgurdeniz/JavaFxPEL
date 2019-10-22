package application;


import controlleur.Controleur;
import facade.FacadeParis;
import facade.FacadeParisStaticImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

public class Main extends Application {

    public void start (Stage primaryStage) throws Exception{
        // init du modele : TODO
        FacadeParis facadeParis  = new FacadeParisStaticImpl();
        Controleur monControleur = new Controleur(primaryStage, facadeParis);}

    public static void main(String[] args) {
        launch(args);
    }



}
