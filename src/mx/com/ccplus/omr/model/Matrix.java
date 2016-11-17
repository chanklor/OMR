package mx.com.ccplus.omr.model;

public class Matrix {
    private String name;
    private int columns;
    private int rows;
    private int diameter;
    private Coordinate[][] coordinates;

    public Matrix(String name, int columns, int rows, int diameter) {
        this.name = name;
        this.columns = columns;
        this.rows = rows;
        this.diameter = diameter;
        
        this.coordinates = new Coordinate[columns][rows];
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates[i].length; j++) {
                coordinates[i][j] = new Coordinate();
            }
        }
        
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

    public Coordinate[][] getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Matrix{" + "name=" + name + ", columns=" + columns + ", rows=" + rows + ", diameter=" + diameter + ", coordinates=" + coordinates + '}';
    }
    
    
    
}
