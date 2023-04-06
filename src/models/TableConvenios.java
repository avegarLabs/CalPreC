package models;

public class TableConvenios {

    public Integer idCuad;
    public String codeInt;
    public String fullName;
    public String th;
    public String hp;
    public String firma;
    public String hr;
    public String cpl;
    public String firma2;
    public String observaciones;

    public TableConvenios(Integer idCuad, String codeInt, String fullName, String th, String hp, String firma, String hr, String cpl, String firma2, String observaciones) {
        this.idCuad = idCuad;
        this.codeInt = codeInt;
        this.fullName = fullName;
        this.th = th;
        this.hp = hp;
        this.firma = firma;
        this.hr = hr;
        this.cpl = cpl;
        this.firma2 = firma2;
        this.observaciones = observaciones;
    }

    public Integer getIdCuad() {
        return idCuad;
    }

    public void setIdCuad(Integer idCuad) {
        this.idCuad = idCuad;
    }

    public String getCodeInt() {
        return codeInt;
    }

    public void setCodeInt(String codeInt) {
        this.codeInt = codeInt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getCpl() {
        return cpl;
    }

    public void setCpl(String cpl) {
        this.cpl = cpl;
    }

    public String getFirma2() {
        return firma2;
    }

    public void setFirma2(String firma2) {
        this.firma2 = firma2;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
