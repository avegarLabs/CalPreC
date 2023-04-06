package models;

public class UOPlanCertConvModel {
    public int idBrigada;
    public int idGrupo;
    public int idCuadrilla;
    public String uoCode;
    public String uoDescrip;
    public Double plnVolumen;
    public Double plnHoras;
    public Double plnSalario;
    public Double plnSalarioInc;
    public Double plnProduccion;
    public Double crtVolumen;
    public Double crtHoras;
    public Double crtSalario;
    public Double crtSalarioInc;
    public Double crtProduccion;
    public String aprobado;

    public UOPlanCertConvModel(int idBrigada, int idGrupo, int idCuadrilla, String uoCode, String uoDescrip, Double plnVolumen, Double plnHoras, Double plnSalario, Double plnSalarioInc, Double plnProduccion, Double crtVolumen, Double crtHoras, Double crtSalario, Double crtSalarioInc, Double crtProduccion, String aprobado) {
        this.idBrigada = idBrigada;
        this.idGrupo = idGrupo;
        this.idCuadrilla = idCuadrilla;
        this.uoCode = uoCode;
        this.uoDescrip = uoDescrip;
        this.plnVolumen = plnVolumen;
        this.plnHoras = plnHoras;
        this.plnSalario = plnSalario;
        this.plnSalarioInc = plnSalarioInc;
        this.plnProduccion = plnProduccion;
        this.crtVolumen = crtVolumen;
        this.crtHoras = crtHoras;
        this.crtSalario = crtSalario;
        this.crtSalarioInc = crtSalarioInc;
        this.crtProduccion = crtProduccion;
        this.aprobado = aprobado;
    }

    public int getIdBrigada() {
        return idBrigada;
    }

    public void setIdBrigada(int idBrigada) {
        this.idBrigada = idBrigada;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdCuadrilla() {
        return idCuadrilla;
    }

    public void setIdCuadrilla(int idCuadrilla) {
        this.idCuadrilla = idCuadrilla;
    }

    public String getUoCode() {
        return uoCode;
    }

    public void setUoCode(String uoCode) {
        this.uoCode = uoCode;
    }

    public String getUoDescrip() {
        return uoDescrip;
    }

    public void setUoDescrip(String uoDescrip) {
        this.uoDescrip = uoDescrip;
    }

    public Double getPlnVolumen() {
        return plnVolumen;
    }

    public void setPlnVolumen(Double plnVolumen) {
        this.plnVolumen = plnVolumen;
    }

    public Double getPlnHoras() {
        return plnHoras;
    }

    public void setPlnHoras(Double plnHoras) {
        this.plnHoras = plnHoras;
    }

    public Double getPlnSalario() {
        return plnSalario;
    }

    public void setPlnSalario(Double plnSalario) {
        this.plnSalario = plnSalario;
    }

    public Double getPlnSalarioInc() {
        return plnSalarioInc;
    }

    public void setPlnSalarioInc(Double plnSalarioInc) {
        this.plnSalarioInc = plnSalarioInc;
    }

    public Double getPlnProduccion() {
        return plnProduccion;
    }

    public void setPlnProduccion(Double plnProduccion) {
        this.plnProduccion = plnProduccion;
    }

    public Double getCrtVolumen() {
        return crtVolumen;
    }

    public void setCrtVolumen(Double crtVolumen) {
        this.crtVolumen = crtVolumen;
    }

    public Double getCrtHoras() {
        return crtHoras;
    }

    public void setCrtHoras(Double crtHoras) {
        this.crtHoras = crtHoras;
    }

    public Double getCrtSalario() {
        return crtSalario;
    }

    public void setCrtSalario(Double crtSalario) {
        this.crtSalario = crtSalario;
    }

    public Double getCrtSalarioInc() {
        return crtSalarioInc;
    }

    public void setCrtSalarioInc(Double crtSalarioInc) {
        this.crtSalarioInc = crtSalarioInc;
    }

    public Double getCrtProduccion() {
        return crtProduccion;
    }

    public void setCrtProduccion(Double crtProduccion) {
        this.crtProduccion = crtProduccion;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }
}