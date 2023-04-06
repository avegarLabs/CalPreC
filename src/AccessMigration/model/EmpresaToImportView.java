package AccessMigration.model;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpresaToImportView {

    public IntegerProperty id;
    public JFXCheckBox code;
    public StringProperty descrip;
    public StringProperty simil;


    public EmpresaToImportView(int id, JFXCheckBox code, String descrip, String simil) {
        this.id = new SimpleIntegerProperty(id);
        this.code = code;
        this.descrip = new SimpleStringProperty(descrip);
        this.simil = new SimpleStringProperty(simil);
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

    public JFXCheckBox getCode() {
        return code;
    }

    public void setCode(JFXCheckBox code) {
        this.code = code;
    }

    public String getDescrip() {
        return descrip.get();
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }

    public StringProperty descripProperty() {
        return descrip;
    }

    public String getSimil() {
        return simil.get();
    }

    public void setSimil(String simil) {
        this.simil.set(simil);
    }

    public StringProperty similProperty() {
        return simil;
    }
}
