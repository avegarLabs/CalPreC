package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuariosView extends RecursiveTreeObject<UsuariosView> {

    private IntegerProperty id;
    private StringProperty usuario;
    private StringProperty nombre;
    private StringProperty cargo;
    private StringProperty dpto;
    private StringProperty grupo;


    public UsuariosView(Integer id, String usuario, String nombre, String cargo, String dpto, String grupo) {
        this.id = new SimpleIntegerProperty(id);
        this.usuario = new SimpleStringProperty(usuario);
        this.nombre = new SimpleStringProperty(nombre);
        this.cargo = new SimpleStringProperty(cargo);
        this.dpto = new SimpleStringProperty(dpto);
        this.grupo = new SimpleStringProperty(grupo);
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

    public String getUsuario() {
        return usuario.get();
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    public StringProperty usuarioProperty() {
        return usuario;
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

    public String getCargo() {
        return cargo.get();
    }

    public void setCargo(String cargo) {
        this.cargo.set(cargo);
    }

    public StringProperty cargoProperty() {
        return cargo;
    }

    public String getDpto() {
        return dpto.get();
    }

    public void setDpto(String dpto) {
        this.dpto.set(dpto);
    }

    public StringProperty dptoProperty() {
        return dpto;
    }

    public String getGrupo() {
        return grupo.get();
    }

    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }

    public StringProperty grupoProperty() {
        return grupo;
    }
}
