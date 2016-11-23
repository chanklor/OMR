package mx.com.ccplus.omr.view.viewer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RegMatrix {
    
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty color;
    private final StringProperty columns;
    private final StringProperty rows;
    private final StringProperty starting;
    private final StringProperty ending;

    public RegMatrix(String id, String name, String color, String columns, String rows, String starting, String ending) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.color = new SimpleStringProperty(color);
        this.columns = new SimpleStringProperty(columns);
        this.rows = new SimpleStringProperty(rows);
        this.starting = new SimpleStringProperty(starting);
        this.ending = new SimpleStringProperty(ending);
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getColor() {
        return color.get();
    }

    public String getColumns() {
        return columns.get();
    }

    public String getRows() {
        return rows.get();
    }

    public String getStarting() {
        return starting.get();
    }

    public String getEnding() {
        return ending.get();
    }
    
    
    
    
    
}
