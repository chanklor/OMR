package mx.com.ccplus.omr.viewer.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    @FXML private TextField tfStarting;
    @FXML private TextField tfEnding;
    
    @FXML private Button btnAdd;
    
    @FXML private Button btnDelete;
    
    @FXML private Button btnAccept;
    
    private GraphicsContext gc;
    private Image image;
    
    private boolean flagTfName = false;
    private boolean flagTfSize = false;
    private boolean flagTfColumns = false;
    private boolean flagTfRows = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = canvas.getGraphicsContext2D();
        imageView.setPreserveRatio(true);
        canvas.setHeight(imageView.getBoundsInParent().getHeight());
        canvas.setWidth(imageView.getBoundsInParent().getWidth());
        
        disableAllViews();
        setupTextFieldListeners();
        
        gc.setFill(new Color(0,0,0,0.2));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
    }    
    
    private void disableAllViews(){
        tfName.setDisable(true);
        tfSize.setDisable(true);
        tfColumns.setDisable(true);
        tfRows.setDisable(true);
        btnAdd.setDisable(true);
        btnAccept.setDisable(true);
    }
    
    private void setupTextFieldListeners(){
        tfName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                flagTfName = (newValue.equals("") ? false : true);
                if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) enableChooseC();
                else disableChooseC();
            }
        });
        tfSize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                flagTfSize = (newValue.equals("") ? false : true);
                if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) enableChooseC();
                else disableChooseC();
            }
        });
        tfColumns.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                flagTfColumns = (newValue.equals("") ? false : true);
                if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) enableChooseC();
                else disableChooseC();
            }
        });
        tfRows.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                flagTfRows = (newValue.equals("") ? false : true);
                if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) enableChooseC();
                else disableChooseC();
            }
        });
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
                
        enableAddMatrixDetailsViews();
    }
    
    private void enableAddMatrixDetailsViews(){
        tfName.setDisable(false);
        tfSize.setDisable(false);
        tfColumns.setDisable(false);
        tfRows.setDisable(false);
    }
    
    private void enableChooseC(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    private void disableChooseC(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(new Color(0,0,0,0.2));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
   
    
}
