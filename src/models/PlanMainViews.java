package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class PlanMainViews extends RecursiveTreeObject<PlanMainViews> {
    private IntegerProperty id;
    private StringProperty code;
    private StringProperty descrip;
    private StringProperty um;
    private DoubleProperty presp;
    private String plan;
    private DoubleProperty certf;
    private DoubleProperty disp;

    public PlanMainViews(int id, String code, String descrip, String um, Double presp, String plan, Double certf, Double disp) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.descrip = new SimpleStringProperty(descrip);
        this.um = new SimpleStringProperty(um);
        this.presp = new SimpleDoubleProperty(presp);
        this.plan = plan;
        this.certf = new SimpleDoubleProperty(certf);
        this.disp = new SimpleDoubleProperty(disp);
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

    public String getDescrip() {
        return descrip.get();
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }

    public StringProperty descripProperty() {
        return descrip;
    }

    public String getUm() {
        return um.get();
    }

    public void setUm(String um) {
        this.um.set(um);
    }

    public StringProperty umProperty() {
        return um;
    }

    public double getPresp() {
        return presp.get();
    }

    public void setPresp(double presp) {
        this.presp.set(presp);
    }

    public DoubleProperty prespProperty() {
        return presp;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public double getCertf() {
        return certf.get();
    }

    public void setCertf(double certf) {
        this.certf.set(certf);
    }

    public DoubleProperty certfProperty() {
        return certf;
    }

    public double getDisp() {
        return disp.get();
    }

    public void setDisp(double disp) {
        this.disp.set(disp);
    }

    public DoubleProperty dispProperty() {
        return disp;
    }
}
