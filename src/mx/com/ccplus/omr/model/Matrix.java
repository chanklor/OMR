package mx.com.ccplus.omr.model;

import java.util.Arrays;

public class Matrix {
    private String name;
    private int columns;
    private int rows;
    private int diameter;
    private Coordinate startingCoordinate;
    private Coordinate endingCoordinate;

    public Matrix(String name, int columns, int rows, int diameter, Coordinate startingCoordinate, Coordinate endingCoordinate) {
        this.name = name;
        this.columns = columns;
        this.rows = rows;
        this.diameter = diameter;
        this.startingCoordinate = startingCoordinate;
        this.endingCoordinate = endingCoordinate;
    }

    public String getName() {
        return name;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getDiameter() {
        return diameter;
    }

    public Coordinate getStartingCoordinate() {
        return startingCoordinate;
    }

    public Coordinate getEndingCoordinate() {
        return endingCoordinate;
    }

    @Override
    public String toString() {
        return "Matrix{" + "name=" + name + ", columns=" + columns + ", rows=" + rows + ", diameter=" + diameter + ", startingCoordinate=" + startingCoordinate + ", endingCoordinate=" + endingCoordinate + '}';
    }
    
    public Coordinate[][] getCoordinates(){
        
        double deltaX = (double) (endingCoordinate.getX() - startingCoordinate.getX());
        double deltaY = (double) (endingCoordinate.getY() - startingCoordinate.getY());
        
        double stepX = deltaX / (double) (this.columns - 1);
        double stepY = deltaY / (double) (this.rows - 1);
        
        Coordinate[][] coordinateArray = new Coordinate[this.columns][this.rows];

        for (int i = 0; i < coordinateArray.length; i++) {
            for (int j = 0; j < coordinateArray[0].length; j++) {
                Coordinate c = new Coordinate();

                double cX = ((double) startingCoordinate.getX()) + (stepX * (double) i) + 0.5;
                c.setX((int) cX);
                double cY = ((double) startingCoordinate.getY()) + (stepY * (double) j) + 0.5;
                c.setY((int) cY);

                coordinateArray[i][j] = c;
            }
        }
        
        return coordinateArray;
        
    }
 
    
    
    
}
