package mx.com.ccplus.omr.model;

import java.util.ArrayList;

public class Template {
    
    private String name;
    private ArrayList<Matrix> matrixes = new ArrayList<Matrix>();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Matrix> getMatrixes() {
        return matrixes;
    }

    @Override
    public String toString() {
        return "Template{" + "name=" + name + ", matrixes=" + matrixes + '}';
    }
    
    
}
