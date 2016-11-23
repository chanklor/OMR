/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ccplus.omr.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import mx.com.ccplus.omr.model.Coordinate;
import mx.com.ccplus.omr.model.Matrix;

/**
 *
 * @author 47385
 */
public class MatrixDAO {
    
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
    
    public Coordinate transformMouseCoordinateToImageLocation(MouseEvent event, ImageView imageView, Image image){
        double cursor_width = event.getX();
        double cursor_height = event.getY();

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
    
}
