package AccessMigration.model;

import java.util.ArrayList;

public class RVCreateModel {

    public String SubGrp;
    public String RV;
    public String Descripn;
    public String UM;
    public String CostoMat;
    public String CostoMan;
    public String CostoEq;
    public String Peso;
    public String HH;
    public String HO;
    public String HA;
    public String HE;
    public String Cemento;
    public String Arido;

    public ArrayList<rvComposition> rvCompositionArrayList;

    public RVCreateModel(String subGrp, String RV, String descripn, String UM, String costoMat, String costoMan, String costoEq, String peso, String HH, String HO, String HA, String HE, String cemento, String arido, ArrayList<rvComposition> rvCompositionArrayList) {
        SubGrp = subGrp;
        this.RV = RV;
        Descripn = descripn;
        this.UM = UM;
        CostoMat = costoMat;
        CostoMan = costoMan;
        CostoEq = costoEq;
        Peso = peso;
        this.HH = HH;
        this.HO = HO;
        this.HA = HA;
        this.HE = HE;
        Cemento = cemento;
        Arido = arido;
        this.rvCompositionArrayList = rvCompositionArrayList;
    }

    public String getSubGrp() {
        return SubGrp;
    }

    public void setSubGrp(String subGrp) {
        SubGrp = subGrp;
    }

    public String getRV() {
        return RV;
    }

    public void setRV(String RV) {
        this.RV = RV;
    }

    public String getDescripn() {
        return Descripn;
    }

    public void setDescripn(String descripn) {
        Descripn = descripn;
    }

    public String getUM() {
        return UM;
    }

    public void setUM(String UM) {
        this.UM = UM;
    }

    public String getCostoMat() {
        return CostoMat;
    }

    public void setCostoMat(String costoMat) {
        CostoMat = costoMat;
    }

    public String getCostoMan() {
        return CostoMan;
    }

    public void setCostoMan(String costoMan) {
        CostoMan = costoMan;
    }

    public String getCostoEq() {
        return CostoEq;
    }

    public void setCostoEq(String costoEq) {
        CostoEq = costoEq;
    }

    public String getPeso() {
        return Peso;
    }

    public void setPeso(String peso) {
        Peso = peso;
    }

    public String getHH() {
        return HH;
    }

    public void setHH(String HH) {
        this.HH = HH;
    }

    public String getHO() {
        return HO;
    }

    public void setHO(String HO) {
        this.HO = HO;
    }

    public String getHA() {
        return HA;
    }

    public void setHA(String HA) {
        this.HA = HA;
    }

    public String getHE() {
        return HE;
    }

    public void setHE(String HE) {
        this.HE = HE;
    }

    public String getCemento() {
        return Cemento;
    }

    public void setCemento(String cemento) {
        Cemento = cemento;
    }

    public String getArido() {
        return Arido;
    }

    public void setArido(String arido) {
        Arido = arido;
    }

    public ArrayList<rvComposition> getRvCompositionArrayList() {
        return rvCompositionArrayList;
    }

    public void setRvCompositionArrayList(ArrayList<rvComposition> rvCompositionArrayList) {
        this.rvCompositionArrayList = rvCompositionArrayList;
    }
}
