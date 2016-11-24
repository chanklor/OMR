package mx.com.ccplus.omr.model;

public class Matrix {
    private String name;
    private int columns;
    private int rows;
    private int diameter;
    private double hue;
    private Coordinate startingCoordinate;
    private Coordinate endingCoordinate;

    public Matrix(String name, int columns, int rows, int diameter, double hue, Coordinate startingCoordinate, Coordinate endingCoordinate) {
        this.name = name;
        this.columns = columns;
        this.rows = rows;
        this.diameter = diameter;
        this.hue = hue;
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

    public double getHue() {
        return hue;
    }

    public Coordinate getStartingCoordinate() {
        return startingCoordinate;
    }

    public Coordinate getEndingCoordinate() {
        return endingCoordinate;
    }

    @Override
    public String toString() {
        return "Matrix{" + "name=" + name + ", columns=" + columns + ", rows=" + rows + ", diameter=" + diameter + ", hue=" + hue + ", startingCoordinate=" + startingCoordinate + ", endingCoordinate=" + endingCoordinate + '}';
    }


    
    
 
    
    
    
}
