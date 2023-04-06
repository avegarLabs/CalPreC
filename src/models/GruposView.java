package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GruposView extends RecursiveTreeObject<GruposView> {

    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty controltotal;
    private StringProperty insertar;
    private StringProperty modificar;
    private StringProperty eliminar;
    private StringProperty visualizar;


    public GruposView(Integer id, String nombre, String controltotal, String insertar, String modificar, String eliminar, String visualizar) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.controltotal = new SimpleStringProperty(controltotal);
        this.insertar = new SimpleStringProperty(insertar);
        this.modificar = new SimpleStringProperty(modificar);
        this.eliminar = new SimpleStringProperty(eliminar);
        this.visualizar = new SimpleStringProperty(visualizar);
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

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getControltotal() {
        return controltotal.get();
    }

    public void setControltotal(String controltotal) {
        this.controltotal.set(controltotal);
    }

    public StringProperty controltotalProperty() {
        return controltotal;
    }

    public String getInsertar() {
        return insertar.get();
    }

    public void setInsertar(String insertar) {
        this.insertar.set(insertar);
    }

    public StringProperty insertarProperty() {
        return insertar;
    }

    public String getModificar() {
        return modificar.get();
    }

    public void setModificar(String modificar) {
        this.modificar.set(modificar);
    }

    public StringProperty modificarProperty() {
        return modificar;
    }

    public String getEliminar() {
        return eliminar.get();
    }

    public void setEliminar(String eliminar) {
        this.eliminar.set(eliminar);
    }

    public StringProperty eliminarProperty() {
        return eliminar;
    }

    public String getVisualizar() {
        return visualizar.get();
    }

    public void setVisualizar(String visualizar) {
        this.visualizar.set(visualizar);
    }

    public StringProperty visualizarProperty() {
        return visualizar;
    }
}
