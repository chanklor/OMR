package mx.com.ccplus.omr.view.viewer.view;

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
import javafx.scene.control.TableCell;
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
import javafx.util.Callback;
import mx.com.ccplus.omr.controller.CoordinateDAO;
import mx.com.ccplus.omr.controller.MatrixDAO;
import mx.com.ccplus.omr.misc.Utils;
import mx.com.ccplus.omr.model.Coordinate;
import mx.com.ccplus.omr.model.Matrix;
import mx.com.ccplus.omr.model.Template;
import mx.com.ccplus.omr.view.viewer.Viewer;
import mx.com.ccplus.omr.view.viewer.model.RegMatrix;

public class Viewer_FXMLController implements Initializable {
    
    Template template = new Template();
    MatrixDAO matrixDAO = new MatrixDAO();
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
    //private FilteredList<RegMatrix> listaFiltradaM;
    private SortedList<RegMatrix> listaOrdenadaM;
    
    @FXML private Button btnDelete;
    
    @FXML private Button btnAccept;
    
    private GraphicsContext gc;
    private Image image;
    private int colorCounter = 0;
    private final double COLOR_HUE_STEP = 67.5;
    private final double COLOR_SATURATION = 1;
    private final double COLOR_BRIGHTNESS = 1;
    private final double COLOR_OPACITY = 0.6;
    
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
        tablePrefsM();;
        
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
        
        Callback<TableColumn<RegMatrix, String>, TableCell<RegMatrix, String>> cellFactory = new Callback<TableColumn<RegMatrix, String>, TableCell<RegMatrix, String>>() {
            public TableCell call(TableColumn p) {
                TableCell cell = new TableCell() {
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        double hue = Double.parseDouble(item.toString());
                        System.out.println("Hue: " + df.format(hue));
                        String code = Utils.getRGBoFromColor(Color.hsb(hue, COLOR_SATURATION, COLOR_BRIGHTNESS, COLOR_OPACITY));
                        System.out.println("Code: " + code);
                        setStyle("-fx-background-color:" + code);
                        setText("");
                    }
                };


                return cell;
            }
        };
        
        //colColor.setCellFactory(cellFactory);
        
    }
    
    private void tablePrefsM(){
        //listaFiltradaH = new FilteredList<>(listaObservableH, p -> false);
        //listaOrdenadaH = new SortedList<>(listaFiltradaH);
        listaOrdenadaM = new SortedList<>(listaObservableM);
        listaOrdenadaM.comparatorProperty().bind(tableMatrix.comparatorProperty());
        tableMatrix.setItems(listaOrdenadaM);
    }
    
    private void populateTable(){
        listaObservableM.clear();
        for(RegMatrix r : matrixDAO.transformMatrixArrayToRegArray(template)){
            listaObservableM.add(r);
        }
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
            double hue = COLOR_HUE_STEP * (double) colorCounter;
            gc.setFill(Color.hsb(hue, COLOR_SATURATION, COLOR_BRIGHTNESS, COLOR_OPACITY));
            gc.fillOval(event.getX()-((int)(pixelSize/2)), event.getY()-((int)(pixelSize/2)), pixelSize, pixelSize);
            
            if(temporaryMatrix == null){
                Coordinate d = matrixDAO.transformMouseCoordinateToImageLocation(new Coordinate(event.getX(), event.getY()), imageView, image);
                tfStarting.setText(df.format(d.getX()) + ", " + df.format(d.getY()));
            }
            
            if(temporaryMatrix != null) drawRealMatrix(temporaryMatrix);
            drawAllMatrixes();
        }
        
    }
        
    @FXML
    private void handleCanvasOnMousePressed(MouseEvent event) {
        if(flagTfName && flagTfSize && flagTfColumns && flagTfRows) {
            startingPoint.setX(event.getX());
            startingPoint.setY(event.getY());
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
            
            drawMatrix(new Matrix("test1", numberColumn, numberRow, pixelSize, (COLOR_HUE_STEP * (double) colorCounter), startingPoint, currentPoint));
            
            Coordinate d = matrixDAO.transformMouseCoordinateToImageLocation(new Coordinate(event.getX(), event.getY()), imageView, image);
            tfEnding.setText(df.format(d.getX()) + ", " + df.format(d.getY()));
            
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
            temporaryMatrix = new Matrix(tfName.getText(), numberColumn, numberRow, matrixDAO.transformCursorDiameterToBubbleDiameter(pixelSize, imageView, image), (COLOR_HUE_STEP * (double) colorCounter), matrixDAO.transformMouseCoordinateToImageLocation(startingPoint, imageView, image), matrixDAO.transformMouseCoordinateToImageLocation(currentPoint, imageView, image));
            
            disableAddMatrixDetailsViews();
            btnAdd.setDisable(false);
            
        }
    }
    
    private void drawMatrix(Matrix matrix){
        gc.setFill(Color.hsb(matrix.getHue(), COLOR_SATURATION, COLOR_BRIGHTNESS, COLOR_OPACITY));
        Coordinate[][] coordinateArray = matrixDAO.getCoordinates(matrix);
        double diameter = (double) matrix.getDiameter();
        for (int i = 0; i < coordinateArray.length; i++) {
            for (int j = 0; j < coordinateArray[0].length; j++) {
                gc.fillOval(coordinateArray[i][j].getX()-(diameter/2), coordinateArray[i][j].getY()-(diameter/2), diameter, diameter);
            }
        }
    }
    
    private void drawRealMatrix(Matrix matrix){
        Matrix dummyMatrix = matrixDAO.transformRealMatrixToNodeMatrix(matrix, imageView, image);
        gc.setFill(Color.hsb(dummyMatrix.getHue(), COLOR_SATURATION, COLOR_BRIGHTNESS, COLOR_OPACITY));
        Coordinate[][] coordinateArray = matrixDAO.getCoordinates(dummyMatrix);
        double diameter = (double) dummyMatrix.getDiameter();
        for (int i = 0; i < coordinateArray.length; i++) {
            for (int j = 0; j < coordinateArray[0].length; j++) {
                gc.fillOval(coordinateArray[i][j].getX()-(diameter/2), coordinateArray[i][j].getY()-(diameter/2), diameter, diameter);
            }
        }
    }
        
    private void drawAllMatrixes(){
        for (int i = 0; i < template.getMatrixes().size(); i++) {
            drawRealMatrix(template.getMatrixes().get(i));
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
        populateTable();
        
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
