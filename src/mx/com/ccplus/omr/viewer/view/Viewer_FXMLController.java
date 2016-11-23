package mx.com.ccplus.omr.viewer.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mx.com.ccplus.omr.model.Coordinate;
import mx.com.ccplus.omr.model.Matrix;
import mx.com.ccplus.omr.model.Template;
import mx.com.ccplus.omr.viewer.Viewer;
import mx.com.ccplus.omr.viewer.model.RegMatrix;

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
    
    @FXML private TableView<RegMatrix> tableMatrix;
    @FXML private TableColumn<RegMatrix, String> colId;
    @FXML private TableColumn<RegMatrix, String> colName;
    @FXML private TableColumn<RegMatrix, String> colColor;
    @FXML private TableColumn<RegMatrix, String> colColumns;
    @FXML private TableColumn<RegMatrix, String> colRows;
    @FXML private TableColumn<RegMatrix, String> colStarting;
    @FXML private TableColumn<RegMatrix, String> colEnding;
    
    private ObservableList<RegMatrix> listaObservableM = FXCollections.observableArrayList();
    private FilteredList<RegMatrix> listaFiltradaM;
    private SortedList<RegMatrix> listaOrdenadaM;
    
    @FXML private Button btnDelete;
    
    @FXML private Button btnAccept;
    
    private GraphicsContext gc;
    private Image image;
    private int colorCounter = 0;
    private final float COLOR_HUE_STEP = 0.5f;
    private final float COLOR_SATURATION = 1f;
    private final float COLOR_BRIGHTNESS = 1f;
    private final float COLOR_OPACITY = 0.4f;
    
    private boolean flagTfName = false;
    private boolean flagTfSize = false;
    private boolean flagTfColumns = false;
    private boolean flagTfRows = false;
    
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        disableAllViews();
        setupTextFields();
        
        setupColumnsM();
        
        setupCanvas();
        disableCavas();
        
    }    
        
    private void disableAllViews(){
        disableAddMatrixDetailsViews();
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
    
    private void setupColumnsM(){
        colId.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("name"));
        colColor.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("color"));
        colColumns.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("columns"));
        colRows.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("rows"));
        colStarting.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("starting"));
        colEnding.setCellValueFactory(new PropertyValueFactory<RegMatrix, String>("ending"));
    }
        
    private void setupCanvas(){
        gc = canvas.getGraphicsContext2D();
        imageView.setPreserveRatio(true);
        canvas.setHeight(imageView.getBoundsInParent().getHeight());
        canvas.setWidth(imageView.getBoundsInParent().getWidth());
    }
    
    private void disableCavas(){
        gc.setFill(new Color(0,0,0,0.2));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawAllMatrixes();
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
            float hue = COLOR_HUE_STEP * (float) colorCounter;
            gc.setFill(Color.hsb(hue, COLOR_SATURATION, COLOR_BRIGHTNESS, COLOR_OPACITY));
            gc.fillOval(event.getX()-((int)(pixelSize/2)), event.getY()-((int)(pixelSize/2)), pixelSize, pixelSize);
            
            if(temporaryMatrix == null){
                double[] d = getLocationInImage(event, imageView, image);
                tfStarting.setText(df.format(d[0]) + ", " + df.format(d[1]));
            }
            
            if(temporaryMatrix != null) drawMatrix(temporaryMatrix);
            drawAllMatrixes();
        }
        
       
        
        
        
    }
    
    private double[] getLocationInImage(MouseEvent event, ImageView imageView, Image image){
        double cursor_width = event.getX();
        double cursor_height = event.getY();

        double node_height = imageView.getBoundsInParent().getHeight();
        double node_width = imageView.getBoundsInParent().getWidth();

        double image_height = image.getHeight();
        double image_width = image.getWidth();

        double d1 = (cursor_width/node_width)*image_width;
        double d2 = (cursor_height/node_height)*image_height;
        
        return new double[]{d1, d2};
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
            
            drawMatrix(new Matrix("test1", numberColumn, numberRow, pixelSize, (COLOR_HUE_STEP * (float) colorCounter), startingPoint, currentPoint));
            
            double[] d = getLocationInImage(event, imageView, image);
            tfEnding.setText(df.format(d[0]) + ", " + df.format(d[1]));
            
            drawAllMatrixes();
            
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
            temporaryMatrix = new Matrix(tfName.getText(), numberColumn, numberRow, pixelSize, (COLOR_HUE_STEP * (float) colorCounter), startingPoint, currentPoint);
            
            disableAddMatrixDetailsViews();
            btnAdd.setDisable(false);
            
        }
    }
    
    private void drawMatrix(Matrix matrix){
        gc.setFill(Color.hsb(matrix.getHue(), COLOR_SATURATION, COLOR_BRIGHTNESS, COLOR_OPACITY));
        Coordinate[][] coordinateArray = matrix.getCoordinates();
        int diameter = matrix.getDiameter();
        for (int i = 0; i < coordinateArray.length; i++) {
            for (int j = 0; j < coordinateArray[0].length; j++) {
                gc.fillOval(coordinateArray[i][j].getX()-((int)(diameter/2)), coordinateArray[i][j].getY()-((int)(diameter/2)), diameter, diameter);
            }
        }
    }
    
    private void drawAllMatrixes(){
        for (int i = 0; i < template.getMatrixes().size(); i++) {
            drawMatrix(template.getMatrixes().get(i));
        }
    }
    
    @FXML
    private void handleBtnLoad(Event event){
        image = new Image("http://www.omrtestsheet.com/omr-sheet-sample/90-Questions-OMR-Sheet.png");
        imageView.setImage(image);
        canvas.setHeight(imageView.getBoundsInParent().getHeight());
        canvas.setWidth(imageView.getBoundsInParent().getWidth());
                
        enableAddMatrixDetailsViews();
        btnLoad.setDisable(true);
    }
    
    private void enableAddMatrixDetailsViews(){
        tfName.setDisable(false);
        tfSize.setDisable(false);
        tfColumns.setDisable(false);
        tfRows.setDisable(false);
    }
    
    private void disableAddMatrixDetailsViews(){
        tfName.setDisable(true);
        tfSize.setDisable(true);
        tfColumns.setDisable(true);
        tfRows.setDisable(true);
    }
    
    private void enableChooseC(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawAllMatrixes();
    }
    
    private void disableChooseC(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(new Color(0,0,0,0.2));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawAllMatrixes();
    }
    
    @FXML
    private void handleBtnAdd(Event event){
        template.getMatrixes().add(temporaryMatrix);
        
        temporaryMatrix = null;
        startingPoint = new Coordinate();
        currentPoint = new Coordinate();
        
        clearMatrixDetailsViews();
        btnAdd.setDisable(true);
        enableAddMatrixDetailsViews();
        colorCounter = colorCounter + 1;
    }
    
    private void clearMatrixDetailsViews(){
        tfName.setText("");
        tfSize.setText("");
        tfColumns.setText("");
        tfRows.setText("");
        tfStarting.setText("");
        tfEnding.setText("");
    }
   
    
    
    
    
    @FXML
    private void handleBtnAccept(Event event){
        System.out.println(template);
        System.out.println("COLORCOUNT: " + colorCounter);
    }
    
}
