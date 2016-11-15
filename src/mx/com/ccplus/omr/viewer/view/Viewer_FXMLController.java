package mx.com.ccplus.omr.viewer.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import mx.com.ccplus.omr.viewer.Viewer;

public class Viewer_FXMLController implements Initializable {
    
    int purpose = Viewer.getPurpose();
    
    @FXML private Canvas canvas;
    
    @FXML private ImageView imageView;
    
    @FXML private Button btnLoad;
    
    @FXML private TextField tfName;
    @FXML private TextField tfSize;
    @FXML private TextField tfColumns;
    @FXML private TextField tfRows;
    
    @FXML private Button btnChooseCoords;
    
    @FXML private TextField tfStarting;
    @FXML private TextField tfEnding;
    
    @FXML private Button btnAdd;
    
    
    
    @FXML private Button btnDelete;
    
    @FXML private Button btnAccept;
    
    private GraphicsContext gc;
    private Image image;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = canvas.getGraphicsContext2D();
        imageView.setPreserveRatio(true);
        canvas.setHeight(imageView.getBoundsInParent().getHeight());
        canvas.setWidth(imageView.getBoundsInParent().getWidth());
        
        disableAllViews();
        
        
    }    
    
    private void disableAllViews(){
        tfName.setEditable(false);
        tfSize.setEditable(false);
        tfColumns.setEditable(false);
        tfRows.setEditable(false);
        btnChooseCoords.setDisable(true);
        btnAdd.setDisable(true);
        btnAccept.setDisable(true);
    }
    
    @FXML
    private void handleCanvasOnMouseEnter(Event event) {
        canvas.getScene().setCursor(Cursor.CROSSHAIR);
    }
    
    @FXML
    private void handleCanvasOnMouseExit(Event event) {
        canvas.getScene().setCursor(Cursor.DEFAULT);
    }
    
    @FXML
    private void handleBtnLoad(Event event){
        image = new Image("http://www.omrtestsheet.com/omr-sheet-sample/90-Questions-OMR-Sheet.png");
        imageView.setImage(image);
        canvas.setHeight(imageView.getBoundsInParent().getHeight());
        canvas.setWidth(imageView.getBoundsInParent().getWidth());
        
        
    }
    
}
