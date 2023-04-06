package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewObrasFormTable extends RecursiveTreeObject<NewObrasFormTable> {

    public IntegerProperty id;
    public JFXCheckBox empresa;
    public StringProperty descripcion;


    public NewObrasFormTable(int id, JFXCheckBox empresa, String descripcion) {
        this.id = new SimpleIntegerProperty(id);
        this.empresa = empresa;
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public JFXCheckBox getEmpresa() {
        return empresa;
    }

    public void setEmpresa(JFXCheckBox empresa) {
        this.empresa = empresa;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }
}
