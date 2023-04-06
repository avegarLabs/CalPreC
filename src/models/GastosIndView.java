package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class GastosIndView extends RecursiveTreeObject<GastosIndView> {

    public StringProperty code;
    public StringProperty concepto;
    public DoubleProperty coeficiente;
    public StringProperty valor;
    public IntegerProperty idEmpresa;
    public IntegerProperty idObra;
    public IntegerProperty idConcepto;

    public GastosIndView(String code, String concepto, double coeficiente, String valor, int idEmpresa, int idObra, int idConcepro) {
        this.code = new SimpleStringProperty(code);
        this.concepto = new SimpleStringProperty(concepto);
        this.coeficiente = new SimpleDoubleProperty(coeficiente);
        this.valor = new SimpleStringProperty(valor);
        this.idEmpresa = new SimpleIntegerProperty(idEmpresa);
        this.idObra = new SimpleIntegerProperty(idObra);
        this.idConcepto = new SimpleIntegerProperty(idConcepro);

    }

    public String getConcepto() {
        return concepto.get();
    }

    public void setConcepto(String concepto) {
        this.concepto.set(concepto);
    }

    public StringProperty conceptoProperty() {
        return concepto;
    }

    public double getCoeficiente() {
        return coeficiente.get();
    }

    public void setCoeficiente(double coeficiente) {
        this.coeficiente.set(coeficiente);
    }

    public DoubleProperty coeficienteProperty() {
        return coeficiente;
    }

    public String getValor() {
        return valor.get();
    }

    public void setValor(String valor) {
        this.valor.set(valor);
    }

    public StringProperty valorProperty() {
        return valor;
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

    public int getIdObra() {
        return idObra.get();
    }

    public void setIdObra(int idObra) {
        this.idObra.set(idObra);
    }

    public IntegerProperty idObraProperty() {
        return idObra;
    }

    public int getIdConcepto() {
        return idConcepto.get();
    }

    public void setIdConcepto(int idConcepto) {
        this.idConcepto.set(idConcepto);
    }

    public IntegerProperty idConceptoProperty() {
        return idConcepto;
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }
}
