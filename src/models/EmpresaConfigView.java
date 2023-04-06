package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpresaConfigView extends RecursiveTreeObject<EmpresaConfigView> {

    private IntegerProperty id;
    private StringProperty reup;
    private StringProperty nombre;
    private StringProperty comercial;


    public EmpresaConfigView(Integer id, String reup, String nombre, String comercial) {
        this.id = new SimpleIntegerProperty(id);
        this.reup = new SimpleStringProperty(reup);
        this.nombre = new SimpleStringProperty(nombre);
        this.comercial = new SimpleStringProperty(comercial);
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

    public String getReup() {
        return reup.get();
    }

    public void setReup(String reup) {
        this.reup.set(reup);
    }

    public StringProperty reupProperty() {
        return reup;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getComercial() {
        return comercial.get();
    }

    public void setComercial(String comercial) {
        this.comercial.set(comercial);
    }

    public StringProperty comercialProperty() {
        return comercial;
    }
}
