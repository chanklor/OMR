package mx.com.ccplus.omr.view.intro.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import mx.com.ccplus.omr.view.viewer.Viewer;

public class Intro_FXMLController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void handleOneTimeButtonOnClick(Event event) {
        Viewer viewer = new Viewer();
        viewer.mostrar(Viewer.ONE_TIME);
    }
    
}
