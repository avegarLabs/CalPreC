package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MemoriaView extends RecursiveTreeObject<MemoriaView> {

    public IntegerProperty id;
    public StringProperty titulo;
    public StringProperty texto;

    public MemoriaView(Integer id, String titulo, String texto) {
        this.id = new SimpleIntegerProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.texto = new SimpleStringProperty(texto);
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

    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public String getTexto() {
        return texto.get();
    }

    public void setTexto(String texto) {
        this.texto.set(texto);
    }

    public StringProperty textoProperty() {
        return texto;
    }
}
