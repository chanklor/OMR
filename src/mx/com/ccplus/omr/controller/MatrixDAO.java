package mx.com.ccplus.omr.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import mx.com.ccplus.omr.model.Coordinate;
import mx.com.ccplus.omr.model.Matrix;
import mx.com.ccplus.omr.model.Template;
import mx.com.ccplus.omr.view.viewer.model.RegMatrix;

public class MatrixDAO {
    
    private final DecimalFormat df = new DecimalFormat("#.00");
    
    public Coordinate[][] getCoordinates(Matrix matrix){
        
        double deltaX = (double) (matrix.getEndingCoordinate().getX() - matrix.getStartingCoordinate().getX());
        double deltaY = (double) (matrix.getEndingCoordinate().getY() - matrix.getStartingCoordinate().getY());
        
        double stepX = deltaX / (double) (matrix.getColumns() - 1);
        double stepY = deltaY / (double) (matrix.getRows() - 1);
        
        Coordinate[][] coordinateArray = new Coordinate[matrix.getColumns()][matrix.getRows()];

        for (int i = 0; i < coordinateArray.length; i++) {
            for (int j = 0; j < coordinateArray[0].length; j++) {
                Coordinate c = new Coordinate();

                double cX = ((double) matrix.getStartingCoordinate().getX()) + (stepX * (double) i) + 0.5;
                c.setX((int) cX);
                double cY = ((double) matrix.getStartingCoordinate().getY()) + (stepY * (double) j) + 0.5;
                c.setY((int) cY);

                coordinateArray[i][j] = c;
            }
        }
        
        return coordinateArray;
        
    }
    
    public Coordinate transformMouseCoordinateToImageLocation(Coordinate mouseCoordinate, ImageView imageView, Image image){
        double cursor_width = mouseCoordinate.getX();
        double cursor_height = mouseCoordinate.getY();

        double node_height = imageView.getBoundsInParent().getHeight();
        double node_width = imageView.getBoundsInParent().getWidth();

        double image_height = image.getHeight();
        double image_width = image.getWidth();

        double d1 = (cursor_width/node_width)*image_width;
        double d2 = (cursor_height/node_height)*image_height;
        
        return new Coordinate(d1, d2);
    }
    
    public Coordinate transformImageLocationToMouseCoordinate(Coordinate location, ImageView imageView, Image image){
        double node_height = imageView.getBoundsInParent().getHeight();
        double node_width = imageView.getBoundsInParent().getWidth();

        double image_height = image.getHeight();
        double image_width = image.getWidth();

        double d1 = (node_width/image_width)*location.getX();
        double d2 = (node_height/image_height)*location.getY();
        
        return new Coordinate(d1, d2);
    }
    
    public int transformCursorDiameterToBubbleDiameter(int diameter, ImageView imageView, Image image){
        double node_height = imageView.getBoundsInParent().getHeight();
        double image_height = image.getHeight();
        //for images wider than larger width must be used instead of height
        return (int) ( ((double) (image_height/node_height)) * diameter);
    }
    
    public int transformBubbleDiameterToCursorDiameter(int diameter, ImageView imageView, Image image){
        double node_heigth = imageView.getBoundsInParent().getWidth();
        double image_heigth = image.getWidth();
        //for images wider than larger width must be used instead of height
        return (int) ( ((double) (node_heigth/image_heigth)) * diameter);
    }
    
    public Matrix transformRealMatrixToNodeMatrix(Matrix matrix, ImageView imageView, Image image){
        return new Matrix("dummy", matrix.getColumns(), matrix.getRows(), transformBubbleDiameterToCursorDiameter(matrix.getDiameter(), imageView, image), matrix.getHue(), transformImageLocationToMouseCoordinate(matrix.getStartingCoordinate(), imageView, image), transformImageLocationToMouseCoordinate(matrix.getEndingCoordinate(), imageView, image));
    }
    
    public ArrayList<RegMatrix> transformMatrixArrayToRegArray(Template template){
        ArrayList<RegMatrix> res = new ArrayList<RegMatrix>();
        
        for (int i = 0; i < template.getMatrixes().size(); i++) {
            Matrix m = template.getMatrixes().get(i);
            res.add(new RegMatrix(Integer.toString(i+1), m.getName(), df.format(m.getHue()), Integer.toString(m.getColumns()), Integer.toString(m.getRows()), df.format(m.getStartingCoordinate().getX()) + ", " + df.format(m.getStartingCoordinate().getY()), df.format(m.getEndingCoordinate().getX()) + ", " + df.format(m.getEndingCoordinate().getX())));
        }
        
        return res;
    }
    
    
}
