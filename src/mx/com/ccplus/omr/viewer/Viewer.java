package mx.com.ccplus.omr.viewer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Viewer {
    
    public static final int ONE_TIME = 1;
    public static final int CREATE_TEMPLATE = 2;
    
    private static int purpose = 0;

    public static int getPurpose() {
        return purpose;
    }

    public void mostrar(int purpose){
        this.purpose = purpose;    
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("view/Viewer_FXML.fxml"));
            Parent root = (Parent) fXMLLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            System.out.println("error al abrir viewer");
            e.printStackTrace();
        }
    }    
    
}
