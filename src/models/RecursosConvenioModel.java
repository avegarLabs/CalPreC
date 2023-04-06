package models;

public class RecursosConvenioModel {

    public String obra;
    public String empresa;
    public int idBrigada;
    public String brigada;
    public int idGrupo;
    public String grupo;
    public int idCuadrilla;
    public String cuadrilla;
    public String recCode;
    public String recDescrip;
    public String um;
    public String tipo;
    public Double horascert;
    public Double horaSalariocert;

    public RecursosConvenioModel(String obra, String empresa, int idBrigada, String brigada, int idGrupo, String grupo, int idCuadrilla, String cuadrilla, String recCode, String recDescrip, String um, String tipo, Double horascert, Double horaSalariocert) {
        this.obra = obra;
        this.empresa = empresa;
        this.idBrigada = idBrigada;
        this.brigada = brigada;
        this.idGrupo = idGrupo;
        this.grupo = grupo;
        this.idCuadrilla = idCuadrilla;
        this.cuadrilla = cuadrilla;
        this.recCode = recCode;
        this.recDescrip = recDescrip;
        this.um = um;
        this.tipo = tipo;
        this.horascert = horascert;
        this.horaSalariocert = horaSalariocert;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getIdBrigada() {
        return idBrigada;
    }

    public void setIdBrigada(int idBrigada) {
        this.idBrigada = idBrigada;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getIdCuadrilla() {
        return idCuadrilla;
    }

    public void setIdCuadrilla(int idCuadrilla) {
        this.idCuadrilla = idCuadrilla;
    }

    public String getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(String cuadrilla) {
        this.cuadrilla = cuadrilla;
    }

    public String getRecCode() {
        return recCode;
    }

    public void setRecCode(String recCode) {
        this.recCode = recCode;
    }

    public String getRecDescrip() {
        return recDescrip;
    }

    public void setRecDescrip(String recDescrip) {
        this.recDescrip = recDescrip;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getHorascert() {
        return horascert;
    }

    public void setHorascert(Double horascert) {
        this.horascert = horascert;
    }

    public Double getHoraSalariocert() {
        return horaSalariocert;
    }

    public void setHoraSalariocert(Double horaSalariocert) {
        this.horaSalariocert = horaSalariocert;
    }
}
