package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObrasToImportSuministros extends RecursiveTreeObject<ObrasToImportSuministros> {

    public IntegerProperty id;
    public JFXCheckBox obra;
    public StringProperty descripcion;


    public ObrasToImportSuministros(int id, JFXCheckBox obra, String descripcion) {
        this.id = new SimpleIntegerProperty(id);
        this.obra = obra;
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

    public JFXCheckBox getObra() {
        return obra;
    }

    public void setObra(JFXCheckBox obra) {
        this.obra = obra;
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
