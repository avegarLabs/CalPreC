package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UniddaObraToIMportView extends RecursiveTreeObject<UniddaObraToIMportView> {

    private IntegerProperty id;
    private JFXCheckBox codigo;
    private StringProperty descripcion;
    private IntegerProperty idObra;
    private IntegerProperty idEmpresa;

    public UniddaObraToIMportView(int id, JFXCheckBox codigo, String descripcion, int obraId, int empresaconstructoraId) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = codigo;
        this.descripcion = new SimpleStringProperty(descripcion);
        this.idObra = new SimpleIntegerProperty(obraId);
        this.idEmpresa = new SimpleIntegerProperty(empresaconstructoraId);

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

    public JFXCheckBox getCodigo() {
        return codigo;
    }

    public void setCodigo(JFXCheckBox codigo) {
        this.codigo = codigo;
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

    public int getIdObra() {
        return idObra.get();
    }

    public void setIdObra(int idObra) {
        this.idObra.set(idObra);
    }

    public IntegerProperty idObraProperty() {
        return idObra;
    }

    public int getIdEmpresa() {
        return idEmpresa.get();
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa.set(idEmpresa);
    }

    public IntegerProperty idEmpresaProperty() {
        return idEmpresa;
    }
}
