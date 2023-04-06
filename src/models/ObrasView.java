package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObrasView extends RecursiveTreeObject<ObrasView> {

    private IntegerProperty id;
    private StringProperty codigo;
    private StringProperty tipo;
    private StringProperty descripion;

    public ObrasView(int id, String codigo, String tipo, String descripion) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.tipo = new SimpleStringProperty(tipo);
        this.descripion = new SimpleStringProperty(descripion);
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

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public String getDescripion() {
        return descripion.get();
    }

    public void setDescripion(String descripion) {
        this.descripion.set(descripion);
    }

    public StringProperty descripionProperty() {
        return descripion;
    }
}
