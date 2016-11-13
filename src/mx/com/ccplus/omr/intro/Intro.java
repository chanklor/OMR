package mx.com.ccplus.omr.intro;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Intro {
    
    public void mostrar(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("view/Intro_FXML.fxml"));
            Parent root = (Parent) fXMLLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            System.out.println("error al abrir intro");
            e.printStackTrace();
        }
    }
    
}
