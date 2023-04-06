package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubDuplicateModel extends RecursiveTreeObject<SubDuplicateModel> {
    private IntegerProperty integerProperty;
    private JFXCheckBox datosBox;
    private StringProperty descripcion;


    public SubDuplicateModel(int idProp, JFXCheckBox datosBox, String descripcion) {
        this.integerProperty = new SimpleIntegerProperty(idProp);
        this.datosBox = datosBox;
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public int getIntegerProperty() {
        return integerProperty.get();
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty.set(integerProperty);
    }

    public IntegerProperty integerPropertyProperty() {
        return integerProperty;
    }

    public JFXCheckBox getDatosBox() {
        return datosBox;
    }

    public void setDatosBox(JFXCheckBox datosBox) {
        this.datosBox = datosBox;
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
