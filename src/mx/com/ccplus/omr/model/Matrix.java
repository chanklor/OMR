package mx.com.ccplus.omr.model;

public class Matrix {
    private int columns;
    private int rows;
    private final int diameter;
    private final Coordinate[][] coordinates = new Coordinate[columns][rows];

    public Matrix(int columns, int rows, int diameter) {
        this.columns = columns;
        this.rows = rows;
        this.diameter = diameter;
    }
    
    
    
}
