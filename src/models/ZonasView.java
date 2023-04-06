package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ZonasView extends RecursiveTreeObject<ZonasView> {

    private IntegerProperty id;
    private StringProperty codigo;
    private StringProperty desripcion;


    public ZonasView(int id, String codigo, String desripcion) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.desripcion = new SimpleStringProperty(desripcion);
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

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public String getDesripcion() {
        return desripcion.get();
    }

    public void setDesripcion(String desripcion) {
        this.desripcion.set(desripcion);
    }

    public StringProperty desripcionProperty() {
        return desripcion;
    }
}
