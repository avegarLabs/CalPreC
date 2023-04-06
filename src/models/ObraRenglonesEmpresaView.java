package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObraRenglonesEmpresaView extends RecursiveTreeObject<ObraRenglonesEmpresaView> {

    //ec.codigo, ec.descripcion, rv.codigo, rv.descripcion, uor.cantRv, uor.costMat, uor.costMano, uor.costEquip
    public StringProperty ecCodigo;
    public StringProperty ecDescripcion;
    public StringProperty rvCodigo;
    public StringProperty rvDescrip;
    public StringProperty rvUM;
    public DoubleProperty rvCant;
    public DoubleProperty rvMat;
    public DoubleProperty rvMano;
    public DoubleProperty rvEquipo;
    public DoubleProperty rvTotal;

    public ObraRenglonesEmpresaView(String ecCodigo, String ecDescripcion, String rvCodigo, String rvDescrip, String rvUM, Double rvCant, Double rvMat, Double rvMano, Double rvEquipo, Double rvTotal) {
        this.ecCodigo = new SimpleStringProperty(ecCodigo);
        this.ecDescripcion = new SimpleStringProperty(ecDescripcion);
        this.rvCodigo = new SimpleStringProperty(rvCodigo);
        this.rvDescrip = new SimpleStringProperty(rvDescrip);
        this.rvUM = new SimpleStringProperty(rvUM);
        this.rvCant = new SimpleDoubleProperty(rvCant);
        this.rvMat = new SimpleDoubleProperty(rvMat);
        this.rvMano = new SimpleDoubleProperty(rvMano);
        this.rvEquipo = new SimpleDoubleProperty(rvEquipo);
        this.rvTotal = new SimpleDoubleProperty(rvTotal);
    }

    public String getEcCodigo() {
        return ecCodigo.get();
    }

    public void setEcCodigo(String ecCodigo) {
        this.ecCodigo.set(ecCodigo);
    }

    public StringProperty ecCodigoProperty() {
        return ecCodigo;
    }

    public String getEcDescripcion() {
        return ecDescripcion.get();
    }

    public void setEcDescripcion(String ecDescripcion) {
        this.ecDescripcion.set(ecDescripcion);
    }

    public StringProperty ecDescripcionProperty() {
        return ecDescripcion;
    }

    public String getRvCodigo() {
        return rvCodigo.get();
    }

    public void setRvCodigo(String rvCodigo) {
        this.rvCodigo.set(rvCodigo);
    }

    public StringProperty rvCodigoProperty() {
        return rvCodigo;
    }

    public String getRvDescrip() {
        return rvDescrip.get();
    }

    public void setRvDescrip(String rvDescrip) {
        this.rvDescrip.set(rvDescrip);
    }

    public StringProperty rvDescripProperty() {
        return rvDescrip;
    }

    public String getRvUM() {
        return rvUM.get();
    }

    public void setRvUM(String rvUM) {
        this.rvUM.set(rvUM);
    }

    public StringProperty rvUMProperty() {
        return rvUM;
    }

    public double getRvCant() {
        return rvCant.get();
    }

    public void setRvCant(double rvCant) {
        this.rvCant.set(rvCant);
    }

    public DoubleProperty rvCantProperty() {
        return rvCant;
    }

    public double getRvMat() {
        return rvMat.get();
    }

    public void setRvMat(double rvMat) {
        this.rvMat.set(rvMat);
    }

    public DoubleProperty rvMatProperty() {
        return rvMat;
    }

    public double getRvMano() {
        return rvMano.get();
    }

    public void setRvMano(double rvMano) {
        this.rvMano.set(rvMano);
    }

    public DoubleProperty rvManoProperty() {
        return rvMano;
    }

    public double getRvEquipo() {
        return rvEquipo.get();
    }

    public void setRvEquipo(double rvEquipo) {
        this.rvEquipo.set(rvEquipo);
    }

    public DoubleProperty rvEquipoProperty() {
        return rvEquipo;
    }

    public double getRvTotal() {
        return rvTotal.get();
    }

    public void setRvTotal(double rvTotal) {
        this.rvTotal.set(rvTotal);
    }

    public DoubleProperty rvTotalProperty() {
        return rvTotal;
    }
}
