package mx.com.ccplus.omr.viewer.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mx.com.ccplus.omr.model.Coordinate;
import mx.com.ccplus.omr.model.Matrix;
import mx.com.ccplus.omr.model.Template;
import mx.com.ccplus.omr.viewer.Viewer;

public class Viewer_FXMLController implements Initializable {
    
    Template template = new Template();
    Matrix temporaryMatrix;
    Coordinate startingPoint = new Coordinate();
    Coordinate currentPoint = new Coordinate();
    
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
        setupTextFields();
        
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
    
    private UnaryOperator<Change> getFilter() {
        return change -> {
            String text = change.getText();
            for (int i = 0; i < text.length(); i++)
            if (!Character.isDigit(text.charAt(i)))
                return null;
            return change;
        };
    }    
    
    private void setupTextFields(){
        
        tfSize.setTextFormatter(new TextFormatter<>(getFilter()));
        tfColumns.setTextFormatter(new TextFormatter<>(getFilter()));
        tfRows.setTextFormatter(new TextFormatter<>(getFilter()));
        
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
        if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) canvas.getScene().setCursor(Cursor.CROSSHAIR);
    }
    
    @FXML
    private void handleCanvasOnMouseExit(Event event) {
        canvas.getScene().setCursor(Cursor.DEFAULT);
    }
    
    @FXML
    private void handleCanvasOnMouseMoved(MouseEvent event) {
        if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            int pixelSize = (Integer.parseInt(tfSize.getText()) * 2) + 1;
            gc.setFill(new Color(1,0,0,0.4));
            gc.fillOval(event.getX()-((int)(pixelSize/2)), event.getY()-((int)(pixelSize/2)), pixelSize, pixelSize);
        }
        
        if(temporaryMatrix != null) drawCoordinateArray(temporaryMatrix.getCoordinates(), temporaryMatrix.getDiameter());
        
    }
    
    @FXML
    private void handleCanvasOnMousePressed(MouseEvent event) {
        if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) {
            startingPoint.setX((int) event.getX());
            startingPoint.setY((int) event.getY());
        }
    }
    
    @FXML
    private void handleCanvasOnMouseDrag(MouseEvent event) {
        if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) {
            
            int numberColumn = Integer.parseInt(tfColumns.getText());
            int numberRow = Integer.parseInt(tfRows.getText());
            int pixelSize = (Integer.parseInt(tfSize.getText()) * 2) + 1;
            
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
            currentPoint.setX((int) event.getX());
            currentPoint.setY((int) event.getY());
            
            drawCoordinateArray(new Matrix("test1", numberColumn, numberRow, pixelSize, startingPoint, currentPoint).getCoordinates(), pixelSize);
            
            

        }
    }
    
    @FXML
    private void handleCanvasOnMouseReleased(MouseEvent event) {
        if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) {
            int numberColumn = Integer.parseInt(tfColumns.getText());
            int numberRow = Integer.parseInt(tfRows.getText());
            int pixelSize = (Integer.parseInt(tfSize.getText()) * 2) + 1;
            currentPoint.setX((int) event.getX());
            currentPoint.setY((int) event.getY());
            temporaryMatrix = new Matrix(tfName.getText(), numberColumn, numberRow, pixelSize, startingPoint, currentPoint);
        }
    }
    
    private void drawCoordinateArray(Coordinate[][] coordinateArray, int diameter){
        gc.setFill(new Color(1,0,0,0.4));
        for (int i = 0; i < coordinateArray.length; i++) {
            for (int j = 0; j < coordinateArray[0].length; j++) {
                gc.fillOval(coordinateArray[i][j].getX()-((int)(diameter/2)), coordinateArray[i][j].getY()-((int)(diameter/2)), diameter, diameter);
            }
        }
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
    
    @FXML
    private void handleBtnAdd(Event event){
        template.getMatrixes().add(temporaryMatrix);
    }
   
    
}
