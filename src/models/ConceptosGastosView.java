package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class ConceptosGastosView extends RecursiveTreeObject<ConceptosGastosView> {

    private IntegerProperty id;
    private StringProperty code;
    private StringProperty descripcion;
    private DoubleProperty coeficiente;
    private StringProperty formula;
    private DoubleProperty porciento;
    private JFXCheckBox calc;
    private IntegerProperty pertenece;


    public ConceptosGastosView(int id, String code, String descripcion, double coeficiente, String formula, double porciento, JFXCheckBox calc, int pertenece) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.coeficiente = new SimpleDoubleProperty(coeficiente);
        this.formula = new SimpleStringProperty(formula);
        this.porciento = new SimpleDoubleProperty(porciento);
        this.calc = calc;
        this.pertenece = new SimpleIntegerProperty(pertenece);

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

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
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

    public double getCoeficiente() {
        return coeficiente.get();
    }

    public void setCoeficiente(double coeficiente) {
        this.coeficiente.set(coeficiente);
    }

    public DoubleProperty coeficienteProperty() {
        return coeficiente;
    }

    public String getFormula() {
        return formula.get();
    }

    public void setFormula(String formula) {
        this.formula.set(formula);
    }

    public StringProperty formulaProperty() {
        return formula;
    }

    public double getPorciento() {
        return porciento.get();
    }

    public void setPorciento(double porciento) {
        this.porciento.set(porciento);
    }

    public DoubleProperty porcientoProperty() {
        return porciento;
    }

    public JFXCheckBox getCalc() {
        return calc;
    }

    public void setCalc(JFXCheckBox calc) {
        this.calc = calc;
    }

    public int getPertenece() {
        return pertenece.get();
    }

    public void setPertenece(int pertenece) {
        this.pertenece.set(pertenece);
    }

    public IntegerProperty perteneceProperty() {
        return pertenece;
    }
}
