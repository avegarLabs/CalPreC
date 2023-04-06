package models;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class SuministroPropioView extends RecursiveTreeObject<SuministroPropioView> {

    public IntegerProperty idp;
    public StringProperty codigop;
    public StringProperty descripcionp;
    public StringProperty ump;
    public DoubleProperty pesop;
    public DoubleProperty preciomnp;
    public DoubleProperty preciomlcp;
    public DoubleProperty merprodp;
    public DoubleProperty mertranspp;
    public DoubleProperty mermanipp;


    public SuministroPropioView(int id, String codigo, String descripcion, String um, double peso, double preciomn, double preciomlc, double merprod, double mertransp, double mermanip) {
        this.idp = new SimpleIntegerProperty(id);
        this.codigop = new SimpleStringProperty(codigo);

        this.descripcionp = new SimpleStringProperty(descripcion);
        this.ump = new SimpleStringProperty(um);
        this.pesop = new SimpleDoubleProperty(peso);
        this.preciomnp = new SimpleDoubleProperty(preciomn);
        this.preciomlcp = new SimpleDoubleProperty(preciomlc);
        this.merprodp = new SimpleDoubleProperty(merprod);
        this.mertranspp = new SimpleDoubleProperty(mertransp);
        this.mermanipp = new SimpleDoubleProperty(mermanip);


    }

    public int getIdp() {
        return idp.get();
    }

    public void setIdp(int idp) {
        this.idp.set(idp);
    }

    public IntegerProperty idpProperty() {
        return idp;
    }

    public String getCodigop() {
        return codigop.get();
    }

    public void setCodigop(String codigop) {
        this.codigop.set(codigop);
    }

    public StringProperty codigopProperty() {
        return codigop;
    }

    public String getDescripcionp() {
        return descripcionp.get();
    }

    public void setDescripcionp(String descripcionp) {
        this.descripcionp.set(descripcionp);
    }

    public StringProperty descripcionpProperty() {
        return descripcionp;
    }

    public String getUmp() {
        return ump.get();
    }

    public void setUmp(String ump) {
        this.ump.set(ump);
    }

    public StringProperty umpProperty() {
        return ump;
    }

    public double getPesop() {
        return pesop.get();
    }

    public void setPesop(double pesop) {
        this.pesop.set(pesop);
    }

    public DoubleProperty pesopProperty() {
        return pesop;
    }

    public double getPreciomnp() {
        return preciomnp.get();
    }

    public void setPreciomnp(double preciomnp) {
        this.preciomnp.set(preciomnp);
    }

    public DoubleProperty preciomnpProperty() {
        return preciomnp;
    }

    public double getPreciomlcp() {
        return preciomlcp.get();
    }

    public void setPreciomlcp(double preciomlcp) {
        this.preciomlcp.set(preciomlcp);
    }

    public DoubleProperty preciomlcpProperty() {
        return preciomlcp;
    }

    public double getMerprodp() {
        return merprodp.get();
    }

    public void setMerprodp(double merprodp) {
        this.merprodp.set(merprodp);
    }

    public DoubleProperty merprodpProperty() {
        return merprodp;
    }

    public double getMertranspp() {
        return mertranspp.get();
    }

    public void setMertranspp(double mertranspp) {
        this.mertranspp.set(mertranspp);
    }

    public DoubleProperty mertransppProperty() {
        return mertranspp;
    }

    public double getMermanipp() {
        return mermanipp.get();
    }

    public void setMermanipp(double mermanipp) {
        this.mermanipp.set(mermanipp);
    }

    public DoubleProperty mermanippProperty() {
        return mermanipp;
    }
}
